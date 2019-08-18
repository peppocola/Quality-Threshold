package mining;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.sql.SQLException;

import org.junit.jupiter.api.Test;

import data.Data;
import database.DatabaseConnectionException;
import database.EmptySetException;
import database.NoValueException;

class ClusterTest {

	@Test
	void testCompareTo() {
		Data d = null;
		try {
			d = new Data("test");
		} catch (EmptySetException | SQLException | NoValueException | DatabaseConnectionException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
		Cluster c = new Cluster(d.getItemSet(0));
		c.addData(0);
		c.addData(1);

		Cluster c1 = new Cluster(d.getItemSet(1));
		c.addData(1);

		assertEquals(-1, c1.compareTo(c));

	}

}
