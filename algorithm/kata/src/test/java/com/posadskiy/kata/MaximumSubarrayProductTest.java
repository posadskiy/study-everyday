package com.posadskiy.kata;

import org.junit.Test;

import static org.junit.Assert.*;

public class MaximumSubarrayProductTest {
    
    @Test
    public void test() {
        assertEquals(MaximumSubarrayProduct.sequenceBest(new int[]{2,-5,-2,-4,3}), 24);
    }

}
