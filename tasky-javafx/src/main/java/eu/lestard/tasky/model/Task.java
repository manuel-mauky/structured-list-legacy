package eu.lestard.tasky.model;

import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Task {

    private StringProperty text = new SimpleStringProperty();

    private ReadOnlyStringWrapper title = new ReadOnlyStringWrapper();

    private ObservableList<Task> subTasks = FXCollections.observableArrayList();

    public Task(){
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

}
