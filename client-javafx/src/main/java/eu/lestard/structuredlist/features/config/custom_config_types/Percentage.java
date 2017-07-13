package eu.lestard.structuredlist.features.config.custom_config_types;

public class Percentage {

	private final double value;

	/**
	 * Creates a percentage value object with the given value.
	 * <p/>
	 * The int value has to be in the range between 0 and 100.
	 * Values smaller then 0 are interpreted as 0.
	 * Values bigger then 100 are interpreted as 100.
	 */
	public static Percentage fromInt(int value) {
		int tmp = Integer.max(value, 0);

		tmp = Integer.min(tmp, 100);

		return new Percentage((double)tmp / 100.0);
	}
	/**
	 * Creates a percentage value object with the given value.
	 * <p/>
	 * The int value has to be in the range between 0.0 and 1.0.
	 * Values smaller then 0.0 are interpreted as 0.0.
	 * Values bigger then 1.0 are interpreted as 1.0.
	 */
	public static Percentage fromDouble(double value) {
		double tmp = Double.max(value, 0);
		tmp = Double.min(tmp, 1.0);

		return new Percentage(tmp);
	}

	private Percentage(double value) {
		this.value = value;
	}

	/**
	 * @return the percentage value as double between <code>0.0</code> and <code>1.0</code>.
	 */
	public double getAsDouble() {
		return value;
	}

	/**
	 * @return the percentage value as int between <code>0</code> and <code>100</code>
	 */
	public int getAsInteger() {
		return (int) (value * 100);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		Percentage that = (Percentage) o;

		return Double.compare(that.value, value) == 0;
	}

	@Override
	public int hashCode() {
		long temp = Double.doubleToLongBits(value);
		return (int) (temp ^ (temp >>> 32));
	}
}
