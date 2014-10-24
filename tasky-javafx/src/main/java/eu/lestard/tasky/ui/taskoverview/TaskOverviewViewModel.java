package eu.lestard.tasky.ui.taskoverview;

import de.saxsys.mvvmfx.ViewModel;
import eu.lestard.tasky.model.Task;
import eu.lestard.tasky.model.TasksModel;
import eu.lestard.tasky.util.RecursiveTreeItem;
import javafx.scene.control.TreeItem;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

public class TaskOverviewViewModel implements ViewModel {

    private TreeItem<Task> rootNode;

    @Inject
    TasksModel tasksModel;

    public TaskOverviewViewModel(){
        rootNode = new RecursiveTreeItem<Task>(Task::getSubTasks);
    }

    @PostConstruct
    void init(){
        rootNode.setValue(tasksModel.getRoot());
    }


    public TreeItem<Task> getRootNode(){
        return rootNode;
    }

}
