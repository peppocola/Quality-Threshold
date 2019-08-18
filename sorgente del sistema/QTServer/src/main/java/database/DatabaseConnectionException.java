package database;

/**
 * eccezione lanciata in caso di fallimento della connessione.
 */

public class DatabaseConnectionException extends Exception {
	public DatabaseConnectionException(final String s) {
		super(s);
	}
}
