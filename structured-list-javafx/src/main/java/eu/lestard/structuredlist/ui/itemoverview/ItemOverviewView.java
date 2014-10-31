package eu.lestard.structuredlist.ui.itemoverview;

import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import eu.lestard.structuredlist.model.Item;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;

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

        titleColumn.setCellValueFactory(param -> {
            final Item item = param.getValue().getValue();

            return Bindings.concat(item.textProperty(), "(", item.recursiveNumberOfAllSubItems(), ")");
        });

        itemsColumn.setCellValueFactory(param ->
            param.getValue().getValue().recursiveNumberOfAllSubItems().asObject());

        viewModel.selectedItemProperty().bind(itemTreeView.getSelectionModel().selectedItemProperty());
    }

}
