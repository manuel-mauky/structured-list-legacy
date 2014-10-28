package eu.lestard.structuredlist.ui.menu;

import de.saxsys.mvvmfx.ViewModel;
import eu.lestard.structuredlist.ui.itemoverview.ItemOverviewViewModel;

import javax.inject.Inject;

public class MenuViewModel implements ViewModel {

    @Inject
    ItemOverviewViewModel itemOverviewViewModel;

    public void newItem(){
        itemOverviewViewModel.getSelectedItem().ifPresent(item -> {
            item.addSubTask("New Item");
        });
    }

    public void removeItem() {
        itemOverviewViewModel.getSelectedItem()
            .ifPresent(item ->
                item.getParent().ifPresent(
                    parent->
                        parent.getSubItems().remove(item)));
    }
}
