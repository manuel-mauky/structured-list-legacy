package eu.lestard.tasky.ui.taskoverview;

import eu.lestard.tasky.model.Task;
import eu.lestard.tasky.model.TasksModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TaskOverviewViewModelTest {

    private TaskOverviewViewModel viewModel;

    private ObservableList<Task> rootTaskList;

    @Before
    public void setup(){
        rootTaskList = FXCollections.observableArrayList();

        TasksModel modelMock = mock(TasksModel.class);

//        when(modelMock.tasksProperty()).thenReturn(rootTaskList);

        viewModel = new TaskOverviewViewModel();
        viewModel.tasksModel = modelMock;

    }

    @Test
    public void rootTreeItemIsEmptyWhenNoTasksAreAvailableOnInit(){
        assertThat(rootTaskList).isEmpty();
        viewModel.init();

        final TreeItem<Task> rootNode = viewModel.getRootNode();

        assertThat(rootNode).isNotNull();
        assertThat(rootNode.getChildren()).isEmpty();
    }

    @Test
    public void rootTreeItemIsFilledWhenTasksAreAvailableOnInit(){
        rootTaskList.add(new Task("test 1"));
        rootTaskList.add(new Task("test 2"));

        viewModel.init();

        final TreeItem<Task> rootNode = viewModel.getRootNode();

        assertThat(rootNode).isNotNull();

        final ObservableList<TreeItem<Task>> treeItems = rootNode.getChildren();
        assertThat(treeItems).hasSize(2);

        assertThat(treeItems.get(0).getValue().getTitle()).isEqualTo("test 1");
        assertThat(treeItems.get(1).getValue().getTitle()).isEqualTo("test 2");


        assertThat(treeItems.get(0).getChildren()).isEmpty();
        assertThat(treeItems.get(1).getChildren()).isEmpty();
    }

    @Test
    public void treeItemsAreAddedWhenTasksAreAdded(){
        rootTaskList.add(new Task("test 1"));

        viewModel.init();

        final ObservableList<TreeItem<Task>> treeItems = viewModel.getRootNode().getChildren();

        assertThat(treeItems).hasSize(1);
        assertThat(treeItems.get(0).getValue().getTitle()).isEqualTo("test 1");


        rootTaskList.add(new Task("test 2"));
        assertThat(treeItems).hasSize(2);
        assertThat(treeItems.get(0).getValue().getTitle()).isEqualTo("test 1");
        assertThat(treeItems.get(1).getValue().getTitle()).isEqualTo("test 2");
    }

    @Test
    public void treeItemsAreRemovedWhenTasksAreRemoved(){
        rootTaskList.add(new Task("test 1"));
        rootTaskList.add(new Task("test 2"));

        viewModel.init();

        final ObservableList<TreeItem<Task>> treeItems = viewModel.getRootNode().getChildren();

        assertThat(treeItems).hasSize(2);

        rootTaskList.remove(0);

        assertThat(treeItems).hasSize(1);
        assertThat(treeItems.get(0).getValue().getTitle()).isEqualTo("test 2");
    }


    @Test
    public void treeItemsAreUpdatedWhenTasksAreReplaced(){
        rootTaskList.add(new Task("test 1"));
        rootTaskList.add(new Task("test 2"));


        viewModel.init();

        final ObservableList<TreeItem<Task>> treeItems = viewModel.getRootNode().getChildren();
        assertThat(getItemTitles(treeItems)).contains("test 1");

        rootTaskList.set(0, new Task("other text"));
        assertThat(getItemTitles(treeItems)).contains("other text").doesNotContain("test 1");
    }


    private List<String> getItemTitles(List<TreeItem<Task>> treeItems){
        return treeItems.stream().map(item -> item.getValue().getTitle()).collect(Collectors.toList());
    }

}
