package eu.lestard.structuredlist.eventsourcing;

import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class InMemoryEventStore implements EventStore {

    private List<Event> events = new ArrayList<>();

    @Override
    public void push(Event event) {
        events.add(event);
    }

    @Override
    public List<Event> getEvents() {
        return events;
    }

}
