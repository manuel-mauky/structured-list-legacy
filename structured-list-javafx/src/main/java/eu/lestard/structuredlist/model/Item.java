package eu.lestard.structuredlist.model;

import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import org.fxmisc.easybind.EasyBind;

import java.util.Optional;
import java.util.UUID;

import static eu.lestard.advanced_bindings.api.NumberBindings.*;

public class Item {

    private String id;

    private StringProperty text = new SimpleStringProperty();
    private Item parent;

    private ReadOnlyStringWrapper title = new ReadOnlyStringWrapper();

    private ReadOnlyIntegerWrapper recursiveNumberOfAllSubItems = new ReadOnlyIntegerWrapper(0);

    private ObservableList<Item> subItems = FXCollections.observableArrayList();

    public Item(){
        id = UUID.randomUUID().toString();
        title.bind(text); // todo: replace with logic to extract only the first line of the text as title

        final ObservableList<ReadOnlyIntegerProperty> numbersOfAllSubItems = EasyBind.map(subItems, Item::recursiveNumberOfAllSubItems);

        final ObservableValue<Number> sum = EasyBind.combine(numbersOfAllSubItems, stream -> stream.reduce(
            (a, b) ->
                a.intValue() + b.intValue()).orElse(0));

        recursiveNumberOfAllSubItems.bind(Bindings.size(subItems).add(asDouble(sum)));

        subItems.addListener((ListChangeListener<Item>) change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    change.getAddedSubList().forEach(item -> {
                        item.setParent(Item.this);
                    });
                }

                if (change.wasRemoved()) {
                    change.getRemoved().forEach(item -> {
                        item.setParent(null);
                    });
                }
            }
        });
    }

    public Item(String text){
        this();
        this.setText(text);
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


    public void addSubTask(String text){
        subItems.add(new Item(text));
    }

    public ObservableList<Item> getSubItems(){
        return subItems;
    }

    public String getTitle(){
        return title.get();
    }

    public ReadOnlyStringProperty titleProperty(){
        return title.getReadOnlyProperty();
    }

    public String getText() {
        return text.get();
    }

    public StringProperty textProperty() {
        return text;
    }

    public void setText(String text) {
        this.text.set(text);
    }

    public String getId(){
        return id;
    }

    void setParent(Item parentItem){
        this.parent = parentItem;
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
        return "Item[title='" + getTitle() + "']";
    }
}
