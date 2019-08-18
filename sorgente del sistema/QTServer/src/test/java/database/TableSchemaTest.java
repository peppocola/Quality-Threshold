package database;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.sql.SQLException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class TableSchemaTest {

	static TableSchema ts = null;

	@BeforeAll
	static void testTableSchema() {
		try {
			DbAccess db = new DbAccess();
			db.initConnection();
			ts = new TableSchema(db, "test");
		} catch (SQLException e) {
			e.printStackTrace();
			fail(e.getMessage());
		} catch (DatabaseConnectionException e) {
			e.printStackTrace();
		}
	}

	@Test
	void testGetNumberOfAttribute() {
		assertEquals(2, ts.getNumberOfAttributes());
	}

	@Test
	void testGetColumn() {
		assertTrue(ts.getColumn(1).isNumber());
		assertFalse(ts.getColumn(0).isNumber());
	}

}
