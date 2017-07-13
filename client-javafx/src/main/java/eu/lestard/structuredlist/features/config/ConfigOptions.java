package eu.lestard.structuredlist.features.config;

import java.nio.file.Path;
import java.nio.file.Paths;

public class ConfigOptions {

	public static final ConfigOption<Path> appDir = new ConfigOption<>(
			"app.appDir",
			Paths.get(System.getProperty("user.home"), ".structure-list"),
			"The app directory where the application can store configuration and other files.",
			false
	);

	public static final ConfigOption<String> storageFileName = new ConfigOption<>(
			"eventsourcing.storageFileName",
			"storage.json",
			"The filename of the file that is used to persist the items data",
			false
	);




}
