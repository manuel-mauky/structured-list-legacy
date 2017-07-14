package eu.lestard.structuredlist.features.items;

import eu.lestard.fluxfx.Store;
import eu.lestard.structuredlist.eventsourcing.events.ItemMovedEvent;
import eu.lestard.structuredlist.features.items.actions.CompleteItemAction;
import eu.lestard.structuredlist.features.items.actions.EditItemAction;
import eu.lestard.structuredlist.features.items.actions.MoveItemAction;
import eu.lestard.structuredlist.features.items.actions.NewItemAction;
import eu.lestard.structuredlist.features.items.actions.RemoveItemAction;
import eu.lestard.structuredlist.eventsourcing.EventStore;
import eu.lestard.structuredlist.eventsourcing.events.ItemCompletedEvent;
import eu.lestard.structuredlist.eventsourcing.events.ItemCreatedEvent;
import eu.lestard.structuredlist.eventsourcing.events.ItemRemovedEvent;
import eu.lestard.structuredlist.eventsourcing.events.ItemTextChangedEvent;

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
		subscribe(EditItemAction.class, this::processEditItem);
		subscribe(CompleteItemAction.class, this::processCompleteItem);
		subscribe(MoveItemAction.class, this::processMoveItem);
    }

    private void processMoveItem(MoveItemAction action) {
		String itemId = action.getItemId();
		String newParentId = action.getNewParentId();

		if(itemId.equals(newParentId)) {
			return;
		}

		root.findRecursive(itemId).ifPresent(item -> {
			root.findRecursive(newParentId).ifPresent(newParent -> {

				if(canBeMoved(item, newParentId)) {
					newParent.addSubItem(item);

					eventStore.push(new ItemMovedEvent(itemId, newParentId));
				}
			});
		});
	}

	public boolean canBeMoved(Item item, String newParentId) {
    	if(item.getId().equals(newParentId)) {
    		return false;
		}

		// check whether the new parent is already set as the current parent.
		if(item.getParent().map(parent -> parent.getId().equals(newParentId)).orElse(false)) {
    		return false;
		}

		return !item.findRecursive(newParentId)
				.isPresent();
	}

	public boolean canBeMoved(String itemId, String newParentId) {
    	return root.findRecursive(itemId)
				.map(item -> canBeMoved(item, newParentId))
				.orElse(false);
	}

	private void processCompleteItem(CompleteItemAction action) {
		String itemId = action.getItemId();

		root.findRecursive(itemId).ifPresent(item -> {
			item.setCompleted(true);
			
			eventStore.push(new ItemCompletedEvent(itemId));
		});
	}

	void processEditItem(EditItemAction action) {
		final String itemId = action.getItemId();
		final String text = action.getNewText();
		
		root.findRecursive(itemId).ifPresent(item -> {
			item.setText(text);
			
			eventStore.push(new ItemTextChangedEvent(itemId, text));
		});

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
