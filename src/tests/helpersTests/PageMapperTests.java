package tests.helpersTests;

import helpers.PageMapper;
import static org.junit.Assert.*;
import org.junit.*;

public class PageMapperTests {
	
	PageMapper p1 = new PageMapper();
	
	@Test
    public void testLetterMapperSwitch() {
        assertNotNull("tested", p1.getPage(helpers.Enums.Page.FRONT_PAGE));
        assertNotNull("tested", p1.getPage(helpers.Enums.Page.SOLVE_PAGE));
        assertNotNull("tested", p1.getPage(helpers.Enums.Page.CREATOR_PAGE_ONE));
        assertNotNull("tested", p1.getPage(helpers.Enums.Page.CREATOR_PAGE_TWO));
        
        System.out.println("@Test - testPageMapper");
    }
	
	

}
