package data;

/**
 * modella una colonna di attributi continui, con minimo e massimo che un
 * singolo attributo puo assumere.
 */

class ContinuousAttribute extends Attribute {

	private double max;
	private double min;

	private static final double accuracy = 0.01;

	/**
	 * Inizializza i valori dei membri name, index,min,max.
	 * 
	 * @param name  nome dell'attributo.
	 * @param index identificativo numerico dell'attributo.
	 * @param min   valore minimo che puo assumere.
	 * @param max   valore massimo che puo assumere.
	 */
	ContinuousAttribute(final String name, final int index, final double min, final double max) {
		super(name, index); // chiama il costruttore della classe madre
		this.max = max;
		this.min = min;
	}

	/**
	 * normalizza il valore passato come parametro.
	 * 
	 * @param v valore da scalare
	 * @return il valore scalato
	 */
	double getScaledValue(final double v) {
		return (v - min) / (max - min);
	}

	@Override
	public boolean equals(final Object o) {
		ContinuousAttribute ca = (ContinuousAttribute) o;
		return (ca.min - min < accuracy) && (ca.max - max < accuracy) && ca.getName().equals(getName())
				&& ca.getIndex() == getIndex();
	}
}
