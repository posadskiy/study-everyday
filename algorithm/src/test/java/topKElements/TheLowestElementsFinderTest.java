package topKElements;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TheLowestElementsFinderTest {

    @Test
    public void test() {
        final List<Integer> values = ListGenerator.generate();
        var size = 3;
        var expected = List.of(-17, -11, -2);

        final List<Integer> result = TheLowestElementsFinder.finder(values, size);

        assertEquals(size, result.size());
        for (Integer value : expected) {
            assertTrue(result.contains(value));
        }
    }

    @Test
    public void testOne() {
        final List<Integer> values = ListGenerator.generate();
        var size = 5;
        var expected = List.of(-17, -11, -2, -2, 2);

        final List<Integer> result = TheLowestElementsFinder.finder(values, size);

        assertEquals(size, result.size());
        for (Integer value : expected) {
            assertTrue(result.contains(value));
        }
    }

    @Test
    public void testTwo() {
        final List<Integer> values = ListGenerator.generate();
        var size = 2;
        var expected = List.of(-17, -11);

        final List<Integer> result = TheLowestElementsFinder.finder(values, size);

        assertEquals(size, result.size());
        for (Integer value : expected) {
            assertTrue(result.contains(value));
        }
    }
}
