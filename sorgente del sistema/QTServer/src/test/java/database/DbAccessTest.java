package database;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

class DbAccessTest {

	@Test
	void testInitConnection() {
		DbAccess dba = new DbAccess();
		try {
			dba.initConnection();
		} catch (DatabaseConnectionException e) {
			e.printStackTrace();
		}
		assertNotNull(dba.getConnection());
	}

}
