package eu.lestard.structuredlist.actions;

import eu.lestard.fluxfx.Action;

public class NewRootItemAction implements Action {

    private final String text;

    public NewRootItemAction(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
