package eu.lestard.structuredlist.stores.items;

import eu.lestard.structuredlist.eventsourcing.Event;
import eu.lestard.structuredlist.eventsourcing.EventStore;
import eu.lestard.structuredlist.eventsourcing.events.ItemCreatedEvent;
import eu.lestard.structuredlist.eventsourcing.events.ItemRemovedEvent;

import java.util.Optional;

public class RootItemFactory {
    public static final String ROOT_ID = "rootId";


    private final EventStore eventStore;

    public RootItemFactory(EventStore eventStore) {
        this.eventStore = eventStore;
    }

    public Item createRootItem() {

        Item rootItem = new Item(ROOT_ID, "root");

        for (Event event : eventStore.getEvents()) {
            if(event instanceof ItemCreatedEvent){
                processItemCreatedEvent(rootItem, (ItemCreatedEvent)event);
            }

            if(event instanceof ItemRemovedEvent) {
                processItemRemovedEvent(rootItem, (ItemRemovedEvent) event);
            }
        }

        return rootItem;
    }

    private void processItemCreatedEvent(Item rootItem, ItemCreatedEvent itemCreatedEvent) {
        final String parentId = itemCreatedEvent.getParentId();
        final String itemId = itemCreatedEvent.getItemId();
        final String text = itemCreatedEvent.getText();

        if(ROOT_ID.equals(parentId)) {
            rootItem.addSubItem(new Item(itemId, text));
        } else {
            rootItem.findRecursive(parentId)
                    .ifPresent(parent ->
                            parent.addSubItem(new Item(itemId, text)));
        }
    }


    private void processItemRemovedEvent(Item rootItem, ItemRemovedEvent event) {
        final Optional<Item> itemOptional = rootItem.findRecursive(event.getItemId());

        itemOptional.ifPresent(Item::remove);
    }

}
