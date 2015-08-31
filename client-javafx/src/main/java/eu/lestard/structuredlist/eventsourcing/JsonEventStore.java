package eu.lestard.structuredlist.eventsourcing;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Singleton;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JSR310Module;

@Singleton
public class JsonEventStore implements EventStore {

    private File file;
    private ObjectMapper mapper = new ObjectMapper();


    public JsonEventStore(File file) {
        this.file = file;

//        mapper.enable(SerializationFeature.INDENT_OUTPUT);
		mapper.enable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

		mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
		mapper.registerModule(new JSR310Module());
    }

    @Override
    public void push(Event event) {
		try {
			final String eventJson = mapper.writeValueAsString(event);

			Files.write(file.toPath(), Collections.singletonList(eventJson), StandardOpenOption.APPEND);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

    @Override
    public List<Event> getEvents() {
		try {
			final List<String> lines = Files.readAllLines(file.toPath());

			return lines.stream()
					.map(line -> {
						try {
							return mapper.readValue(line, Event.class);
						} catch (IOException e) {
							throw new RuntimeException(e);
						}
					})
					.collect(Collectors.toList());


		} catch (Exception e) {
			e.printStackTrace();
		}


		return Collections.emptyList();
    }
}
