package database;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * schema della tabella del Database, contiene una lista con nomi delle colonne
 * e tipi.
 */
public class TableSchema {

	private List<Column> tableSchema = new ArrayList<Column>();

	/**
	 * Modella una colonna della tabella. contiene il tipo del dato e il nome della
	 * colonna.
	 *
	 */
	public class Column {
		private String name;
		private String type;

		Column(final String name, final String type) {
			this.name = name;
			this.type = type;
		}

		public String getColumnName() {
			return name;
		}

		public boolean isNumber() {
			return type.equals("number");
		}

		@Override
		public String toString() {
			return name + ":" + type;
		}
	}


	public TableSchema(final DbAccess db, final String tableName) throws SQLException {
		final HashMap<String, String> mapSQL_JAVATypes = new HashMap<String, String>();
		// http://java.sun.com/j2se/1.3/docs/guide/jdbc/getstart/mapping.html
		mapSQL_JAVATypes.put("CHAR", "string");
		mapSQL_JAVATypes.put("VARCHAR", "string");
		mapSQL_JAVATypes.put("LONGVARCHAR", "string");
		mapSQL_JAVATypes.put("BIT", "string");
		mapSQL_JAVATypes.put("SHORT", "number");
		mapSQL_JAVATypes.put("INT", "number");
		mapSQL_JAVATypes.put("LONG", "number");
		mapSQL_JAVATypes.put("FLOAT", "number");
		mapSQL_JAVATypes.put("DOUBLE", "number");

		final Connection con = db.getConnection();
		final DatabaseMetaData meta = con.getMetaData();
		final ResultSet res = meta.getColumns(null, null, tableName, null);

		while (res.next()) {
			if (mapSQL_JAVATypes.containsKey(res.getString("TYPE_NAME"))) {
				tableSchema.add(new Column(res.getString("COLUMN_NAME"),
						mapSQL_JAVATypes.get(res.getString("TYPE_NAME"))));
			}
		}
		res.close();
	}

	public int getNumberOfAttributes() {
		return tableSchema.size();
	}

	/**
	 * Restituisce la colonna all' indice index.
	 * 
	 * @param index posizione della colonna
	 * @return la colonna
	 */
	public Column getColumn(final int index) {
		return tableSchema.get(index);
	}
}
