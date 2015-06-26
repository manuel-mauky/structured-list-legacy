package eu.lestard.structuredlist.stores;

import eu.lestard.fluxfx.Action;
import eu.lestard.fluxfx.Store;
import eu.lestard.structuredlist.actions.ExitApplicationAction;
import javafx.application.Platform;

public class SystemStore extends Store {

    public <T extends Action> SystemStore() {
        subscribe(ExitApplicationAction.class, this::processExit);
    }

    private void processExit(ExitApplicationAction action) {
        Platform.exit();
    }

}
