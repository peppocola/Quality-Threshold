package data;

/**
 * modella una cella della tabella contenente un valore discreto.
 */

class DiscreteItem extends Item {
	/**
	 * Costruisce un DiscreteItem.
	 * 
	 * @param attribute attributo dell' item
	 * @param value     valore dell' item
	 */
	DiscreteItem(final DiscreteAttribute attribute, final String value) {
		super(attribute, value);
	}

	@Override
	double distance(final Object a) {
		if (getValue().equals(((Item) a).getValue())) {
			return 0.0;
		} else {
			return 1.0;
		}
	}

	@Override
	public boolean equals(final Object o) {
		return ((DiscreteItem) o).getAttribute().equals(getAttribute())
				&& ((DiscreteItem) o).getValue().equals(getValue());
	}
}
