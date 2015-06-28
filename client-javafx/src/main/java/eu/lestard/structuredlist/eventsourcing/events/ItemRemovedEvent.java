package eu.lestard.structuredlist.eventsourcing.events;

import eu.lestard.structuredlist.eventsourcing.Event;

public class ItemRemovedEvent extends Event {
    private final String itemId;

    public ItemRemovedEvent(String itemId) {
        this.itemId = itemId;
    }

    public String getItemId() {
        return itemId;
    }
}
