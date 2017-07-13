package eu.lestard.structuredlist.ui.menu;

import eu.lestard.fluxfx.View;
import eu.lestard.structuredlist.features.system.actions.ExitApplicationAction;
import eu.lestard.structuredlist.features.items.actions.NewItemAction;
import eu.lestard.structuredlist.ui.itemoverview.ItemInputDialog;
import eu.lestard.structuredlist.util.DialogUtil;
import javafx.fxml.FXML;

public class MenuView implements View {

    @FXML
    public void newItem(){
		
        ItemInputDialog dialog = new ItemInputDialog("Add new Item");

		DialogUtil.initDialogPositioning(dialog);
        dialog.showAndWait().ifPresent(text -> publishAction(new NewItemAction(text)));
    }

    @FXML
    public void exit(){
        publishAction(new ExitApplicationAction());
    }
}
