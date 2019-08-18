package mining;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.sql.SQLException;

import org.junit.jupiter.api.Test;

import data.Data;
import data.EmptyDatasetException;
import database.DatabaseConnectionException;
import database.EmptySetException;
import database.NoValueException;

class QTMinerTest {
	QTMiner q = null;
	Data d=null;

	@Test
	void testCompute() {
		
		try {
			d=new Data ("test");
		} catch (EmptySetException | SQLException | NoValueException | DatabaseConnectionException e) {
			e.printStackTrace();
			fail("failed");
		}
		
		q = new QTMiner(0);
		
		try {
			assertTrue(q.compute(d)==2);
		} catch (ClusteringRadiusException | EmptyDatasetException e) {
			fail("exception thrown");
			e.printStackTrace();
		}
		
		q=new QTMiner(100);
		
		assertThrows(ClusteringRadiusException.class, ()->{
			q.compute(d);
		});
		
	}

	@Test
	void testSalva() {
		try {
			d=new Data ("playtennis");
		} catch (EmptySetException | SQLException | NoValueException | DatabaseConnectionException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
		
		q = new QTMiner(2);
		try {
			q.compute(d);
		} catch (ClusteringRadiusException | EmptyDatasetException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
		
		try {
			q.salva("playtennis_2.0.dmp");
		} catch (IOException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
		
		QTMiner n = null;
		
		try {
			n = new QTMiner ("playtennis_2.0.dmp");
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
		
		assertEquals(q.getC(),n.getC());
		
	}

}
