package eu.lestard.tasky.model;

import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import java.util.Optional;
import java.util.UUID;

public class Task {

    private String id;

    private StringProperty text = new SimpleStringProperty();
    private Task parent;

    private ReadOnlyStringWrapper title = new ReadOnlyStringWrapper();

    private ObservableList<Task> subTasks = FXCollections.observableArrayList();

    public Task(){
        id = UUID.randomUUID().toString();
        title.bind(text); // todo: replace with logic to extract only the first line of the text as title

        subTasks.addListener((ListChangeListener<Task>) change -> {
            while(change.next()){
                if(change.wasAdded()){
                    change.getAddedSubList().forEach(task->{
                        task.setParent(Task.this);
                    });
                }

                if(change.wasRemoved()){
                    change.getRemoved().forEach(task->{
                        task.setParent(null);
                    });
                }
            }
        });
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

    void setParent(Task parentTask){
        this.parent = parentTask;
    }

    public Optional<Task> getParent(){
        if(parent == null){
            return Optional.empty();
        }else{
            return Optional.of(parent);
        }
    }

    @Override
    public String toString() {
        return "Task[title='" + getTitle() + "']";
    }
}
