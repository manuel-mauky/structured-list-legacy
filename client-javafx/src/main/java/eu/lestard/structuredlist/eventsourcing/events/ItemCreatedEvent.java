package eu.lestard.structuredlist.eventsourcing.events;

import eu.lestard.structuredlist.eventsourcing.Event;

public class ItemCreatedEvent extends Event {

    private final String parentId;

    private final String itemId;

    private final String text;

    public ItemCreatedEvent(String parentId, String itemId, String text) {
        this.parentId = parentId;
        this.itemId = itemId;
        this.text = text;
    }

    public String getParentId() {
        return parentId;
    }

    public String getItemId() {
        return itemId;
    }

    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        return "ItemCreated " + text;
    }
}
