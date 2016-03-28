package tests.helpersTests;

import helpers.LetterMapper;
import static org.junit.Assert.*;

import org.junit.*;

public class LetterMapperTests {
	
	@Test
    public void testLetterMapperSwitch() {
        assertEquals('A', LetterMapper.mapToLetter(1));
        assertEquals('B', LetterMapper.mapToLetter(2));
        assertEquals('C', LetterMapper.mapToLetter(3));
        assertEquals('D', LetterMapper.mapToLetter(4));
        assertEquals('E', LetterMapper.mapToLetter(5));
        assertEquals('F', LetterMapper.mapToLetter(6));
        assertEquals('G', LetterMapper.mapToLetter(7));
        assertEquals('H', LetterMapper.mapToLetter(8));
        assertEquals('I', LetterMapper.mapToLetter(9));
        assertEquals('J', LetterMapper.mapToLetter(10));
        assertEquals('K', LetterMapper.mapToLetter(11));
        assertEquals('L', LetterMapper.mapToLetter(12));
        assertEquals('M', LetterMapper.mapToLetter(13));
        assertEquals('N', LetterMapper.mapToLetter(14));
        assertEquals('O', LetterMapper.mapToLetter(15));
        assertEquals('P', LetterMapper.mapToLetter(16));
        assertEquals('Q', LetterMapper.mapToLetter(17));
        assertEquals('R', LetterMapper.mapToLetter(18));
        assertEquals('S', LetterMapper.mapToLetter(19));
        assertEquals('T', LetterMapper.mapToLetter(20));
        assertEquals(0, LetterMapper.mapToLetter(1000));
        assertEquals(0, LetterMapper.mapToLetter(0));
        
        System.out.println("@Test - testLetterMapperSwitch");
    }
	



}
