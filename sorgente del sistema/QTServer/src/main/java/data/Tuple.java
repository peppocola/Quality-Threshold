package data;

import java.io.Serializable;
import java.util.Set;

/**
 * modella una tupla della tabella fornendo metodi per il confronto con altre
 * tuple.
 */

public class Tuple implements Serializable {

	private Item[] tuple; // array di Item

	/**
	 * Costruisce una tupla vuota di dimensione size.
	 * 
	 * @param size dimensione della tupla
	 */
	Tuple(final int size) {
		tuple = new Item[size];
	}

	public int getLength() {
		return tuple.length;
	}

	/**
	 * Restituisce l' item in posizione i.
	 * 
	 * @param i posizione dell item
	 * @return l'item
	 */
	public Item get(final int i) {
		return tuple[i];
	}

	/**
	 * Inserisce l'item c in posizione i.
	 * 
	 * @param c item da inserire
	 * @param i posizione
	 */
	void add(final Item c, final int i) {
		tuple[i] = c;
	}

	/**
	 * Restituisce la distanza tra la tupla corrente e obj. La distanza è ottenuta
	 * come la somma delle distanze tra gli item in posizioni eguali nelle due
	 * tuple.
	 * 
	 * @param obj tupla da cui calcolare la distanza
	 * @return la distanza
	 */
	public double getDistance(final Tuple obj) {
		double distance = 0.0;
		for (int i = 0; i < obj.getLength(); i++) {
			distance += get(i).distance(obj.get(i));
		}
		return distance;
	}

	/**
	 * Calcola la distanza media tra la tupla corrente e le tuple clusterizzate.
	 * 
	 * @param data          Tabella contenente tutte le tuple
	 * @param clusteredData insieme degli indici delle tuple clusterizzate
	 * @return la distanza media
	 */
	public double avgDistance(final Data data, final Set<Integer> clusteredData) {
		double p = 0.0, sumD = 0.0;
		for (final Integer i : clusteredData) {
			final double d = getDistance(data.getItemSet(i));
			sumD += d;
		}
		p = sumD / clusteredData.size();
		return p;
	}

	@Override
	public String toString() {
		String str = "";
		for (int i = 0; i < tuple.length - 1; i++) {
			str += tuple[i].getValue() + ", ";
		}
		str += tuple[tuple.length - 1].getValue();
		return str;
	}

	@Override
	public boolean equals(final Object o) {
		if (getLength() != ((Tuple) o).getLength()) {
			return false;
		}
		return ((Tuple) o).getDistance(this) == 0;
	}
}
