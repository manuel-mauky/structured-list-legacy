package eu.lestard.structuredlist.ui.menu;

import de.saxsys.mvvmfx.ViewModel;
import eu.lestard.structuredlist.model.ItemsModel;

import javax.inject.Inject;
import java.util.Optional;

public class MenuViewModel implements ViewModel {

    @Inject
    private ItemsModel itemsModel;

    public void newItem(Optional<String> itemText){
        itemText.ifPresent(text-> itemsModel.getRoot().addSubTask(text));
    }

}
