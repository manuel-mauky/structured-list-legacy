package eu.lestard.structuredlist.ui.menu;

import de.saxsys.mvvmfx.ViewModel;
import de.saxsys.mvvmfx.utils.notifications.NotificationCenter;
import eu.lestard.structuredlist.model.ItemsModel;

import java.util.Optional;

public class MenuViewModel implements ViewModel {

    private ItemsModel itemsModel;

    private NotificationCenter notificationCenter;

    public MenuViewModel(ItemsModel itemsModel, NotificationCenter notificationCenter){
        this.itemsModel = itemsModel;
        this.notificationCenter = notificationCenter;
    }

    public void newItem(Optional<String> itemText){
        itemText.ifPresent(text-> itemsModel.getRoot().addSubTask(text));
    }

    public void exit() {
        notificationCenter.publish("exit");
    }
}
