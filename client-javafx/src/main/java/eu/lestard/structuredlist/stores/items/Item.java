package eu.lestard.structuredlist.stores.items;

import javafx.beans.binding.Bindings;
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
    private Item parent;


    private ReadOnlyIntegerWrapper recursiveNumberOfAllSubItems = new ReadOnlyIntegerWrapper(0);

    private ObservableList<Item> subItems = FXCollections.observableArrayList();
    private ObservableList<Item> readOnlySubItems = FXCollections.unmodifiableObservableList(subItems);


    Item(String id, String text) {
        this.id = id;
        this.setText(text);

        final ObservableList<ReadOnlyIntegerProperty> numbersOfAllSubItems = EasyBind.map(subItems, Item::recursiveNumberOfAllSubItems);

        final ObservableValue<Number> sum = EasyBind.combine(numbersOfAllSubItems, stream -> stream.reduce(
                (a, b) ->
                        a.intValue() + b.intValue()).orElse(0));

        recursiveNumberOfAllSubItems.bind(Bindings.size(subItems).add(asDouble(sum)));

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

    public ObservableList<Item> getSubItems(){
        return readOnlySubItems;
    }

    public String getText() {
        return text.get();
    }

    public ReadOnlyStringProperty textProperty() {
        return text.getReadOnlyProperty();
    }


    /**
     * This read-only property represents the recursive number of all sub items.
     * Example: This item has 3 sub-items and each of these sub-items itself has 2 sub-items.
     * Then this property will have the value 9 (3 + 3*2).
     *
     *
     * @return the recursive number of all sub items.
     */
    public ReadOnlyIntegerProperty recursiveNumberOfAllSubItems(){
        return recursiveNumberOfAllSubItems.getReadOnlyProperty();
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
}
