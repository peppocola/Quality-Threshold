package data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import database.DatabaseConnectionException;
import database.EmptySetException;
import database.NoValueException;

class DataTest {
	
	static Data d;

	@BeforeAll
	static void testData() {
		
			assertThrows(EmptySetException.class, ()->{
				d=new Data ("emptytable");
				});
			
			try {
				d=new Data ("test");
		} catch (EmptySetException | SQLException | NoValueException | DatabaseConnectionException e) {
				e.printStackTrace();
				fail(e.getMessage());
			}

	}
	
	@Test
	void testGetNumberOfExamples() {
		assertEquals(d.getNumberOfExamples(), 2);
	}
	@Test
	void testGetNumberOfAttributes() {
		assertEquals(d.getNumberOfAttributes(), 2);
	}
	@Test
	void testGetAttributeValue() {
		assertEquals(d.getAttributeValue(0,0), "primo");
	}
	
	@Test
	void testGetAttribute() {
		assertEquals(d.getAttribute(0), new DiscreteAttribute("discreto", 0, new String[] {"primo","secondo"}));
		assertEquals(d.getAttribute(1), new ContinuousAttribute("continuo", 1, 30.3,31.2));
	}
	
	@Test
	void testGetAttributeSchema() {
		List<Attribute> test = new LinkedList<>();
		test.add(new DiscreteAttribute("discreto", 0, new String[] {"primo","secondo"}));
		test.add(new ContinuousAttribute("continuo", 1, 30.3,31.2));
		List<Attribute> real = d.getAttributeSchema();
		assertEquals(test.get(0),real.get(0));
		assertEquals(test.get(1),real.get(1));
		assertEquals(test,real);
		
	}

	@Test
	void testGetItemSet() {
	
		assertEquals(d.getItemSet(0).get(0), new DiscreteItem(new DiscreteAttribute("discreto", 0, new String[] {"primo","secondo"}), "primo"));
		assertEquals(d.getItemSet(0).get(1), new ContinuousItem(new ContinuousAttribute("continuo", 1, 30.3,31.2), 30.3));
		
	}

}
