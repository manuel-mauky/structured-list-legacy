package eu.lestard.structuredlist.features.config.custom_config_types;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PercentageTest {

	@Test
	public void testValueAsDouble() {
		assertThat(Percentage.fromDouble(-4.2).getAsDouble()).isEqualTo(0);
		assertThat(Percentage.fromDouble(0.0).getAsDouble()).isEqualTo(0);
		assertThat(Percentage.fromDouble(0.1).getAsDouble()).isEqualTo(0.1);
		assertThat(Percentage.fromDouble(0.5).getAsDouble()).isEqualTo(0.5);
		assertThat(Percentage.fromDouble(1.0).getAsDouble()).isEqualTo(1.0);
		assertThat(Percentage.fromDouble(1.0001).getAsDouble()).isEqualTo(1.0);
		assertThat(Percentage.fromDouble(1.3).getAsDouble()).isEqualTo(1.0);
		assertThat(Percentage.fromDouble(430.2).getAsDouble()).isEqualTo(1.0);

		Percentage p1 = Percentage.fromDouble(0.4);
		assertThat(p1.getAsInteger()).isEqualTo(40);

		Percentage p2 = Percentage.fromDouble(0.0);
		assertThat(p2.getAsInteger()).isEqualTo(0);

		Percentage p3 = Percentage.fromDouble(1.0);
		assertThat(p3.getAsInteger()).isEqualTo(100);

		Percentage p4 = Percentage.fromDouble(0.1234);
		assertThat(p4.getAsInteger()).isEqualTo(12);

		Percentage p5 = Percentage.fromDouble(0.1299);
		assertThat(p5.getAsInteger()).isEqualTo(12);
	}

	@Test
	public void testValuesAsInteger() {
		assertThat(Percentage.fromInt(-12).getAsInteger()).isEqualTo(0);
		assertThat(Percentage.fromInt(0).getAsInteger()).isEqualTo(0);
		assertThat(Percentage.fromInt(12).getAsInteger()).isEqualTo(12);
		assertThat(Percentage.fromInt(100).getAsInteger()).isEqualTo(100);
		assertThat(Percentage.fromInt(104).getAsInteger()).isEqualTo(100);

		Percentage p1 = Percentage.fromInt(30);
		assertThat(p1.getAsDouble()).isEqualTo(0.3);

		Percentage p2 = Percentage.fromInt(0);
		assertThat(p2.getAsDouble()).isEqualTo(0.0);

		Percentage p3 = Percentage.fromInt(100);
		assertThat(p3.getAsDouble()).isEqualTo(1.0);

	}

}
