package eu.lestard.structuredlist.ui.itemoverview;

import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import eu.lestard.structuredlist.model.Item;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class ItemOverviewView implements FxmlView<ItemOverviewViewModel> {


    @FXML
    private TreeTableView<Item> itemTreeView;

    @FXML
    private TreeTableColumn<Item, String> titleColumn;

    @FXML
    private TreeTableColumn<Item, Integer> itemsColumn;

    @InjectViewModel
    private ItemOverviewViewModel viewModel;


    public void initialize() {
        itemTreeView.setRoot(viewModel.getRootNode());

        titleColumn.setCellValueFactory(param ->
            ItemOverviewViewModel.createTitleColumnBinding(
                param.getValue().getValue()));

        itemsColumn.setCellValueFactory(param ->
            ItemOverviewViewModel.createItemsColumnBinding(
                param.getValue().getValue()));

        viewModel.selectedItemProperty().bind(itemTreeView.getSelectionModel().selectedItemProperty());
    }

    public void addItem(){
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Add new Item");
        dialog.setHeaderText(null);
        viewModel.addItem(dialog.showAndWait());
    }

    public void removeItem(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Remove Item");
        alert.setHeaderText("Remove the selected Item");
        alert.setContentText("Do you really want to remove the selected Item?");

        alert.showAndWait().ifPresent(button->{
            if(button.equals(ButtonType.OK)){
                viewModel.removeItem();
            }
        });
    }

}
