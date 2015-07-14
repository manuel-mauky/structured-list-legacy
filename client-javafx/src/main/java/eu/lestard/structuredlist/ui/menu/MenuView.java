package eu.lestard.structuredlist.ui.menu;

import eu.lestard.fluxfx.View;
import eu.lestard.structuredlist.actions.ExitApplicationAction;
import eu.lestard.structuredlist.actions.NewItemAction;
import eu.lestard.structuredlist.ui.itemoverview.ItemInputDialog;
import javafx.fxml.FXML;
import javafx.scene.control.TextInputDialog;

public class MenuView implements View {

    @FXML
    public void newItem(){
		
        ItemInputDialog dialog = new ItemInputDialog("Add new Item");
        dialog.showAndWait().ifPresent(text -> publishAction(new NewItemAction(text)));
    }

    @FXML
    public void exit(){
        publishAction(new ExitApplicationAction());
    }
}
