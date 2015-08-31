package eu.lestard.structuredlist.stores.items;

import org.junit.Test;

import static eu.lestard.assertj.javafx.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThat;

public class ItemTest {


    @Test
    public void testParentIsSet(){
        Item root = new Item("root");
        Item sub1 = new Item("sub1");

        assertThat(root.getParent().isPresent()).isFalse();
        assertThat(sub1.getParent().isPresent()).isFalse();

        root.addSubItem(sub1);

        assertThat(sub1.getParent().isPresent()).isTrue();
        assertThat(sub1.getParent().get()).isSameAs(root);


        root.addSubItem("sub2");

        final Item sub2 = root.getSubItems().get(1);
        assertThat(sub2.getParent().get()).isSameAs(root);
    }

    @Test
    public void testParentIsRemovedOnDelete(){
        Item root = new Item("root");
        Item sub1 = new Item("sub1");

        root.addSubItem(sub1);

        assertThat(sub1.getParent().get()).isSameAs(root);


        root.removeSubItem(sub1);
        assertThat(sub1.getParent().isPresent()).isFalse();
    }

    @Test
    public void testRecursiveNumberOfAllSubItems(){
        Item root = new Item("root");
        assertThat(root.recursiveNumberOfOpenSubItems()).hasValue(0);

        Item sub1 = new Item("sub1");
        root.addSubItem(sub1);

        assertThat(root.recursiveNumberOfOpenSubItems()).hasValue(1);


        Item sub2 = new Item("sub2");
        root.addSubItem(sub2);
        assertThat(root.recursiveNumberOfOpenSubItems()).hasValue(2);


        Item sub2_1 = new Item("sub2_1");
        sub2.addSubItem(sub2_1);
        assertThat(root.recursiveNumberOfOpenSubItems()).hasValue(3);

        Item sub2_2 = new Item("sub2_2");
        sub2.addSubItem(sub2_2);
        assertThat(root.recursiveNumberOfOpenSubItems()).hasValue(4);

        Item sub2_3 = new Item("sub2_3");
        sub2.addSubItem(sub2_3);
        assertThat(root.recursiveNumberOfOpenSubItems()).hasValue(5);


        Item sub2_3_1 = new Item("sub2_3_1");
        sub2_3.addSubItem(sub2_3_1);
        assertThat(root.recursiveNumberOfOpenSubItems()).hasValue(6);


        sub2.removeSubItem(sub2_3);
        assertThat(root.recursiveNumberOfOpenSubItems()).hasValue(4);
    }

    @Test
    public void testRemove(){
        Item root = new Item("root");

        Item sub1 = new Item("sub1");
        root.addSubItem(sub1);

        Item sub2 = new Item("sub2");
        root.addSubItem(sub2);

        assertThat(root.getSubItems()).contains(sub1, sub2);
        assertThat(sub1.getParent().get()).isEqualTo(root);



        sub1.remove();
        assertThat(root.getSubItems()).contains(sub2).doesNotContain(sub1);
        assertThat(sub1.getParent().isPresent()).isFalse();

    }
}
