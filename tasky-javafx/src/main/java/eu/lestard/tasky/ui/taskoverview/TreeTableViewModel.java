package eu.lestard.tasky.ui.taskoverview;

import eu.lestard.tasky.model.Task;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.ReadOnlyStringWrapper;

/**
 * This class defines the viewModel for each row in the task TreeTable.
 */
public class TreeTableViewModel {

    private ReadOnlyStringWrapper title = new ReadOnlyStringWrapper();

    private String id;

    TreeTableViewModel(){
        id = "";
    }

    TreeTableViewModel(Task task){
        id = task.getId();
        title.bind(task.titleProperty());
    }

    public ReadOnlyStringProperty titleProperty(){
        return title;
    }

    public String getId(){
        return id;
    }

    public String getTitle(){
        return title.get();
    }


    @Override
    public String toString() {
        return "TreeItem[title='" + getTitle() + "']";
    }
}
