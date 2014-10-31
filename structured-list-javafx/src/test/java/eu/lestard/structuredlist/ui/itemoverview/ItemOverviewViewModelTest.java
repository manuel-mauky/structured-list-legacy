package eu.lestard.structuredlist.ui.itemoverview;

import eu.lestard.structuredlist.model.Item;
import eu.lestard.structuredlist.model.ItemsModel;
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

    @Before
    public void setup(){
        rootItem = new Item("root");
        ItemsModel modelMock = mock(ItemsModel.class);
        when(modelMock.getRoot()).thenReturn(rootItem);

        viewModel = new ItemOverviewViewModel();
        viewModel.itemsModel = modelMock;
    }

    @Test
    public void rootTreeItemIsEmptyWhenNoItemsAreAvailableOnInit(){
        assertThat(rootItem.getSubItems()).isEmpty();
        viewModel.init();

        final TreeItem<Item> rootNode = viewModel.getRootNode();

        assertThat(rootNode).isNotNull();
        assertThat(rootNode.getChildren()).isEmpty();
    }

    @Test
    public void rootTreeItemIsFilledWhenItemsAreAvailableOnInit(){
        rootItem.addSubTask("test 1");
        rootItem.addSubTask("test 2");

        viewModel.init();

        final TreeItem<Item> rootNode = viewModel.getRootNode();

        assertThat(rootNode).isNotNull();

        final ObservableList<TreeItem<Item>> treeItems = rootNode.getChildren();
        assertThat(treeItems).hasSize(2);

        assertThat(treeItems.get(0).getValue().getTitle()).isEqualTo("test 1");
        assertThat(treeItems.get(1).getValue().getTitle()).isEqualTo("test 2");


        assertThat(treeItems.get(0).getChildren()).isEmpty();
        assertThat(treeItems.get(1).getChildren()).isEmpty();
    }

    @Test
    public void treeItemsAreAddedWhenItemsAreAdded(){
        rootItem.addSubTask("test 1");

        viewModel.init();

        final ObservableList<TreeItem<Item>> treeItems = viewModel.getRootNode().getChildren();

        assertThat(treeItems).hasSize(1);
        assertThat(treeItems.get(0).getValue().getTitle()).isEqualTo("test 1");


        rootItem.addSubTask("test 2");
        assertThat(treeItems).hasSize(2);
        assertThat(treeItems.get(0).getValue().getTitle()).isEqualTo("test 1");
        assertThat(treeItems.get(1).getValue().getTitle()).isEqualTo("test 2");
    }

    @Test
    public void treeItemsAreRemovedWhenItemsAreRemoved(){
        rootItem.addSubTask("test 1");
        rootItem.addSubTask("test 2");

        viewModel.init();

        final ObservableList<TreeItem<Item>> treeItems = viewModel.getRootNode().getChildren();

        assertThat(treeItems).hasSize(2);

        rootItem.getSubItems().remove(0);

        assertThat(treeItems).hasSize(1);
        assertThat(treeItems.get(0).getValue().getTitle()).isEqualTo("test 2");
    }


    @Test
    public void treeItemsAreUpdatedWhenItemsAreReplaced(){
        rootItem.addSubTask("test 1");
        rootItem.addSubTask("test 2");


        viewModel.init();

        final ObservableList<TreeItem<Item>> treeItems = viewModel.getRootNode().getChildren();
        assertThat(getItemTitles(treeItems)).contains("test 1");

        rootItem.getSubItems().set(0, new Item("other text"));
        assertThat(getItemTitles(treeItems)).contains("other text").doesNotContain("test 1");
    }


    private List<String> getItemTitles(List<TreeItem<Item>> treeItems){
        return treeItems.stream().map(item -> item.getValue().getTitle()).collect(Collectors.toList());
    }

}
