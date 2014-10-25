package eu.lestard.tasky.model;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TaskTest {


    @Test
    public void testParentIsSet(){
        Task root = new Task("root");
        Task sub1 = new Task("sub1");

        assertThat(root.getParent().isPresent()).isFalse();
        assertThat(sub1.getParent().isPresent()).isFalse();

        root.getSubTasks().add(sub1);

        assertThat(sub1.getParent().isPresent()).isTrue();
        assertThat(sub1.getParent().get()).isSameAs(root);


        root.addSubTask("sub2");

        final Task sub2 = root.getSubTasks().get(1);
        assertThat(sub2.getParent().get()).isSameAs(root);
    }

    @Test
    public void testParentIsRemovedOnDelete(){
        Task root = new Task("root");
        Task sub1 = new Task("sub1");

        root.getSubTasks().add(sub1);

        assertThat(sub1.getParent().get()).isSameAs(root);


        root.getSubTasks().remove(sub1);
        assertThat(sub1.getParent().isPresent()).isFalse();
    }
}
