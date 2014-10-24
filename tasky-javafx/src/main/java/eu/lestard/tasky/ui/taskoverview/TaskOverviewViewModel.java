package eu.lestard.tasky.ui.taskoverview;

import de.saxsys.mvvmfx.ViewModel;
import eu.lestard.tasky.model.Task;
import eu.lestard.tasky.model.TasksModel;
import javafx.collections.ListChangeListener;
import javafx.scene.control.TreeItem;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

public class TaskOverviewViewModel implements ViewModel {

    private TreeItem<Task> rootNode;

    @Inject
    TasksModel tasksModel;

    public TaskOverviewViewModel(){
        rootNode = new TreeItem<>(new Task("root"));
    }

    @PostConstruct
    void init(){
        tasksModel.tasksProperty().forEach(task->rootNode.getChildren().add(new TreeItem<>(task)));

        tasksModel.tasksProperty().addListener((ListChangeListener<Task>) change -> {
            while(change.next()) {

                if (change.wasAdded()) {
                    change.getAddedSubList().forEach(newTask -> 
                        rootNode.getChildren().add(new TreeItem<>(newTask)));
                }

                if (change.wasRemoved()) {

                    final List<TreeItem<Task>> treeItemsToRemove = rootNode.getChildren().stream().filter(treeItem -> change.getRemoved().contains(treeItem.getValue())).collect(Collectors.toList());

                    rootNode.getChildren().removeAll(treeItemsToRemove);
                }
            }
        });
    }


    public TreeItem<Task> getRootNode(){
        return rootNode;
    }

}
