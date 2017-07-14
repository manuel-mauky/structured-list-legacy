package eu.lestard.structuredlist.features.items.actions;

import eu.lestard.fluxfx.Action;

public class MoveItemAction implements Action {
	private final String itemId;

	private final String newParentId;

	public MoveItemAction(String itemId, String newParentId) {
		this.itemId = itemId;
		this.newParentId = newParentId;
	}

	public String getItemId() {
		return itemId;
	}

	public String getNewParentId() {
		return newParentId;
	}
}
