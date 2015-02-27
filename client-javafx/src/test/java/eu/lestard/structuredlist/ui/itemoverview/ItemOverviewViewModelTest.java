package eu.lestard.structuredlist.ui.itemoverview;

import eu.lestard.structuredlist.model.Item;
import eu.lestard.structuredlist.model.ItemsModel;
import eu.lestard.structuredlist.util.RecursiveTreeItem;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ItemOverviewViewModelTest {

    private ItemOverviewViewModel viewModel;

    private Item rootItem;

    private TreeItem<Item> rootNode;

    @Before
    public void setup(){
        rootItem = new Item("root");
        ItemsModel modelMock = mock(ItemsModel.class);
        when(modelMock.getRoot()).thenReturn(rootItem);

        viewModel = new ItemOverviewViewModel(modelMock);

        rootNode = new RecursiveTreeItem<Item>(viewModel.getRootItem(), Item::getSubItems);
    }

    @Test
    public void rootTreeItemIsEmptyWhenNoItemsAreAvailableOnInit(){
        assertThat(rootItem.getSubItems()).isEmpty();

        assertThat(rootNode).isNotNull();
        assertThat(rootNode.getChildren()).isEmpty();
    }

    @Test
    public void rootTreeItemIsFilledWhenItemsAreAvailableOnInit(){
        rootItem.addSubTask("test 1");
        rootItem.addSubTask("test 2");

        assertThat(rootNode).isNotNull();

        final ObservableList<TreeItem<Item>> treeItems = rootNode.getChildren();
        assertThat(treeItems).hasSize(2);

        assertThat(treeItems.get(0).getValue().getText()).isEqualTo("test 1");
        assertThat(treeItems.get(1).getValue().getText()).isEqualTo("test 2");


        assertThat(treeItems.get(0).getChildren()).isEmpty();
        assertThat(treeItems.get(1).getChildren()).isEmpty();
    }

    @Test
    public void treeItemsAreAddedWhenItemsAreAdded(){
        rootItem.addSubTask("test 1");

        final ObservableList<TreeItem<Item>> treeItems = rootNode.getChildren();

        assertThat(treeItems).hasSize(1);
        assertThat(treeItems.get(0).getValue().getText()).isEqualTo("test 1");


        rootItem.addSubTask("test 2");
        assertThat(treeItems).hasSize(2);
        assertThat(treeItems.get(0).getValue().getText()).isEqualTo("test 1");
        assertThat(treeItems.get(1).getValue().getText()).isEqualTo("test 2");
    }

    @Test
    public void treeItemsAreRemovedWhenItemsAreRemoved(){
        rootItem.addSubTask("test 1");
        rootItem.addSubTask("test 2");

        final ObservableList<TreeItem<Item>> treeItems = rootNode.getChildren();

        assertThat(treeItems).hasSize(2);

        rootItem.getSubItems().remove(0);

        assertThat(treeItems).hasSize(1);
        assertThat(treeItems.get(0).getValue().getText()).isEqualTo("test 2");
    }


    @Test
    public void treeItemsAreUpdatedWhenItemsAreReplaced(){
        rootItem.addSubTask("test 1");
        rootItem.addSubTask("test 2");


        final ObservableList<TreeItem<Item>> treeItems = rootNode.getChildren();
        assertThat(getItemTitles(treeItems)).contains("test 1");

        rootItem.getSubItems().set(0, new Item("other text"));
        assertThat(getItemTitles(treeItems)).contains("other text").doesNotContain("test 1");
    }


    private List<String> getItemTitles(List<TreeItem<Item>> treeItems){
        return treeItems.stream().map(item -> item.getValue().getText()).collect(Collectors.toList());
    }

}
