package data;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class ContinuousItemTest {


	@Test
	void testDistance() {
		ContinuousItem item1 = new ContinuousItem(new ContinuousAttribute("name", 0, 0, 2), 1.0);
		ContinuousItem item2 = new ContinuousItem(new ContinuousAttribute("name", 0, 0, 2), 2.0);
		assertEquals(0.5, item1.distance(item2));

	}

}
