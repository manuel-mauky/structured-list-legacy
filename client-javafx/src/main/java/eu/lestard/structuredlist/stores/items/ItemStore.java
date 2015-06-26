package eu.lestard.structuredlist.stores.items;

import eu.lestard.fluxfx.Store;
import eu.lestard.structuredlist.actions.NewRootItemAction;
import eu.lestard.structuredlist.actions.NewSubItemAction;
import eu.lestard.structuredlist.actions.RemoveItemAction;

public class ItemStore extends Store {

    private Item root = new Item("root");

    public ItemStore() {
        subscribe(NewRootItemAction.class, this::processNewRootItem);
        subscribe(NewSubItemAction.class, this::processNewSubItem);
        subscribe(RemoveItemAction.class, this::processRemoveItem);
    }

    void processNewRootItem(NewRootItemAction action) {
        root.addSubItem(action.getText());
    }

    void processNewSubItem(NewSubItemAction action) {
        final String parentId = action.getParentId();

        root.findRecursive(parentId).ifPresent(parent -> {
            parent.addSubItem(action.getText());
        });
    }

    void processRemoveItem(RemoveItemAction action) {
        root.findRecursive(action.getItemId()).ifPresent(Item::remove);
    }

    public Item getRootItem() {
        return root;
    }

}
