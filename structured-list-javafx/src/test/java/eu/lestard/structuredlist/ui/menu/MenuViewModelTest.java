package eu.lestard.structuredlist.ui.menu;

import eu.lestard.structuredlist.model.Item;
import eu.lestard.structuredlist.ui.itemoverview.ItemOverviewViewModel;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class MenuViewModelTest {

    private MenuViewModel viewModel;

    private ItemOverviewViewModel itemOverviewViewModel;

    @Before
    public void setup(){
        itemOverviewViewModel = mock(ItemOverviewViewModel.class);
        viewModel = new MenuViewModel();
        viewModel.itemOverviewViewModel = itemOverviewViewModel;
    }


    @Test
    public void testRemoveTask(){
        Item root = new Item("root");
        Item sub1 = new Item("sub1");
        root.getSubItems().add(sub1);
        Item sub1_2 = new Item("sub1_2");
        sub1.getSubItems().add(sub1_2);


        when(itemOverviewViewModel.getSelectedItem()).thenReturn(Optional.of(sub1_2));

        viewModel.removeItem();

        assertThat(sub1_2.getParent().isPresent()).isFalse();
        assertThat(sub1.getSubItems()).doesNotContain(sub1_2);
    }

}
