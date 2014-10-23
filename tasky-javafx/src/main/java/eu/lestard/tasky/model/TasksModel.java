package eu.lestard.tasky.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javax.inject.Singleton;

@Singleton
public class TasksModel {

    private ObservableList<Task> tasks = FXCollections.observableArrayList();


    public ObservableList<Task> tasksProperty(){
        return tasks;
    }

    public void addTasks(Task ...task){
        tasks.addAll(task);
    }
}
