package eu.lestard.structuredlist.stores;


import eu.lestard.fluxfx.Store;
import eu.lestard.structuredlist.actions.ExitApplicationAction;
import javafx.application.Platform;

import javax.inject.Singleton;

@Singleton
public class SystemStore extends Store {

    public SystemStore() {
        subscribe(ExitApplicationAction.class, this::processExit);
    }

    private void processExit(ExitApplicationAction action) {
        Platform.exit();
    }

}
