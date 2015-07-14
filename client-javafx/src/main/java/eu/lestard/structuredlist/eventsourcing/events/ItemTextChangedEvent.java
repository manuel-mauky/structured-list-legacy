package eu.lestard.structuredlist.eventsourcing.events;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import eu.lestard.structuredlist.eventsourcing.Event;

/**
 * @author manuel.mauky
 */
public class ItemTextChangedEvent extends Event {

	private final String itemId;

	private final String text;

	@JsonCreator
	public ItemTextChangedEvent(@JsonProperty("itemId") String itemId, @JsonProperty("text")  String text) {
		this.itemId = itemId;
		this.text = text;
	}

	public String getText() {
		return text;
	}

	public String getItemId() {
		return itemId;
	}
}
