package eu.lestard.tasky.taskoverview;

import eu.lestard.tasky.model.Task;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.ReadOnlyStringWrapper;

/**
 * This class defines the viewModel for each row in the task TreeTable.
 */
public class TreeTableViewModel {

    private ReadOnlyStringWrapper title = new ReadOnlyStringWrapper();

    TreeTableViewModel(){
    }

    TreeTableViewModel(Task task){
        title.bind(task.titleProperty());
    }

    public ReadOnlyStringProperty titleProperty(){
        return title;
    }

}
