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

    private TreeItem<TreeTableViewModel> rootNode;

    @Inject
    TasksModel tasksModel;

    public TaskOverviewViewModel(){
        rootNode = new TreeItem<>(new TreeTableViewModel());
    }

    @PostConstruct
    void init(){
        tasksModel.tasksProperty().forEach(task->rootNode.getChildren().add(new TreeItem<>(new TreeTableViewModel(task))));

        tasksModel.tasksProperty().addListener((ListChangeListener<Task>) change -> {
            while(change.next()) {

                if (change.wasAdded()) {
                    change.getAddedSubList().forEach(newTask -> 
                        rootNode.getChildren().add(new TreeItem<>(new TreeTableViewModel(newTask))));
                }

                if (change.wasRemoved()) {
                    final List<String> idsToRemove = change.getRemoved().stream().map(Task::getId).collect(Collectors.toList());

                    final List<TreeItem<TreeTableViewModel>> treeItemsToRemove = rootNode.getChildren().stream()
                        .filter(treeItem -> idsToRemove.contains(treeItem.getValue().getId()))
                        .collect(Collectors.toList());

                    rootNode.getChildren().removeAll(treeItemsToRemove);
                }
            }
        });
    }


    public TreeItem<TreeTableViewModel> getRootNode(){
        return rootNode;
    }

}
