package modelTests;

import model.Board;

import static org.junit.Assert.*;

import org.junit.Test;

public class BoardTests {

	
	@Test
    public void testNumOfRows() {
		Board b = new Board(2,2);
        
		assertEquals(2, b.getNumberOfRows());
        
        System.out.println("@Test - testNumberOfRows");
    }
	
	@Test
    public void testNumOfColumns() {
		Board b = new Board(2,2);
        
		assertEquals(2, b.getNumberOfColumns());
        
        System.out.println("@Test - testNumberOfColumns");
    }
	
	
}
