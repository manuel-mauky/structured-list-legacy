package eu.lestard.tasky.ui.menu;

import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import javafx.fxml.FXML;

public class MenuView implements FxmlView<MenuViewModel> {

    @InjectViewModel
    private MenuViewModel viewModel;

    @FXML
    public void newTask(){
        viewModel.newTask();
    }


    @FXML
    public void removeTask(){
        viewModel.removeTask();
    }

}
