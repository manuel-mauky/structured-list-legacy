package eu.lestard.tasky.model;

import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.UUID;

public class Task {

    private String id;

    private StringProperty text = new SimpleStringProperty();

    private ReadOnlyStringWrapper title = new ReadOnlyStringWrapper();

    private ObservableList<Task> subTasks = FXCollections.observableArrayList();

    public Task(){
        id = UUID.randomUUID().toString();
        title.bind(text); // todo: replace with logic to extract only the first line of the text as title
    }

    public Task(String text){
        this();
        this.setText(text);
    }

    public void addSubTask(String text){
        subTasks.add(new Task(text));
    }

    public ObservableList<Task> getSubTasks(){
        return subTasks;
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


    @Override
    public String toString() {
        return "Task[title='" + getTitle() + "']";
    }
}
