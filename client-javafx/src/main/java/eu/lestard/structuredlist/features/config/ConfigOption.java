package eu.lestard.structuredlist.features.config;

import java.util.Objects;

public class ConfigOption<T> {
	private String path;
	private T defaultValue;
	private String description;
	private boolean ediable;

	public ConfigOption(String path, T defaultValue, String description, boolean ediable) {
		this.path = Objects.requireNonNull(path);
		this.defaultValue = defaultValue;
		this.description = description;
		this.ediable = ediable;
	}

	public String getPath() {
		return path;
	}

	public T getDefaultValue() {
		return defaultValue;
	}

	public String getDescription() {
		return description;
	}

	public boolean isEdiable() {
		return ediable;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		ConfigOption<?> that = (ConfigOption<?>) o;

		return path.equals(that.path);
	}

	@Override
	public int hashCode() {
		return path.hashCode();
	}
}
