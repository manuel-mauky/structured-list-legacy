package eu.lestard.structuredlist.features.config.actions;

import eu.lestard.fluxfx.Action;
import eu.lestard.structuredlist.features.config.ConfigOption;

public class ChangeConfigAction<T> implements Action{

	private final ConfigOption<T> optionToChange;
	private final T newValue;

	public ChangeConfigAction(ConfigOption<T> optionToChange, T newValue) {
		this.optionToChange = optionToChange;
		this.newValue = newValue;
	}

	public ConfigOption<T> getOptionToChange() {
		return optionToChange;
	}

	public T getNewValue() {
		return newValue;
	}
}
