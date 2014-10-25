package eu.lestard.tasky.ui.menu;

import de.saxsys.mvvmfx.ViewModel;
import eu.lestard.tasky.ui.taskoverview.TaskOverviewViewModel;

import javax.inject.Inject;

public class MenuViewModel implements ViewModel {

    @Inject
    TaskOverviewViewModel taskOverviewViewModel;

    public void newTask(){
        taskOverviewViewModel.getSelectedTask().ifPresent(task -> {
            task.addSubTask("New Task");
        });
    }

    public void removeTask() {
        taskOverviewViewModel.getSelectedTask()
            .ifPresent(task ->
                task.getParent().ifPresent(
                    parent->
                        parent.getSubTasks().remove(task)));
    }
}
