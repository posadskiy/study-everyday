package cycleSorting;

import org.junit.Test;

import static org.junit.Assert.*;

public class MissingNumberFinderTest {

    @Test
    public void missingNumber() {
        int[] argument = {0, 3, 2};
        int expected = 1;

        final int result = new MissingNumberFinder().missingNumber(argument);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void missingNumberTwo() {
        int[] argument = {3, 0, 1};
        int expected = 2;

        final int result = new MissingNumberFinder().missingNumber(argument);
        
        assertEquals(expected, result);
    }
}
