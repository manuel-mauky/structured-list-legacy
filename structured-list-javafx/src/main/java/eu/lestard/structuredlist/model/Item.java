package eu.lestard.structuredlist.model;

import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import java.util.Optional;
import java.util.UUID;

public class Item {

    private String id;

    private StringProperty text = new SimpleStringProperty();
    private Item parent;

    private ReadOnlyStringWrapper title = new ReadOnlyStringWrapper();

    private ObservableList<Item> subItems = FXCollections.observableArrayList();

    public Item(){
        id = UUID.randomUUID().toString();
        title.bind(text); // todo: replace with logic to extract only the first line of the text as title

        subItems.addListener((ListChangeListener<Item>) change -> {
            while(change.next()){
                if(change.wasAdded()){
                    change.getAddedSubList().forEach(item->{
                        item.setParent(Item.this);
                    });
                }

                if(change.wasRemoved()){
                    change.getRemoved().forEach(item->{
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
