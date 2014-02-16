import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;


public class mainstuffTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testTranslate() {
		mainstuff a = new mainstuff();
		assertEquals("madbro", a.translate("hi"));
	}

}
