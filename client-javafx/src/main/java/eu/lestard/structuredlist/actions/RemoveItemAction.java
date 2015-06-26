package eu.lestard.structuredlist.actions;

import eu.lestard.fluxfx.Action;

public class RemoveItemAction implements Action {

    private final String itemId;

    public RemoveItemAction(String itemId) {
        this.itemId = itemId;
    }

    public String getItemId() {
        return itemId;
    }
}
