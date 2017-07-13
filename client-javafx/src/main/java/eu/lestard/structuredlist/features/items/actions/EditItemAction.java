package eu.lestard.structuredlist.features.items.actions;

import eu.lestard.fluxfx.Action;

/**
 * @author manuel.mauky
 */
public class EditItemAction implements Action {
	
	private final String itemId;
	
	private final String newText;

	public EditItemAction(String itemId, String newText) {
		this.itemId = itemId;
		this.newText = newText;
	}

	public String getItemId() {
		return itemId;
	}

	public String getNewText() {
		return newText;
	}
}
