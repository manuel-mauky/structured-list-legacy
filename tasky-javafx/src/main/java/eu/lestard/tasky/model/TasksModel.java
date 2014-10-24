package eu.lestard.tasky.model;

import javax.inject.Singleton;

@Singleton
public class TasksModel {

    private Task root = new Task("root");

    public Task getRoot(){
        return root;
    }
}
