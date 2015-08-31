package eu.lestard.structuredlist.actions;

import eu.lestard.fluxfx.Action;

public class CompleteItemAction implements Action {
	
	private final String itemId;

	public CompleteItemAction(String itemId) {
		this.itemId = itemId;
	}

	public String getItemId() {
		return itemId;
	}
}
