package database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import database.TableSchema.Column;

/**
 * Recupera i dati dal database.
 *
 */
public class TableData {

	private DbAccess db;

	/**
	 * Inizializza db.
	 *
	 * @param db gestore accesso al DB
	 */
	public TableData(final DbAccess db) {
		this.db = db;
	}

	/**
	 * popola una List di Example con le tuple della tabella del database.
	 *
	 * @param table nome della tabella nel database.
	 * @return Lista di transazioni distinte memorizzate nella tabella.
	 * @throws SQLException      se il numero di colonne della tabella è 0.
	 * @throws EmptySetException se la tabella è vuota.
	 */
	public List<Example> getDistinctTransazioni(final String table) throws SQLException, EmptySetException {
		final LinkedList<Example> transSet = new LinkedList<Example>();
		Statement statement;
		final TableSchema tSchema = new TableSchema(db, table);
		String query = "select distinct ";

		for (int i = 0; i < tSchema.getNumberOfAttributes(); i++) {
			final Column c = tSchema.getColumn(i);
			if (i > 0) {
				query += ",";
			}
			query += c.getColumnName();
		}
		if (tSchema.getNumberOfAttributes() == 0) {
			throw new SQLException();
		}
		query += " FROM " + table;

		statement = db.getConnection().createStatement();
		final ResultSet rs = statement.executeQuery(query);
		boolean empty = true;

		while (rs.next()) {
			empty = false;
			final Example currentTuple = new Example();
			for (int i = 0; i < tSchema.getNumberOfAttributes(); i++) {
				if (tSchema.getColumn(i).isNumber()) {
					currentTuple.add(rs.getDouble(i + 1));
				} else {
					currentTuple.add(rs.getString(i + 1));
				}
			}
			transSet.add(currentTuple);
		}
		rs.close();
		statement.close();
		if (empty) {
			throw new EmptySetException("il set è vuoto");
		}
		return transSet;
	}

	/**
	 * effettua una query su una colonna restituendone tutti i valori assunti
	 * ordinati e distinti.
	 *
	 * @param table  nome della tabella.
	 * @param column nome della colonna.
	 * @return insieme ordinato dei valori presenti in una singola colonna della
	 *         tabella.
	 * @throws SQLException problemi con il database.
	 */
	public Set<Object> getDistinctColumnValues(final String table, final Column column) throws SQLException {
		final Set<Object> valueSet = new TreeSet<Object>();
		Statement statement;
		String query = "select distinct ";
		query += column.getColumnName();
		query += " FROM " + table;
		query += " ORDER BY " + column.getColumnName();
		statement = db.getConnection().createStatement();
		final ResultSet rs = statement.executeQuery(query);

		while (rs.next()) {
			if (column.isNumber()) {
				valueSet.add(rs.getDouble(1));
			} else {
				valueSet.add(rs.getString(1));
			}
		}
		rs.close();
		statement.close();
		return valueSet;
	}

	/**
	 * effettua una query con operazione di aggregazione.
	 *
	 * @param table     tabella in cui cercare.
	 * @param column    colonna su cui cercare il valore aggregato.
	 * @param aggregate può essere MAX o MIN.
	 * @return il massimo o il minimo in una colonna (se presente).
	 * @throws SQLException     problemi col database.
	 * @throws NoValueException se il valore è nullo.
	 */
	public Object getAggregateColumnValue(final String table, final Column column, final QUERY_TYPE aggregate)
			throws SQLException, NoValueException {
		Statement statement;
		Object value = null;
		String aggregateOp = "";
		String query = "select ";

		if (aggregate == QUERY_TYPE.MAX) {
			aggregateOp += "max";
		} else {
			aggregateOp += "min";
		}

		query += aggregateOp + "(" + column.getColumnName() + ") FROM " + table;
		statement = db.getConnection().createStatement();
		final ResultSet rs = statement.executeQuery(query);

		if (rs.next()) {
			if (column.isNumber()) {
				value = rs.getFloat(1);
			} else {
				value = rs.getString(1);
			}
		}
		rs.close();
		statement.close();
		if (value == null) {
			throw new NoValueException("No " + aggregateOp + " on " + column.getColumnName());
		}
		return value;
	}
}
