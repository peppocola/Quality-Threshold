package data;

import java.io.Serializable;

/**
 * modella una cella della tabella contenente un valore (continuo o discreto).
 */

abstract class Item implements Serializable {

	private Attribute attribute;
	private Object value;

	/**
	 * Costruisce un Item.
	 * 
	 * @param attribute attributo dell' item
	 * @param value     valore dell' item
	 */
	Item(final Attribute attribute, final Object value) {

		this.attribute = attribute;
		this.value = value;
	}

	Attribute getAttribute() {

		return attribute;
	}

	Object getValue() {

		return value;
	}

	@Override
	public String toString() {

		return value.toString();
	}

	/**
	 * Calcola la distanza tra due Item.
	 * 
	 * @param a Item da cui calcola la distanza
	 * @return la distanza
	 */
	abstract double distance(Object a);
}
