package eu.lestard.structuredlist.util;

import eu.lestard.structuredlist.model.Item;
import javafx.scene.control.TreeItem;
import org.junit.Test;

import static org.assertj.core.api.Assertions.*;

public class RecursiveTreeItemTest {

    @Test
    public void testTreeItemIsFilledOnInit(){
        Item root = new Item("root");
        Item sub1 = new Item("sub1");
        Item sub2 = new Item("sub2");

        root.getSubItems().addAll(sub1, sub2);

        Item sub2_1 = new Item("sub2_1");
        sub2.getSubItems().add(sub2_1);

        RecursiveTreeItem<Item> rootItem = new RecursiveTreeItem<>(root, Item::getSubItems);

        assertThat(rootItem.getChildren()).hasSize(2);

        final TreeItem<Item> subItem1 = rootItem.getChildren().get(0);
        final TreeItem<Item> subItem2 = rootItem.getChildren().get(1);

        assertThat(subItem1.getParent()).isSameAs(rootItem);
        assertThat(subItem2.getParent()).isSameAs(rootItem);

        assertThat(subItem1.getValue()).isSameAs(sub1);
        assertThat(subItem2.getValue()).isSameAs(sub2);

        assertThat(subItem1.getChildren()).isEmpty();
        assertThat(subItem2.getChildren()).hasSize(1);

        final TreeItem<Item> subItem2_1 = subItem2.getChildren().get(0);
        assertThat(subItem2_1.getParent()).isSameAs(subItem2);
        assertThat(subItem2_1.getValue()).isSameAs(sub2_1);
        assertThat(subItem2_1.getChildren()).isEmpty();
    }


    @Test
    public void testTreeItemsAreAddedWhenSubElementsAreAdded(){

        Item root = new Item("root");
        RecursiveTreeItem<Item> rootItem = new RecursiveTreeItem<>(root, Item::getSubItems);

        assertThat(rootItem.getChildren()).isEmpty();


        Item sub1 = new Item("sub1");
        root.getSubItems().add(sub1);

        assertThat(rootItem.getChildren()).hasSize(1);
        final TreeItem<Item> subItem1 = rootItem.getChildren().get(0);

        assertThat(subItem1).isNotNull();
        assertThat(subItem1.getChildren()).isEmpty();
        assertThat(subItem1.getValue()).isSameAs(sub1);
        assertThat(subItem1.getParent()).isSameAs(rootItem);


        Item sub2 = new Item("sub2");
        root.getSubItems().add(sub2);

        assertThat(rootItem.getChildren()).hasSize(2);
        final TreeItem<Item> subItem2 = rootItem.getChildren().get(1);

        assertThat(subItem2).isNotNull();
        assertThat(subItem2.getChildren()).isEmpty();
        assertThat(subItem2.getValue()).isSameAs(sub2);
        assertThat(subItem2.getParent()).isSameAs(rootItem);

        Item sub2_1 = new Item("sub2_1");
        sub2.getSubItems().add(sub2_1);

        assertThat(subItem2.getChildren()).hasSize(1);
        final TreeItem<Item> subItem2_1 = subItem2.getChildren().get(0);

        assertThat(subItem2_1).isNotNull();
        assertThat(subItem2_1.getChildren()).isEmpty();
        assertThat(subItem2_1.getValue()).isSameAs(sub2_1);
        assertThat(subItem2_1.getParent()).isSameAs(subItem2);
    }


    @Test
    public void testElementIsAddedAfterConstructor(){
        Item root = new Item("root");

        root.addSubTask("sub1");
        root.addSubTask("sub2");

        RecursiveTreeItem<Item> rootItem = new RecursiveTreeItem<Item>(Item::getSubItems);

        assertThat(rootItem.getChildren()).isEmpty();

        rootItem.setValue(root);
        assertThat(rootItem.getChildren()).hasSize(2);
    }

    @Test
    public void testTreeItemIsRemovedWhenElementIsRemoved(){

        Item root = new Item("root");

        Item sub1 = new Item("sub1");
        root.getSubItems().add(sub1);

        Item sub1_2 = new Item("sub1_2");
        sub1.getSubItems().add(sub1_2);

        Item sub2 = new Item("sub2");
        root.getSubItems().add(sub2);

        Item sub2_1 = new Item("sub2_1");
        Item sub2_2 = new Item("sub2_2");
        sub2.getSubItems().addAll(sub2_1, sub2_2);


        RecursiveTreeItem<Item> treeRoot = new RecursiveTreeItem<>(root, Item::getSubItems);

        final TreeItem<Item> treeSub1 = treeRoot.getChildren().get(0);
        assertThat(treeSub1.getChildren()).hasSize(1);

        sub1.getSubItems().remove(sub1_2);
        assertThat(treeSub1.getChildren()).isEmpty();


        assertThat(treeRoot.getChildren()).hasSize(2);

        root.getSubItems().remove(sub2);
        assertThat(treeRoot.getChildren()).hasSize(1);

    }

}
