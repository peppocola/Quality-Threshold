package mining;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import data.Data;

/**
 * memorizza tutti i cluster creati.
 */

public class ClusterSet implements Iterable<Cluster>, Serializable {

	private Set<Cluster> C = new TreeSet<Cluster>();

	/**
	 * aggiunge un cluster al set.
	 * 
	 * @param c Cluster da aggiungere.
	 */
	void add(final Cluster c) {
		C.add(c);
	}

	@Override
	public Iterator<Cluster> iterator() {
		return C.iterator();
	}

	@Override
	public boolean equals(Object o) {

		if (C.size() != ((ClusterSet) o).C.size())
			return false;

		Iterator<Cluster> i = C.iterator();
		Iterator<Cluster> j = ((ClusterSet) o).C.iterator();
		boolean equals = true;
		while (i.hasNext() && equals == true) {
			equals = ((Cluster) i.next()).equals((Cluster) j.next());
		}

		return equals;
	}

	@Override
	public String toString() {
		String str = "";
		int i = 1;
		for (final Cluster c : C) {
			str += i + ":Centroid=(" + c.getCentroid() + ")\n";
			i++;
		}
		return str;
	}

	public String toString(final Data data) {
		String str = "";
		int i = 0;
		for (final Cluster c : C) {
			str += i + 1 + ":";
			str += c.toString(data) + "\n";
			i++;
			str += "\n";
		}
		return str;
	}
}
