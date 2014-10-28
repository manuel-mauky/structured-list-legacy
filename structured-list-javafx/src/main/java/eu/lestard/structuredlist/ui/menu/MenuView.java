package eu.lestard.structuredlist.ui.menu;

import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import javafx.fxml.FXML;

public class MenuView implements FxmlView<MenuViewModel> {

    @InjectViewModel
    private MenuViewModel viewModel;

    @FXML
    public void newItem(){
        viewModel.newItem();
    }


    @FXML
    public void removeItem(){
        viewModel.removeItem();
    }

}
