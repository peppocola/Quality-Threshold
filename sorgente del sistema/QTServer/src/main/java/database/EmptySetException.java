package database;

/**
 * eccezione lanciata in caso di ResultSet vuoto.
 */
public class EmptySetException extends Exception {
	public EmptySetException(final String s) {
		super(s);
	}
}
