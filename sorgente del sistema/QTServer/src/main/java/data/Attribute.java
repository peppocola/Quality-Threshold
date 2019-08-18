package data;

import java.io.Serializable;

/**
 * Modella una colonna della tabella del Database.
 */

abstract class Attribute implements Serializable {

	private String name;
	private int index;

	/**
	 * Inizializza i valori dei membri name, index.
	 * 
	 * @param name  nome dell'attributo.
	 * @param index identificativo numerico dell'attributo.
	 */
	Attribute(final String name, final int index) { // la classe � astratta pertanto non pu� essere public
		this.name = name;
		this.index = index;
	}

	/**
	 * @return nome dell'attributo
	 */
	String getName() {
		return name; // oppure this.name
	}

	/**
	 *
	 * @return identificativo numerico dell'attributo.
	 */
	int getIndex() {
		return index;
	}

	@Override
	public String toString() {
		return name;
	}
}
