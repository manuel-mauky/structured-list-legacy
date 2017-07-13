package eu.lestard.structuredlist.features.config;

import eu.lestard.fluxfx.Store;
import eu.lestard.structuredlist.features.config.actions.ChangeConfigAction;
import io.vavr.Tuple2;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;

import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Singleton
public class ConfigStore extends Store {

	/**
	 * A list of config options that are handled by this store.
	 */
	private List<ConfigOption<?>> registeredOptions = new ArrayList<>();

	private ObservableList<Tuple2<ConfigOption<?>,?>> observableOptionsList = FXCollections.observableArrayList();
	private SortedList<Tuple2<ConfigOption<?>, ?>> sortedOptionsList = new SortedList<>(
			observableOptionsList, Comparator.comparing(o -> o._1().getPath()));

	public ConfigStore() {
		subscribe(ChangeConfigAction.class, this::processChangeConfigAction);
	}

	public void registerDefaultOptions() {
		registerOption(ConfigOptions.appDir);
		registerOption(ConfigOptions.storageFileName);
	}

	<T> void processChangeConfigAction(ChangeConfigAction<T> action) {
		if(action.getOptionToChange().isEdiable()) {
			if(registeredOptions.contains(action.getOptionToChange())) {

				Tuple2 newOptionTuple = new Tuple2<>(action.getOptionToChange(), action.getNewValue());

				Optional<Tuple2<ConfigOption<?>, ?>> optional = observableOptionsList.stream()
						.filter(entry -> entry._1().equals(action.getOptionToChange()))
						.findAny();

				optional.ifPresent(configOptionTuple2 -> observableOptionsList.remove(configOptionTuple2));

				observableOptionsList.add(newOptionTuple);
			}
		}
	}


	<T> void registerOption(ConfigOption<T> option) {
		registeredOptions.add(option);
		observableOptionsList.add(new Tuple2<>(option, option.getDefaultValue()));
	}

	public SortedList<Tuple2<ConfigOption<?>, ?>> getOptionsList() {
		return sortedOptionsList;
	}

	@SuppressWarnings("unchecked")
	public <T> ObservableValue<T> getOptionObservable(ConfigOption<T> option) {
		return Bindings.createObjectBinding(() -> {

			Optional<Tuple2<ConfigOption<?>, ?>> registeredOption = observableOptionsList.stream()
					.filter(entry -> entry._1().equals(option))
					.findAny();

			return registeredOption
					.map(tuple -> (T) tuple._2())
					.orElse(null);
		}, observableOptionsList);
	}

	@SuppressWarnings("unchecked")
	public <T> T getOptionValue(ConfigOption<T> option) {
		return (T) observableOptionsList.stream()
				.filter(entry -> entry._1().equals(option))
				.findFirst()
				.map(Tuple2::_2)
				.orElse(null);
	}
}
