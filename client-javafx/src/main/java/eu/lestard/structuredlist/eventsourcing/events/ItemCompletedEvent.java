package eu.lestard.structuredlist.eventsourcing.events;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import eu.lestard.structuredlist.eventsourcing.Event;

public class ItemCompletedEvent extends Event {
	
	private final String itemId;

	@JsonCreator
	public ItemCompletedEvent(@JsonProperty("itemId") String itemId) {
		this.itemId = itemId;
	}

	public String getItemId() {
		return itemId;
	}
}
