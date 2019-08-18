package data;

/**
 * modella una cella della tabella contenente un valore continuo.
 */

class ContinuousItem extends Item {

	/**
	 * Inizializza i valori dei membri attribute e value.
	 * 
	 * @param attribute Attributo componente dell' item
	 * @param value     Valore dell' item
	 */
	ContinuousItem(final ContinuousAttribute attribute, final double value) {
		super(attribute, value);
	}

	/**
	 * Calcola la distanza tra due ContinuousItem.
	 * 
	 * @param a ContinuousItem da cui calcola la distanza
	 * @return la distanza
	 */
	@Override
	double distance(final Object a) {
		return Math.abs(((ContinuousAttribute) getAttribute()).getScaledValue((double) getValue())
				- ((ContinuousAttribute) ((Item) a).getAttribute())
						.getScaledValue((double) ((Item) a).getValue()));

	}

	@Override
	public boolean equals(final Object o) {
		return ((ContinuousItem) o).getAttribute().equals(getAttribute())
				&& ((ContinuousItem) o).getValue().equals(getValue());
	}
}
