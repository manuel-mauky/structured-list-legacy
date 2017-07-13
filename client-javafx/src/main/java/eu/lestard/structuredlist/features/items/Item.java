package eu.lestard.structuredlist.features.items;

import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import org.fxmisc.easybind.EasyBind;

import java.util.Optional;
import java.util.UUID;

import static eu.lestard.advanced_bindings.api.NumberBindings.asDouble;

public class Item {

    private String id;

    private ReadOnlyStringWrapper text = new ReadOnlyStringWrapper();
	
	private ReadOnlyBooleanWrapper completed = new ReadOnlyBooleanWrapper(false);

	/**
	 * The title is the first line of the {@link #text}.
	 */
	private ReadOnlyStringWrapper title = new ReadOnlyStringWrapper();
    private Item parent;


    private ReadOnlyIntegerWrapper recursiveNumberOfOpenSubItems = new ReadOnlyIntegerWrapper(0);

    private ObservableList<Item> subItems = FXCollections.observableArrayList(Item::getObservables);
    private ObservableList<Item> readOnlySubItems = FXCollections.unmodifiableObservableList(subItems);
	private ObservableList<Item> openSubItems = subItems.filtered(item -> !item.completed.get());


    Item(String id, String initText) {
        this.id = id;
        this.setText(initText);

        final ObservableList<ReadOnlyIntegerProperty> numbersOfAllSubItems = EasyBind.map(openSubItems, Item::recursiveNumberOfOpenSubItems);

        final ObservableValue<Number> sum = EasyBind.combine(numbersOfAllSubItems, stream -> stream.reduce(
                (a, b) ->
                        a.intValue() + b.intValue()).orElse(0));

        recursiveNumberOfOpenSubItems.bind(Bindings.size(openSubItems).add(asDouble(sum)));

        subItems.addListener((ListChangeListener<Item>) change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    change.getAddedSubList().forEach(item -> item.setParent(Item.this));
                }

                if (change.wasRemoved()) {
                    change.getRemoved().forEach(item -> item.setParent(null));
                }
            }
        });
		
		
		this.title.bind(Bindings.createStringBinding(() -> {
			final String text = getText() == null ? "" : getText();

			final String[] lines = text.split("\\r?\\n");
			
			if(lines.length > 0) {
				return lines[0];
			}
			return "";
		}, this.text));
    }

    Item(String text){
        this(UUID.randomUUID().toString(), text);
    }

    public Optional<Item> findRecursive(String itemId) {
        if(this.getId().equals(itemId)) {
            return Optional.of(this);
        } else {

            for (Item subItem : subItems) {

                final Optional<Item> subItemOptional = subItem.findRecursive(itemId);

                if(subItemOptional.isPresent()) {
                    return subItemOptional;
                }
            }
        }

        return Optional.empty();
    }

    void remove(){
        this.getParent().ifPresent(parent-> parent.subItems.remove(this));
    }

    Item addSubItem(String text){
        final Item newItem = new Item(text);
        subItems.add(newItem);

        return newItem;
    }

    void addSubItem(Item item) {
        subItems.add(item);
    }

    void removeSubItem(Item item) {
        subItems.remove(item);
    }

    void setText(String text) {
        this.text.set(text);
    }

    void setParent(Item parentItem){
        this.parent = parentItem;
    }
	
	void setCompleted(boolean completed) {
		this.completed.set(completed);
	}

    public ObservableList<Item> getSubItems(){
        return readOnlySubItems;
    }
	
	public ObservableList<Item> getOpenSubItems() {
		return openSubItems;
	}

    public String getText() {
        return text.get();
    }

    public ReadOnlyStringProperty textProperty() {
        return text.getReadOnlyProperty();
    }

	public ReadOnlyStringProperty titleProperty() {
		return title.getReadOnlyProperty();
	}
	
	public ReadOnlyBooleanProperty completedProperty() {
		return completed.getReadOnlyProperty();
	}

    /**
     * This read-only property represents the recursive number of all open (not completed) sub items.
     * Example: This item has 3 sub-items and each of these sub-items itself has 2 sub-items.
     * Then this property will have the value 9 (3 + 3*2).
     *
     *
     * @return the recursive number of all open sub items.
     */
    public ReadOnlyIntegerProperty recursiveNumberOfOpenSubItems(){
        return recursiveNumberOfOpenSubItems.getReadOnlyProperty();
    }


    public String getId(){
        return id;
    }


    public Optional<Item> getParent(){
        if(parent == null){
            return Optional.empty();
        }else{
            return Optional.of(parent);
        }
    }

    @Override
    public String toString() {
        return "Item[title='" + getText() + "']";
    }
	
	
	public Observable[] getObservables() {
		return new Observable[] {text, completed, title};
	}
}
