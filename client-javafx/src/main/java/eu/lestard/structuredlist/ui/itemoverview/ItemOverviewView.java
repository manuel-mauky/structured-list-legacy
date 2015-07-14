package eu.lestard.structuredlist.ui.itemoverview;

import eu.lestard.fluxfx.View;
import eu.lestard.structuredlist.actions.EditItemAction;
import eu.lestard.structuredlist.actions.NewItemAction;
import eu.lestard.structuredlist.actions.RemoveItemAction;
import eu.lestard.structuredlist.stores.items.Item;
import eu.lestard.structuredlist.stores.items.ItemStore;
import eu.lestard.structuredlist.util.RecursiveTreeItem;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ObservableStringValue;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.Optional;

public class ItemOverviewView implements View {


    @FXML
    private TreeTableView<Item> itemTreeView;

    @FXML
    private TreeTableColumn<Item, String> titleColumn;

    @FXML
    private TreeTableColumn<Item, Integer> itemsColumn;


    private final ItemStore itemStore;

    public ItemOverviewView(ItemStore itemStore) {
        this.itemStore = itemStore;
    }

    public void initialize() {
        final TreeItem<Item> rootTreeItem = new RecursiveTreeItem<>(itemStore.getRootItem(), Item::getSubItems);
        itemTreeView.setRoot(rootTreeItem);
		
		
		itemTreeView.setOnMouseClicked(event -> {
			if(event.getClickCount() > 1) {
				editItem();
			}
		});

        titleColumn.setCellValueFactory(param ->
                createTitleColumnBinding(
                        param.getValue().getValue()));

        itemsColumn.setCellValueFactory(param ->
                createItemsColumnBinding(
                        param.getValue().getValue()));
    }
	
	public void editItem() {
		Optional.ofNullable(itemTreeView.getSelectionModel().getSelectedItem())
				.map(TreeItem::getValue)
				.ifPresent(item -> {
					ItemInputDialog dialog = new ItemInputDialog("Show/Edit Item", item.getText());
					
					dialog.showAndWait().ifPresent(newText ->
							publishAction(new EditItemAction(item.getId(), newText)));
				});
		
	}

    public void addItem() {
		ItemInputDialog dialog = new ItemInputDialog("Add new Item");

        dialog.showAndWait().ifPresent(text ->
                getSelectedItemId().ifPresent(parentId ->
                        publishAction(new NewItemAction(parentId, text))));
    }

    private Optional<String> getSelectedItemId() {
        return Optional.ofNullable(itemTreeView.getSelectionModel().getSelectedItem())
                .map(TreeItem::getValue)
                .map(Item::getId);
    }

    public void removeItem() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Remove Item");
        alert.setHeaderText("Remove the selected Item");
        alert.setContentText("Do you really want to remove the selected Item?");

        alert.showAndWait()
                .filter(button -> button.equals(ButtonType.OK))
                .flatMap(button -> getSelectedItemId())
                .ifPresent(itemId -> publishAction(new RemoveItemAction(itemId)));
    }


    private static ObservableStringValue createTitleColumnBinding(Item item) {
        return Bindings.concat(item.titleProperty(), " (", item.recursiveNumberOfAllSubItems(), ")");
    }

    private static ObservableValue<Integer> createItemsColumnBinding(Item item) {
        return item.recursiveNumberOfAllSubItems().asObject();
    }
}
