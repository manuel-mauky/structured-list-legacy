package eu.lestard.tasky.util;

import eu.lestard.tasky.model.Task;
import javafx.scene.control.TreeItem;
import org.junit.Test;

import static org.assertj.core.api.Assertions.*;

public class RecursiveTreeItemTest {

    @Test
    public void testTreeItemIsFilledOnInit(){
        Task root = new Task("root");
        Task sub1 = new Task("sub1");
        Task sub2 = new Task("sub2");

        root.getSubTasks().addAll(sub1, sub2);

        Task sub2_1 = new Task("sub2_1");
        sub2.getSubTasks().add(sub2_1);

        RecursiveTreeItem<Task> rootItem = new RecursiveTreeItem<>(root, Task::getSubTasks);

        assertThat(rootItem.getChildren()).hasSize(2);

        final TreeItem<Task> subItem1 = rootItem.getChildren().get(0);
        final TreeItem<Task> subItem2 = rootItem.getChildren().get(1);

        assertThat(subItem1.getParent()).isSameAs(rootItem);
        assertThat(subItem2.getParent()).isSameAs(rootItem);

        assertThat(subItem1.getValue()).isSameAs(sub1);
        assertThat(subItem2.getValue()).isSameAs(sub2);

        assertThat(subItem1.getChildren()).isEmpty();
        assertThat(subItem2.getChildren()).hasSize(1);

        final TreeItem<Task> subItem2_1 = subItem2.getChildren().get(0);
        assertThat(subItem2_1.getParent()).isSameAs(subItem2);
        assertThat(subItem2_1.getValue()).isSameAs(sub2_1);
        assertThat(subItem2_1.getChildren()).isEmpty();
    }


    @Test
    public void testTreeItemsAreAddedWhenSubElementsAreAdded(){

        Task root = new Task("root");
        RecursiveTreeItem<Task> rootItem = new RecursiveTreeItem<>(root, Task::getSubTasks);

        assertThat(rootItem.getChildren()).isEmpty();


        Task sub1 = new Task("sub1");
        root.getSubTasks().add(sub1);

        assertThat(rootItem.getChildren()).hasSize(1);
        final TreeItem<Task> subItem1 = rootItem.getChildren().get(0);

        assertThat(subItem1).isNotNull();
        assertThat(subItem1.getChildren()).isEmpty();
        assertThat(subItem1.getValue()).isSameAs(sub1);
        assertThat(subItem1.getParent()).isSameAs(rootItem);


        Task sub2 = new Task("sub2");
        root.getSubTasks().add(sub2);

        assertThat(rootItem.getChildren()).hasSize(2);
        final TreeItem<Task> subItem2 = rootItem.getChildren().get(1);

        assertThat(subItem2).isNotNull();
        assertThat(subItem2.getChildren()).isEmpty();
        assertThat(subItem2.getValue()).isSameAs(sub2);
        assertThat(subItem2.getParent()).isSameAs(rootItem);

        Task sub2_1 = new Task("sub2_1");
        sub2.getSubTasks().add(sub2_1);

        assertThat(subItem2.getChildren()).hasSize(1);
        final TreeItem<Task> subItem2_1 = subItem2.getChildren().get(0);

        assertThat(subItem2_1).isNotNull();
        assertThat(subItem2_1.getChildren()).isEmpty();
        assertThat(subItem2_1.getValue()).isSameAs(sub2_1);
        assertThat(subItem2_1.getParent()).isSameAs(subItem2);
    }


    @Test
    public void testValueIsAddedAfterConstructor(){
        Task root = new Task("root");

        root.addSubTask("sub1");
        root.addSubTask("sub2");

        RecursiveTreeItem<Task> rootItem = new RecursiveTreeItem<Task>(Task::getSubTasks);

        assertThat(rootItem.getChildren()).isEmpty();

        rootItem.setValue(root);
        assertThat(rootItem.getChildren()).hasSize(2);


    }



}
