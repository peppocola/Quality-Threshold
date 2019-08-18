package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * gestisce la connessione al database.
 */

public class DbAccess {

	/**
	 * (Per utilizzare questo Driver scaricare e aggiungere al classpath il
	 * connettore mysql connector).
	 */
	private static final String DRIVER_CLASS_NAME = "com.mysql.cj.jdbc.Driver"; // org.gjt.mm.mysql.Driver";
	/**
	 *
	 */
	private static final String DBMS = "jdbc:mysql";
	/**
	 * contiene l'identificativo del server su cui risiede la base di dati (per
	 * esempio localhost).
	 */
	private static final String SERVER = "localhost";
	/**
	 * contiene il nome della base di dati.
	 */
	private static final String DATABASE = "MapDB";
	/**
	 * La porta su cui il DBMS MySQL accetta le connessioni.
	 */
	private static final String PORT = "3306";
	/**
	 * contiene il nome dell' utente per l'accesso alla base di dati.
	 */
	private static final String USER_ID = "MapUser";
	/**
	 * contiene la password di autenticazione per l' utente identificato da USER_ID.
	 */
	private static final String PASSWORD = "map";
	/**
	 * gestisce una connessione.
	 */
	private Connection conn;

	/**
	 * Impartisce al class loader l' ordine di caricare il driver mysql, inizializza
	 * la connessione riferita da conn. Il metodo solleva e propaga una eccezione di
	 * tipo DatabaseConnectionException in caso di fallimento nella connessione al
	 * database.
	 *
	 * @throws DatabaseConnectionException problemi di connessione al database
	 */
	public void initConnection() throws DatabaseConnectionException {
		try {
			Class.forName(DRIVER_CLASS_NAME).newInstance();
		} catch (final ClassNotFoundException e) {
			e.printStackTrace();
		} catch (final IllegalAccessException e) {
			e.printStackTrace();
		} catch (final InstantiationException e) {
			e.printStackTrace();
		}
		try {
			conn = DriverManager.getConnection(DBMS + "://" + SERVER + ":" + PORT + "/" + DATABASE
					+ "?user=" + USER_ID + "&password=" + PASSWORD + "&serverTimezone=UTC");
		} catch (final SQLException e) {
			throw new DatabaseConnectionException("connessione fallita");
		}
	}

	/**
	 * Restituisce la connessione.
	 *
	 * @return conn
	 */
	public Connection getConnection() {
		return conn;
	}

	/**
	 * chiude la connessione conn.
	 */
	public void closeConnection() {
		try {
			conn.close();
		} catch (final SQLException e) {
			e.printStackTrace();
		}
	}
}
