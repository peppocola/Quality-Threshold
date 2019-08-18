package data;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import database.DatabaseConnectionException;
import database.DbAccess;
import database.EmptySetException;
import database.Example;
import database.NoValueException;
import database.QUERY_TYPE;
import database.TableData;
import database.TableSchema;

/**
 * modella una tebella del database.
 */
public class Data {

	private List<Example> data = new ArrayList<Example>();
	private int numberOfExamples;
	private List<Attribute> attributeSet = new LinkedList<Attribute>();

	/**
	 * popola l'attributeSet, data e inizializza numberOfExamples utilizzando
	 * tabella del database.
	 *
	 * @param table nome della tabella.
	 * @throws EmptySetException           se la tabella è vuota
	 * @throws SQLException                se la tabella non esiste
	 * @throws NoValueException            eccezione lanciata quando l'operatore
	 *                                     aggregato non da risultati
	 * @throws DatabaseConnectionException la connesione al databese è fallita
	 */

	public Data(final String table)
			throws EmptySetException, SQLException, NoValueException, DatabaseConnectionException {

		final DbAccess db = new DbAccess();
		final TableData tData = new TableData(db);



			db.initConnection();
			data = tData.getDistinctTransazioni(table);
			final TableSchema tSchema = new TableSchema(db, table);
			numberOfExamples = data.size();

			for (int i = 0; i < tSchema.getNumberOfAttributes(); i++) {

				if (tSchema.getColumn(i).isNumber()) {
					attributeSet.add(new ContinuousAttribute(tSchema.getColumn(i).getColumnName(),
							i,
							(float) tData.getAggregateColumnValue(table,
									tSchema.getColumn(i), QUERY_TYPE.MIN),
							(float) tData.getAggregateColumnValue(table,
									tSchema.getColumn(i), QUERY_TYPE.MAX)));
				} else {
					attributeSet.add(new DiscreteAttribute(tSchema.getColumn(i).getColumnName(), i,
							tData.getDistinctColumnValues(table, tSchema.getColumn(i))
									.toArray(new String[0])));
				}
			}

	}

	public int getNumberOfExamples() {
		return numberOfExamples;
	}

	public int getNumberOfAttributes() {
		return attributeSet.size();
	}

	/**
	 * Restituisce il valore dell' attributi alla riga exampleIndex e colonna
	 * attributeIndex.
	 *
	 * @param exampleIndex   riga
	 * @param attributeIndex colonna
	 * @return valore
	 */
	public Object getAttributeValue(final int exampleIndex, final int attributeIndex) {
		return data.get(exampleIndex).get(attributeIndex);
	}

	/**
	 * Restituisce l' attributo in posizione index.
	 *
	 * @param index indice dell' attributo
	 * @return l' attributo
	 */
	public Attribute getAttribute(final int index) {
		return attributeSet.get(index);
	}

	public List<Attribute> getAttributeSchema() {
		return attributeSet;
	}

	/**
	 * Restituisce la tupla all' indice index.
	 *
	 * @param index indice di riga
	 * @return tupla
	 */
	public Tuple getItemSet(final int index) {
		final Tuple tuple = new Tuple(attributeSet.size());
		for (int i = 0; i < attributeSet.size(); i++) {
			if (attributeSet.get(i) instanceof DiscreteAttribute) {
				tuple.add(new DiscreteItem((DiscreteAttribute) attributeSet.get(i),
						(String) data.get(index).get(i)), i);
			} else {
				tuple.add(new ContinuousItem((ContinuousAttribute) attributeSet.get(i),
						(double) data.get(index).get(i)), i);
			}
		}
		return tuple;
	}

	@Override
	public String toString() {
		String s = "";
		for (int i = 0; i < getNumberOfAttributes(); i++) {
			// se non avessi ridefinito il toString in Attribute, avrei la stampa di default
			// di Object
			s += attributeSet.get(i);
			if (i != getNumberOfAttributes() - 1) {
				s += ", ";
			}
		}
		s += '\n';
		for (int i = 0; i < getNumberOfExamples(); i++) {
			s += i + 1 + ":";
			for (int j = 0; j < getNumberOfAttributes(); j++) {
				s += getAttributeValue(i, j);
				if (j != getNumberOfAttributes() - 1) {
					s += ", ";
				}
			}
			s += "\n";
		}
		return s;
	}

}
