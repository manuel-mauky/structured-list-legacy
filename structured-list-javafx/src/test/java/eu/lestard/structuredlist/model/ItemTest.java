package eu.lestard.structuredlist.model;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ItemTest {


    @Test
    public void testParentIsSet(){
        Item root = new Item("root");
        Item sub1 = new Item("sub1");

        assertThat(root.getParent().isPresent()).isFalse();
        assertThat(sub1.getParent().isPresent()).isFalse();

        root.getSubItems().add(sub1);

        assertThat(sub1.getParent().isPresent()).isTrue();
        assertThat(sub1.getParent().get()).isSameAs(root);


        root.addSubTask("sub2");

        final Item sub2 = root.getSubItems().get(1);
        assertThat(sub2.getParent().get()).isSameAs(root);
    }

    @Test
    public void testParentIsRemovedOnDelete(){
        Item root = new Item("root");
        Item sub1 = new Item("sub1");

        root.getSubItems().add(sub1);

        assertThat(sub1.getParent().get()).isSameAs(root);


        root.getSubItems().remove(sub1);
        assertThat(sub1.getParent().isPresent()).isFalse();
    }
}
