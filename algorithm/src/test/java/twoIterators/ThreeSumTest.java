package twoIterators;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class ThreeSumTest {
    @Test
    public void test() {


        final List<List<Integer>> result = new ThreeSum().threeSum(new int[]{-1, 0, 1, 2, -1, -4});

        assertEquals(-1, (int) result.get(0).get(0));
        assertEquals(-1, (int) result.get(0).get(1));
        assertEquals(2, (int) result.get(0).get(2));
        assertEquals(-1, (int) result.get(1).get(0));
        assertEquals(0, (int) result.get(1).get(1));
        assertEquals(1, (int) result.get(1).get(2));
        assertEquals(2, result.size());
    }
}
