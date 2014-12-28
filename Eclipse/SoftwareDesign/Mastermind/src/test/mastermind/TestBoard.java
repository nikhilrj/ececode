package test.mastermind;

import static org.junit.Assert.*;

import mastermind.Board;

import org.junit.Before;
import org.junit.Test;

public class TestBoard {
Board b=null;
	@Before
	public void setUp() throws Exception {
	b = new Board();
	}
//annotation
	@Test
	public void testGetB() {
		assertFalse(b.getB());
	}

}
