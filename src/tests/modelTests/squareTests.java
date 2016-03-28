package modelTests;
import model.Square;

import static org.junit.Assert.*;

import org.junit.*;


public class squareTests {
	
	Square sq = new Square();
	
	@Test
    public void testFlags() {
		
        assertFalse(sq.isFlagged());
        assertFalse(sq.isBlack());
        assertFalse(sq.isUserSelected());
        
        sq.toggleFlag();
        sq.toggleBlack();
        sq.toggleUserSelected();
        
        assertTrue(sq.isFlagged());
        assertTrue(sq.isBlack());
        assertTrue(sq.isUserSelected());
        
        System.out.println("@Test - testFlags");
    }
	

}
