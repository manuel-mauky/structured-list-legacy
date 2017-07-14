package eu.lestard.structuredlist.ui.itemoverview;

import eu.lestard.fluxfx.View;
import eu.lestard.structuredlist.features.items.actions.CompleteItemAction;
import eu.lestard.structuredlist.features.items.actions.EditItemAction;
import eu.lestard.structuredlist.features.items.actions.MoveItemAction;
import eu.lestard.structuredlist.features.items.actions.NewItemAction;
import eu.lestard.structuredlist.features.items.actions.RemoveItemAction;
import eu.lestard.structuredlist.features.items.Item;
import eu.lestard.structuredlist.features.items.ItemStore;
import eu.lestard.structuredlist.util.DialogUtil;
import eu.lestard.structuredlist.util.RecursiveTreeItem;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ObservableStringValue;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.util.Callback;

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
        final TreeItem<Item> rootTreeItem = new RecursiveTreeItem<>(itemStore.getRootItem(), Item::getOpenSubItems);
        itemTreeView.setRoot(rootTreeItem);
		
		
		itemTreeView.setOnMouseClicked(event -> {
			if (event.getClickCount() > 1) {
				editItem();
			}
		});


		itemTreeView.setRowFactory(new Callback<TreeTableView<Item>, TreeTableRow<Item>>() {
			@Override
			public TreeTableRow<Item> call(TreeTableView<Item> param) {
				TreeTableRow<Item> row = new TreeTableRow<>();

				row.setOnDragDetected(event -> {
					TreeItem<Item> selectedItem = itemTreeView.getSelectionModel().getSelectedItem();

					if(selectedItem != null) {
						Dragboard dragboard = itemTreeView.startDragAndDrop(TransferMode.ANY);

						dragboard.setDragView(row.snapshot(null, null));

						ClipboardContent content = new ClipboardContent();
						content.putString(selectedItem.getValue().getId());

						dragboard.setContent(content);

						event.consume();
					}
				});


				row.setOnDragOver(event -> {
					Dragboard dragboard = event.getDragboard();

					if(dragboard.hasString()) {
						String movedItemId = dragboard.getString();

						Item item = row.getTreeItem().getValue();

						event.acceptTransferModes(TransferMode.MOVE);

						if(item != null) {
							if(! itemStore.canBeMoved(movedItemId, item.getId())) {
								event.acceptTransferModes(TransferMode.NONE);
							};
						}

					}

					event.consume();
				});

				row.setOnDragDropped(event -> {

					Dragboard dragboard = event.getDragboard();

					boolean success = false;

					if(dragboard.hasString()) {
							String movedItemId = dragboard.getString();

							String newParentId = row.getTreeItem().getValue().getId();

							publishAction(new MoveItemAction(movedItemId, newParentId));

							success = true;
					}

					event.setDropCompleted(success);
					event.consume();
				});

				return row;
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

					DialogUtil.initDialogPositioning(dialog);

					dialog.showAndWait().ifPresent(newText ->
							publishAction(new EditItemAction(item.getId(), newText)));
				});
		
	}

    public void addItem() {
		ItemInputDialog dialog = new ItemInputDialog("Add new Item");
		DialogUtil.initDialogPositioning(dialog);
		
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

		DialogUtil.initDialogPositioning(alert);

        alert.showAndWait()
                .filter(button -> button.equals(ButtonType.OK))
                .flatMap(button -> getSelectedItemId())
                .ifPresent(itemId -> publishAction(new RemoveItemAction(itemId)));
    }

	public void completeItem() {
		getSelectedItemId().ifPresent(itemId -> publishAction(new CompleteItemAction(itemId)));
	}

    private static ObservableStringValue createTitleColumnBinding(Item item) {
        return Bindings.concat(item.titleProperty(), " (", item.recursiveNumberOfOpenSubItems(), ")");
    }

    private static ObservableValue<Integer> createItemsColumnBinding(Item item) {
        return item.recursiveNumberOfOpenSubItems().asObject();
    }

}
