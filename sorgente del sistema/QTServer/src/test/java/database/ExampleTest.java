package database;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class ExampleTest {

	@Test
	void testCompareTo() {
		Example e1 = new Example();
		e1.add(1);
		e1.add("first");
		Example e2 = new Example();
		e2.add(2);
		e2.add("second");

		assertEquals(1, e1.compareTo(e2));
	}

}
