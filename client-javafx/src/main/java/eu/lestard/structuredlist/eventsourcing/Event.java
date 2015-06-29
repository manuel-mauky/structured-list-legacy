package eu.lestard.structuredlist.eventsourcing;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public abstract class Event implements Comparable<Event> {

    private final String id;

    private final LocalDateTime timeStamp;

    public Event() {
        id = UUID.randomUUID().toString();
        timeStamp = LocalDateTime.now();
    }

    private Event(String id, LocalDateTime timeStamp) {
        this.id = id;
        this.timeStamp = timeStamp;
    }

    public String getId() {
        return id;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return Objects.equals(id, event.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }


    @Override
    public int compareTo(Event o) {
        return this.timeStamp.compareTo(o.getTimeStamp());
    }
}
