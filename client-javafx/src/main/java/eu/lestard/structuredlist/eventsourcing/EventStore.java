package eu.lestard.structuredlist.eventsourcing;

import java.util.List;

public interface EventStore {

    void push(Event event);

    List<Event> getEvents();

}
