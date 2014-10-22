package eu.lestard.tasky.main;

import de.saxsys.mvvmfx.FxmlView;
import eu.lestard.tasky.model.Model;
import eu.lestard.tasky.model.Task;
import javafx.fxml.FXML;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;

import javax.inject.Inject;

public class MainView implements FxmlView<MainViewModel> {


    @FXML
    private TreeTableView<Task> taskTreeView;

    @FXML
    private TreeTableColumn<Task, String> titleColumn;

    @Inject
    private Model model;


    public void initialize(){

        titleColumn.setCellValueFactory(param -> param.getValue().getValue().titleProperty());
    }

}
