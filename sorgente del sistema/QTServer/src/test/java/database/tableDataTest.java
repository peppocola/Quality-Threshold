package database;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.Set;
import java.util.TreeSet;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class tableDataTest {
	static TableData td = null;
	static DbAccess db = null;
	static TableSchema ts = null;
	@BeforeAll
	static void testTableData() {

		try {

			db = new DbAccess();
			db.initConnection();
			ts = new TableSchema(db, "test");
		} catch (DatabaseConnectionException | SQLException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
		td = new TableData(db);
	}

	@Test
	void testGetDistinctTransazioni() {
		LinkedList<Example> trans = new LinkedList<Example>();
		Example e = new Example();
		e.add("primo");
		e.add(30.3);
		Example e1 = new Example();
		e1.add("secondo");
		e1.add(31.2);
		trans.add(e);
		trans.add(e1);

		try {
			assertEquals(trans, td.getDistinctTransazioni("test"));
		} catch (SQLException e2) {
			e2.printStackTrace();
			fail(e2.getMessage());
		} catch (EmptySetException e2) {
			e2.printStackTrace();
			fail(e2.getMessage());
		}
	}

	@Test
	void testGetDistinctColumnValues() {
		Set<Object> values = new TreeSet<Object>();
		values.add("primo");
		values.add("secondo");

		try {
			assertEquals(values, td.getDistinctColumnValues("test", ts.getColumn(0)));
		} catch (SQLException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	@Test
	void testGetAggregateColumnValue() {
		try {
			assertEquals((float) 30.3, td.getAggregateColumnValue("test", ts.getColumn(1), QUERY_TYPE.MIN));
		} catch (SQLException e) {
			e.printStackTrace();
			fail(e.getMessage());
		} catch (NoValueException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

}
