package eu.lestard.structuredlist.eventsourcing.events;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import eu.lestard.structuredlist.eventsourcing.Event;

public class ItemMovedEvent extends Event {

	private final String itemId;
	private final String newParentId;

	@JsonCreator
	public ItemMovedEvent(@JsonProperty("itemId") String itemId, @JsonProperty("newParentId") String newParentId) {
		this.itemId = itemId;
		this.newParentId = newParentId;
	}

	public String getItemId() {
		return itemId;
	}

	public String getNewParentId() {
		return newParentId;
	}
}
