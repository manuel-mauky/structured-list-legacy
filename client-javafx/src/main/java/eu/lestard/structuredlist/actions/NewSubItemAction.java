package eu.lestard.structuredlist.actions;

import eu.lestard.fluxfx.Action;

public class NewSubItemAction implements Action {

    private final String text;
    private final String parentId;

    public NewSubItemAction(String text, String parentId) {
        this.text = text;
        this.parentId = parentId;
    }

    public String getText() {
        return text;
    }

    public String getParentId() {
        return parentId;
    }
}
