package eu.lestard.structuredlist.features.config;

import eu.lestard.structuredlist.features.config.actions.ChangeConfigAction;
import io.vavr.Tuple2;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ConfigStoreTest {

	private ConfigStore store;

	private ConfigOption<Integer> editableIntegerOption = new ConfigOption<>(
		"test.int",
			5,
			"for testing purposes only",
			true
	);

	private ConfigOption<Integer> readonlyIntegerOption = new ConfigOption<>(
		"test.readonlyint",
			1,
			"for testing purposes only",
			false
	);

	@Before
	public void setup() {
		store = new ConfigStore();
	}

	@Test
	public void testChangeOption() {
		assertThat(store.getOptionValue(editableIntegerOption)).isNull();
		assertThat(store.getOptionValue(readonlyIntegerOption)).isNull();


		store.registerOption(editableIntegerOption);
		store.registerOption(readonlyIntegerOption);


		ObservableValue<Integer> editableOption = store.getOptionObservable(editableIntegerOption);
		ObservableValue<Integer> readonlyOption = store.getOptionObservable(readonlyIntegerOption);

		assertThat(editableOption.getValue()).isEqualTo(editableIntegerOption.getDefaultValue());
		assertThat(readonlyOption.getValue()).isEqualTo(readonlyIntegerOption.getDefaultValue());

		assertThat(store.getOptionValue(editableIntegerOption)).isEqualTo(editableIntegerOption.getDefaultValue());
		assertThat(store.getOptionValue(readonlyIntegerOption)).isEqualTo(readonlyIntegerOption.getDefaultValue());

		// when
		store.processChangeConfigAction(new ChangeConfigAction<>(editableIntegerOption, 12));

		// then
		assertThat(editableOption.getValue()).isEqualTo(12);
		assertThat(store.getOptionValue(editableIntegerOption)).isEqualTo(12);

		// when
		store.processChangeConfigAction(new ChangeConfigAction<>(readonlyIntegerOption, 46));

		// then
		assertThat(readonlyOption.getValue()).isEqualTo(readonlyIntegerOption.getDefaultValue());
		assertThat(store.getOptionValue(readonlyIntegerOption)).isEqualTo(readonlyIntegerOption.getDefaultValue());
	}

	@Test
	public void testOptionsList() {
		ObservableList<Tuple2<ConfigOption<?>, ?>> optionList = store.getOptionsList();
		assertThat(optionList).isEmpty();

		store.registerOption(editableIntegerOption);

		assertThat(optionList).hasSize(1);
		Tuple2<ConfigOption<?>, ?> optionTuple1 = optionList.get(0);
		assertThat(optionTuple1._1).isEqualTo(editableIntegerOption);
		assertThat(optionTuple1._2).isEqualTo(editableIntegerOption.getDefaultValue());


		store.registerOption(readonlyIntegerOption);

		assertThat(optionList).hasSize(2);
		Tuple2<ConfigOption<?>, ?> optionTuple2 = optionList.get(1);
		assertThat(optionTuple2._1).isEqualTo(readonlyIntegerOption);
		assertThat(optionTuple2._2).isEqualTo(readonlyIntegerOption.getDefaultValue());


		store.processChangeConfigAction(new ChangeConfigAction<>(editableIntegerOption, 59));

		optionTuple1 = optionList.get(0);
		assertThat(optionTuple1._1).isEqualTo(editableIntegerOption);
		assertThat(optionTuple1._2).isEqualTo(59);
	}

	@Test
	public void testRegisterOption() {
		ObservableValue<Integer> option1 = store.getOptionObservable(editableIntegerOption);
		assertThat(option1.getValue()).isNull();


		store.registerOption(editableIntegerOption);
		assertThat(option1.getValue()).isEqualTo(editableIntegerOption.getDefaultValue());

		store.registerOption(readonlyIntegerOption);

		ObservableValue<Integer> option2 = store.getOptionObservable(readonlyIntegerOption);
		assertThat(option2.getValue()).isEqualTo(readonlyIntegerOption.getDefaultValue());
	}


}
