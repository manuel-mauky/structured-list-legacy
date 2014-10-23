package eu.lestard.tasky.ui.taskoverview;

import de.saxsys.mvvmfx.ViewModel;
import eu.lestard.tasky.model.TasksModel;
import javafx.scene.control.TreeItem;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

public class TaskOverviewViewModel implements ViewModel {

    private TreeItem<TreeTableViewModel> rootNode;

    @Inject
    private TasksModel tasksModel;

    public TaskOverviewViewModel(){
        rootNode = new TreeItem<>(new TreeTableViewModel());
    }

    @PostConstruct
    void init(){
        tasksModel.tasksProperty().forEach(task->rootNode.getChildren().add(new TreeItem<>(new TreeTableViewModel(task))));
    }


    public TreeItem<TreeTableViewModel> getRootNode(){
        return rootNode;
    }

}
