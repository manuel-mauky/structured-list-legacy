package eu.lestard.structuredlist.stores.items;

import eu.lestard.fluxfx.Store;
import eu.lestard.structuredlist.actions.NewItemAction;
import eu.lestard.structuredlist.actions.RemoveItemAction;
import eu.lestard.structuredlist.eventsourcing.EventStore;
import eu.lestard.structuredlist.eventsourcing.events.ItemCreatedEvent;
import eu.lestard.structuredlist.eventsourcing.events.ItemRemovedEvent;

import javax.inject.Singleton;
import java.util.Optional;

@Singleton
public class ItemStore extends Store {

    private Item root;
    private EventStore eventStore;

    public ItemStore(EventStore eventStore, Item rootItem) {
        this.eventStore = eventStore;
        this.root = rootItem;

        subscribe(NewItemAction.class, this::processNewSubItem);
        subscribe(RemoveItemAction.class, this::processRemoveItem);
    }

    void processNewSubItem(NewItemAction action) {
        final Optional<String> parentIdOptional = action.getParentId();

        if(parentIdOptional.isPresent()) {
            root.findRecursive(parentIdOptional.get()).ifPresent(parent -> {
                final Item newItem = parent.addSubItem(action.getText());

                eventStore.push(new ItemCreatedEvent(parent.getId(), newItem.getId(), newItem.getText()));
            });
        } else {  // if no parent is defined, we add the item to the root item.

            final Item newItem = root.addSubItem(action.getText());

            eventStore.push(new ItemCreatedEvent(root.getId(), newItem.getId(), newItem.getText()));
        }
    }

    void processRemoveItem(RemoveItemAction action) {
        root.findRecursive(action.getItemId()).ifPresent(item -> {
            item.remove();

            eventStore.push(new ItemRemovedEvent(item.getId()));
        });
    }

    public Item getRootItem() {
        return root;
    }

}
