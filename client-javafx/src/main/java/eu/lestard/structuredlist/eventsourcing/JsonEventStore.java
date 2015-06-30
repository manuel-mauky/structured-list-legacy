package eu.lestard.structuredlist.eventsourcing;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JSR310Module;

import javax.inject.Singleton;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class JsonEventStore implements EventStore {

    private File file;
    private ObjectMapper mapper = new ObjectMapper();
    private List<Event> events = new ArrayList<>();


    public JsonEventStore(File file) {
        this.file = file;

        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        mapper.enable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        mapper.enableDefaultTyping();
        mapper.registerModule(new JSR310Module());

        if(file.length() == 0) {
            events = new ArrayList<>();
        } else {
            try {
                JavaType listType = mapper.getTypeFactory().constructCollectionType(ArrayList.class, Event.class);

                events = mapper.readValue(file, listType);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void push(Event event) {
        events.add(event);

        try {
            mapper.writeValue(file, events);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Event> getEvents() {
        return events;
    }
}
