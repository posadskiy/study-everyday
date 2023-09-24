package topKElements;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class TheLargestElementsFinderTest {
    
    @Test
    public void test() {
        final List<Integer> values = List.of(-2, 4, 2, 7, 3, -2, -17, 44, 2, 34, 22, 7, -11);
        var size = 3;
        var expected = List.of(44, 34, 22);

        final List<Integer> result = TheLargestElementsFinder.finder(values, size);
        
        assertEquals(size, result.size());
        assertTrue(result.contains(expected.get(0)));
        assertTrue(result.contains(expected.get(1)));
        assertTrue(result.contains(expected.get(2)));
    }

}
