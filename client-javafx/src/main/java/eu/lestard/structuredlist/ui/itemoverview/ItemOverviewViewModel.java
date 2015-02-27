package eu.lestard.structuredlist.ui.itemoverview;

import de.saxsys.mvvmfx.ViewModel;
import eu.lestard.structuredlist.model.Item;
import eu.lestard.structuredlist.model.ItemsModel;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableStringValue;
import javafx.beans.value.ObservableValue;

import javax.inject.Singleton;
import java.util.Optional;

@Singleton
public class ItemOverviewViewModel implements ViewModel {

    private final ItemsModel itemsModel;
    private ObjectProperty<Item> selectedItem = new SimpleObjectProperty<>();

    public ItemOverviewViewModel(ItemsModel itemsModel){
        this.itemsModel = itemsModel;
    }

    public Item getRootItem(){
        return itemsModel.getRoot();
    }

    public ObjectProperty<Item> selectedItemProperty(){
        return selectedItem;
    }

    public Optional<Item> getSelectedItem(){
        return Optional.ofNullable(selectedItem.get());
    }

    public void addItem(Optional<String> dialogText){
        dialogText.ifPresent(text ->
            getSelectedItem().ifPresent(
                item ->
                    item.addSubTask(text)));
    }

    public void removeItem(){
        getSelectedItem().ifPresent(Item::remove);
    }

    public static ObservableStringValue createTitleColumnBinding(Item item){
        return Bindings.concat(item.textProperty(), " (", item.recursiveNumberOfAllSubItems(), ")");
    }

    public static ObservableValue<Integer> createItemsColumnBinding(Item item){
        return item.recursiveNumberOfAllSubItems().asObject();
    }
}
