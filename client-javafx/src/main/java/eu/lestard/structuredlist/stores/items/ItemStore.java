package eu.lestard.structuredlist.stores.items;

import eu.lestard.fluxfx.Store;
import eu.lestard.structuredlist.actions.NewRootItemAction;
import eu.lestard.structuredlist.actions.NewSubItemAction;
import eu.lestard.structuredlist.actions.RemoveItemAction;
import eu.lestard.structuredlist.eventsourcing.EventStore;
import eu.lestard.structuredlist.eventsourcing.events.ItemCreatedEvent;

import javax.inject.Singleton;
import java.util.List;
import java.util.stream.Collectors;

@Singleton
public class ItemStore extends Store {

    private Item root;
    private EventStore eventStore;

    public ItemStore(EventStore eventStore) {
        this.eventStore = eventStore;
        subscribe(NewRootItemAction.class, this::processNewRootItem);
        subscribe(NewSubItemAction.class, this::processNewSubItem);
        subscribe(RemoveItemAction.class, this::processRemoveItem);

        List<ItemCreatedEvent> itemCreatedEventList = eventStore.getEvents().stream()
                .filter(event -> event instanceof ItemCreatedEvent)
                .map(event -> (ItemCreatedEvent) event)
                .collect(Collectors.toList());

        if (itemCreatedEventList.isEmpty()) {
            // if there are no such items it's obviously the first time the app is started.
            // in such a situation we need to create our root item.

            root = new Item("root");
            eventStore.push(new ItemCreatedEvent(null, root.getId(), root.getText()));
        } else {

            itemCreatedEventList.forEach(event -> {
                if (root == null) {
                    // assume that the first event creates the root item.
                    root = new Item(event.getItemId(), event.getText());
                } else {
                    event.getParentId()
                            .flatMap(root::findRecursive)
                            .ifPresent(parentItem -> {
                                parentItem.addSubItem(new Item(event.getItemId(), event.getText()));
                            });
                }
            });
        }
    }


    void processNewRootItem(NewRootItemAction action) {
        final Item newItem = root.addSubItem(action.getText());

        eventStore.push(new ItemCreatedEvent(root.getId(), newItem.getId(), newItem.getText()));
    }

    void processNewSubItem(NewSubItemAction action) {
        final String parentId = action.getParentId();

        root.findRecursive(parentId).ifPresent(parent -> {
            final Item newItem = parent.addSubItem(action.getText());

            eventStore.push(new ItemCreatedEvent(parent.getId(), newItem.getId(), newItem.getText()));
        });
    }

    void processRemoveItem(RemoveItemAction action) {
        root.findRecursive(action.getItemId()).ifPresent(Item::remove);
    }

    public Item getRootItem() {
        return root;
    }

}
