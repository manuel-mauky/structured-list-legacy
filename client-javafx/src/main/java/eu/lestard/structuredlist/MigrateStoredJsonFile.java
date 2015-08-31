package eu.lestard.structuredlist;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JSR310Module;
import eu.lestard.structuredlist.eventsourcing.Event;
import eu.lestard.structuredlist.eventsourcing.JsonEventStore;

import java.io.File;
import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class MigrateStoredJsonFile {


	public static void main(String... args) throws IOException {

		
		

		final Optional<File> oldStorageFileOptional = App.getStorageFile();
		
		if(!oldStorageFileOptional.isPresent()) {
			System.out.println("Storage file not found");
			return;
		}

		final File oldStorageFile = oldStorageFileOptional.get();
		
		System.out.println(
				"The old storage file will now be replaced with the new format. A backup of the old file will be created.");

		ObjectMapper oldMapper = new ObjectMapper();

		oldMapper.enable(SerializationFeature.INDENT_OUTPUT);
		oldMapper.enable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

		oldMapper.enableDefaultTyping();
		oldMapper.registerModule(new JSR310Module());


		JavaType listType = oldMapper.getTypeFactory().constructCollectionType(ArrayList.class, Event.class);

		List<Event> events = oldMapper.readValue(oldStorageFile, listType);

		System.out.println("Found " + events.size() + " events");


		File backupFile = oldStorageFile.toPath().resolveSibling("storage.json.bak").toFile();
		backupFile.createNewFile();

		try {
			Files.copy(oldStorageFile.toPath(), backupFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			System.err.println("Could not create backup file [" + backupFile.toPath().toAbsolutePath() + "]. Aborting migration.");
			e.printStackTrace();
		}

		System.out.println("Backup was created: [" + backupFile.toPath().toAbsolutePath() + "].");
		
		oldStorageFile.delete();
		oldStorageFile.createNewFile();
		
		JsonEventStore store = new JsonEventStore(oldStorageFile);
		
		events.forEach(store::push);


		final List<Event> newEvents = store.getEvents();

		if(events.equals(newEvents)) {
			System.out.println("Migration was successful");
		} else {
			System.out.println("There was an error during migration.");
		}
		
	}
}
