package binarySearch;

import org.junit.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

public class BinarySearcherTest {

    @Test
    public void search() {
        final List<Integer> values = List.of(-8, -5, -4, 0, 1, 3, 7, 9, 15, 33, 82, 192, 292, 343);
        var expected = 7;
        var valueToFind = 9;

        final Optional<Integer> result = BinarySearcher.search(values, valueToFind);
        
        assertEquals(expected, (int) result.orElseThrow());
    }
}
