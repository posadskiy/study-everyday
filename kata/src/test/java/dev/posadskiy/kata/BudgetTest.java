package dev.posadskiy.kata;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BudgetTest {
    @Test
    public void exampleTest() {
        assertEquals(1, Budget.find_caterer(150,10));
        assertEquals(2, Budget.find_caterer(1500,60));
        assertEquals(3, Budget.find_caterer(1500,61));
        assertEquals(-1, Budget.find_caterer(100,0));
        assertEquals(3, Budget.find_caterer(200,5));
        assertEquals(2, Budget.find_caterer(1000,45));
        assertEquals(-1, Budget.find_caterer(940,70));
    }
}
