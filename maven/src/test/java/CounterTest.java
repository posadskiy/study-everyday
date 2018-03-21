import org.junit.Test;

import static org.junit.Assert.*;

public class CounterTest {

    @Test
    public void printCount() {
        Counter counter = new Counter();

        Integer result = counter.printCount();
        Integer secondResult = counter.printCount();

        assertEquals(0, result.intValue());
        assertEquals(1, secondResult.intValue());
    }
}