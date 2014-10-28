package eu.lestard.tasky.ui.taskoverview;

import de.saxsys.mvvmfx.ViewModel;
import eu.lestard.tasky.model.Task;
import eu.lestard.tasky.model.TasksModel;
import eu.lestard.tasky.util.RecursiveTreeItem;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.TreeItem;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Optional;

@Singleton
public class TaskOverviewViewModel implements ViewModel {

    private TreeItem<Task> rootNode;

    private ObjectProperty<TreeItem<Task>> selectedTask = new SimpleObjectProperty<>();

    @Inject
    TasksModel tasksModel;

    public TaskOverviewViewModel(){
        rootNode = new RecursiveTreeItem<>(Task::getSubTasks);
    }

    @PostConstruct
    void init(){
        rootNode.setValue(tasksModel.getRoot());
    }


    public TreeItem<Task> getRootNode(){
        return rootNode;
    }

    public ObjectProperty<TreeItem<Task>> selectedTaskProperty(){
        return selectedTask;
    }

    public Optional<Task> getSelectedTask(){
        final TreeItem<Task> selectedTreeItem = selectedTask.get();
        if(selectedTreeItem != null){
            return Optional.of(selectedTreeItem.getValue());
        }
        return Optional.empty();
    }
}
