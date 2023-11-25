package com.posadskiy.kata;

import org.junit.Test;

import static org.junit.Assert.*;

public class BestTimeToBuyAndSellStockTest {
    
    @Test
    public void test() {
        assertEquals(new BestTimeToBuyAndSellStock().maxProfit(new int[]{7,1,5,3,6,4}), 5);
    }

}
