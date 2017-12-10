package eu.lestard.structuredlist.ui.itemoverview;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import eu.lestard.fluxfx.View;
import eu.lestard.structuredlist.features.items.Item;
import eu.lestard.structuredlist.features.items.ItemStore;
import eu.lestard.structuredlist.features.items.actions.CompleteItemAction;
import eu.lestard.structuredlist.features.items.actions.EditItemAction;
import eu.lestard.structuredlist.features.items.actions.MoveItemAction;
import eu.lestard.structuredlist.features.items.actions.NewItemAction;
import eu.lestard.structuredlist.features.items.actions.RemoveItemAction;
import eu.lestard.structuredlist.util.DialogUtil;
import eu.lestard.structuredlist.util.RecursiveTreeItem;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ObservableStringValue;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableRow;
import javafx.scene.control.TreeTableView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;

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
				Optional.ofNullable(itemTreeView.getSelectionModel().getSelectedItem())
					.map(TreeItem::getValue)
					.ifPresent(this::editItem);
			}
		});

		ContextMenu treeViewContextMenu = new ContextMenu();
		MenuItem addRootMenuItem = new MenuItem("Add Item");
		addRootMenuItem.setGraphic(new FontAwesomeIconView(FontAwesomeIcon.PLUS));
		addRootMenuItem.setOnAction(event -> addRootItem());
		treeViewContextMenu.getItems().add(addRootMenuItem);

		itemTreeView.setContextMenu(treeViewContextMenu);


		itemTreeView.setRowFactory(param -> {

			ContextMenu rowContextMenu = new ContextMenu();
			MenuItem addMenuItem = new MenuItem("Add Item");
			addMenuItem.setGraphic(new FontAwesomeIconView(FontAwesomeIcon.PLUS));
			MenuItem editMenuItem = new MenuItem("Show/Edit Item");
			editMenuItem.setGraphic(new FontAwesomeIconView(FontAwesomeIcon.PENCIL));
			MenuItem completeMenuItem = new MenuItem("Complete");
			completeMenuItem.setGraphic(new FontAwesomeIconView(FontAwesomeIcon.CHECK));
			MenuItem removeMenuItem = new MenuItem("Remove Item");
			removeMenuItem.setGraphic(new FontAwesomeIconView(FontAwesomeIcon.TRASH));

			rowContextMenu.getItems().addAll(addMenuItem, editMenuItem, completeMenuItem, removeMenuItem);

			TreeTableRow<Item> row = new TreeTableRow<Item>() {

				@Override
				protected void updateItem(Item item, boolean empty) {
					super.updateItem(item, empty);

					if(empty) {
						setContextMenu(null);
					} else {
						setContextMenu(rowContextMenu);
					}
				}
			};


			addMenuItem.setOnAction(event -> addSubItem(row.getItem()));
			editMenuItem.setOnAction(event -> editItem(row.getItem()));
			completeMenuItem.setOnAction(event -> completeItem(row.getItem()));
			removeMenuItem.setOnAction(event -> removeItem(row.getItem()));

			row.setOnDragDetected(event -> {
				final Item item = row.getItem();

				if(item != null) {
					Dragboard dragboard = itemTreeView.startDragAndDrop(TransferMode.ANY);

					dragboard.setDragView(row.snapshot(null, null));

					ClipboardContent content = new ClipboardContent();
					content.putString(item.getId());

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
						}
					}
				}

				event.consume();
			});

			row.setOnDragDropped(event -> {
				Dragboard dragboard = event.getDragboard();

				boolean success = false;

				if (dragboard.hasString()) {
					String movedItemId = dragboard.getString();

					final Item value = row.getTreeItem().getValue();
					String newParentId = value.getId();

					publishAction(new MoveItemAction(movedItemId, newParentId));
					success = true;
				}

				event.setDropCompleted(success);
				event.consume();
			});

			return row;
		});

        titleColumn.setCellValueFactory(param ->
				createTitleColumnBinding(
						param.getValue().getValue()));

        itemsColumn.setCellValueFactory(param ->
				createItemsColumnBinding(
						param.getValue().getValue()));
    }

	private void addSubItem(Item parent) {
		ItemInputDialog dialog = new ItemInputDialog("Add new Item");
		DialogUtil.initDialogPositioning(dialog);

		dialog.showAndWait().ifPresent(text ->
				publishAction(new NewItemAction(parent.getId(), text)));
	}


	public void editItem(Item item) {
		ItemInputDialog dialog = new ItemInputDialog("Show/Edit Item", item.getText());

		DialogUtil.initDialogPositioning(dialog);

		dialog.showAndWait().ifPresent(newText ->
				publishAction(new EditItemAction(item.getId(), newText)));

	}

    public void addRootItem() {
		ItemInputDialog dialog = new ItemInputDialog("Add new Item");
		DialogUtil.initDialogPositioning(dialog);

		dialog.showAndWait().ifPresent(text ->
				publishAction(new NewItemAction(text)));
	}

    public void removeItem(Item item) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Remove Item");
        alert.setHeaderText("Remove the selected Item");
        alert.setContentText("Do you really want to remove the selected Item?");

		DialogUtil.initDialogPositioning(alert);

        alert.showAndWait()
                .filter(button -> button.equals(ButtonType.OK))
                .ifPresent(buttonType -> publishAction(new RemoveItemAction(item.getId())));
    }

	public void completeItem(Item item) {
    	publishAction(new CompleteItemAction(item.getId()));
	}

    private static ObservableStringValue createTitleColumnBinding(Item item) {
        return Bindings.concat(item.titleProperty(), " (", item.recursiveNumberOfOpenSubItems(), ")");
    }

    private static ObservableValue<Integer> createItemsColumnBinding(Item item) {
        return item.recursiveNumberOfOpenSubItems().asObject();
    }

}
