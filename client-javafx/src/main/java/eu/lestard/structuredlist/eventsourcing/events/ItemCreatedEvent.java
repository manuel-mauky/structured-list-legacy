package eu.lestard.structuredlist.eventsourcing.events;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import eu.lestard.structuredlist.eventsourcing.Event;

public class ItemCreatedEvent extends Event {

    private final String parentId;

    private final String itemId;

    private final String text;

    @JsonCreator
    public ItemCreatedEvent(@JsonProperty("parentId") String parentId, @JsonProperty("itemId") String itemId, @JsonProperty("text") String text) {
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
