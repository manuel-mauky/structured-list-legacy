package eu.lestard.structuredlist.features.items;

import eu.lestard.structuredlist.eventsourcing.Event;
import eu.lestard.structuredlist.eventsourcing.EventStore;
import eu.lestard.structuredlist.eventsourcing.events.ItemCompletedEvent;
import eu.lestard.structuredlist.eventsourcing.events.ItemCreatedEvent;
import eu.lestard.structuredlist.eventsourcing.events.ItemRemovedEvent;
import eu.lestard.structuredlist.eventsourcing.events.ItemTextChangedEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class RootItemFactory {
    public static final String ROOT_ID = "rootId";

	private List<Consumer<Double>> progressConsumer = new ArrayList<>();

    private final EventStore eventStore;

    public RootItemFactory(EventStore eventStore) {
        this.eventStore = eventStore;
    }
	
	public Item createRootItem() {
		Item rootItem = new Item(ROOT_ID, "root");

		final List<Event> events = eventStore.getEvents();

		int i = 0;
		int size = events.size();

		publishProgressChanged(i, size);

		for (Event event : events) {
			if(event instanceof ItemCreatedEvent){
				processItemCreatedEvent(rootItem, (ItemCreatedEvent)event);
			}

			if(event instanceof ItemRemovedEvent) {
				processItemRemovedEvent(rootItem, (ItemRemovedEvent) event);
			}

			if(event instanceof ItemTextChangedEvent) {
				processItemTextChangedEvent(rootItem, (ItemTextChangedEvent) event);
			}
			
			if(event instanceof ItemCompletedEvent) {
				processItemCompletedEvent(rootItem, (ItemCompletedEvent) event);
			}

			i++;

			publishProgressChanged(i, size);
		}

		return rootItem;
	}

	private void processItemCompletedEvent(Item rootItem, ItemCompletedEvent event) {
		String itemId = event.getItemId();
		
		rootItem.findRecursive(itemId)
				.ifPresent(item -> item.setCompleted(true));
	}

	private void publishProgressChanged(int current, int all) {
		double progress = 0.0;
		if(all > 0) {
			progress = (double) current * 100 / (double) all;
		}

		final double tmp = progress / 100;
		
		progressConsumer.forEach(consumer -> consumer.accept(tmp));
	}
	
	public void onProgressChanged(Consumer<Double> listener) {
		progressConsumer.add(listener);
	}
	
	private void processItemTextChangedEvent(Item rootItem, ItemTextChangedEvent event) {
		final String itemId = event.getItemId();
		final String text = event.getText();
		
		rootItem.findRecursive(itemId)
				.ifPresent(item -> item.setText(text));
		
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
