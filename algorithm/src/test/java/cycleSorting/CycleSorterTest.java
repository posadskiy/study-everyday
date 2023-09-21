package cycleSorting;

import org.junit.Test;

import static org.junit.Assert.*;

public class CycleSorterTest {

    @Test
    public void cycleSort() {
        int[] arr = {3, 2, 4, 0, 8, 5, 1};
        int[] expected = {0, 1, 2, 3, 4, 5, 8};

        final int[] result = new CycleSorter().cycleSort(arr);
        
        assertArrayEquals(expected, result);
    }
}
