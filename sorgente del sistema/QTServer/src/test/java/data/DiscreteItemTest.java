package data;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class DiscreteItemTest {

	@Test
	void testDistance() {
		DiscreteAttribute att = new DiscreteAttribute("name", 0, new String[] {"first", "second", "third" });
		DiscreteItem item1 = new DiscreteItem(att, "first");
		DiscreteItem item2 = new DiscreteItem(att, "second");
		assertEquals(1, item1.distance(item2));
	}

}
