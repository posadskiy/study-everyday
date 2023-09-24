package twoHeap;

import org.junit.Test;

import static org.junit.Assert.*;

public class TwoHeapMedianFinderTest {
    private static final double DELTA = 0.00001; 
    
    @Test
    public void simpleTest() {
        final TwoHeapMedianFinder finder = new TwoHeapMedianFinder();
        var expected = 3;
        
        finder.add(0);
        finder.add(1);
        finder.add(2);
        finder.add(3);
        finder.add(4);
        finder.add(5);
        finder.add(6);

        final double median = finder.findMedian();
        
        assertEquals(expected, median, DELTA);
    }

    @Test
    public void moreAdvancedTest() {
        final TwoHeapMedianFinder finder = new TwoHeapMedianFinder();
        var expected = 0;
        
        finder.add(38);
        finder.add(16);
        finder.add(-8);
        finder.add(1024);
        finder.add(-824);
        finder.add(0);
        finder.add(88);
        finder.add(-992);
        finder.add(-9921);
        finder.add(-942);
        finder.add(43);

        final double median = finder.findMedian();
        
        assertEquals(expected, median, DELTA);
    }

}
