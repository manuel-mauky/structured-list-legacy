package eu.lestard.structuredlist.ui.menu;

import de.saxsys.mvvmfx.ViewModel;
import eu.lestard.structuredlist.events.QuitEvent;
import eu.lestard.structuredlist.model.ItemsModel;

import javax.enterprise.event.Event;
import javax.inject.Inject;
import java.util.Optional;

public class MenuViewModel implements ViewModel {

    @Inject
    private ItemsModel itemsModel;

    @Inject
    private Event<QuitEvent> quitEvent;

    public void newItem(Optional<String> itemText){
        itemText.ifPresent(text-> itemsModel.getRoot().addSubTask(text));
    }

    public void exit() {
        quitEvent.fire(new QuitEvent());
    }
}
