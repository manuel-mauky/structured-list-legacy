package eu.lestard.tasky.ui.menu;

import eu.lestard.tasky.model.Task;
import eu.lestard.tasky.ui.taskoverview.TaskOverviewViewModel;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class MenuViewModelTest {

    private MenuViewModel viewModel;

    private TaskOverviewViewModel taskOverviewViewModel;

    @Before
    public void setup(){
        taskOverviewViewModel = mock(TaskOverviewViewModel.class);
        viewModel = new MenuViewModel();
        viewModel.taskOverviewViewModel = taskOverviewViewModel;
    }


    @Test
    public void testRemoveTask(){
        Task root = new Task("root");
        Task sub1 = new Task("sub1");
        root.getSubTasks().add(sub1);
        Task sub1_2 = new Task("sub1_2");
        sub1.getSubTasks().add(sub1_2);


        when(taskOverviewViewModel.getSelectedTask()).thenReturn(Optional.of(sub1_2));

        viewModel.removeTask();

        assertThat(sub1_2.getParent().isPresent()).isFalse();
        assertThat(sub1.getSubTasks()).doesNotContain(sub1_2);
    }

}
