package data;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class ContinuousAttributeTest {

	@Test
	void testGetScaledValue() {
		ContinuousAttribute att = new ContinuousAttribute("ciao", 0, 0, 2);
		assertEquals(0.5, att.getScaledValue(1));
	}
	

}
