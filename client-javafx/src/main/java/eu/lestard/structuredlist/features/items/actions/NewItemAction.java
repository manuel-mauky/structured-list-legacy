package eu.lestard.structuredlist.features.items.actions;

import eu.lestard.fluxfx.Action;

import java.util.Optional;

public class NewItemAction implements Action {

    private final String text;
    private final String parentId;

    public NewItemAction(String parentId, String text) {
        this.parentId = parentId;
        this.text = text;
    }

    public NewItemAction(String text) {
        this.text = text;
        this.parentId = null;
    }

    public String getText() {
        return text;
    }

    public Optional<String> getParentId() {
        return Optional.ofNullable(parentId);
    }
}
