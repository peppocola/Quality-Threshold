package data;

import java.util.Arrays;
import java.util.Iterator;
import java.util.TreeSet;

/**
 * modella una colonna di attributi discreti, con dei valori possibili.
 */

class DiscreteAttribute extends Attribute implements Iterable<String> {

	// attributi d'istanza

	private TreeSet<String> values = new TreeSet<String>();

	// metodi
	/**
	 * Costruisce un DiscreteAttribute.
	 * 
	 * @param name   nome dell' attributo
	 * @param index  indice dell' attributo
	 * @param values possibili valori dell' attributo
	 */
	DiscreteAttribute(final String name, final int index, final String[] values) {
		super(name, index);
		this.values.addAll(Arrays.asList(values));
	}

	@Override
	public Iterator<String> iterator() {
		return values.iterator();
	}

	@Override
	public boolean equals(final Object o) {
		DiscreteAttribute da = (DiscreteAttribute) o;
		return da.getName().equals(getName()) && da.getIndex() == getIndex() && da.values.equals(values);
	}

	int getNumberOfDistinctValues() {
		return values.size();
	}
}
