package com.posadskiy.kata;

import org.junit.Test;

import static org.junit.Assert.*;

public class BattleFieldTest {
    private static int[][] battleField = {{1, 0, 0, 0, 0, 1, 1, 0, 0, 0},
        {1, 0, 1, 0, 0, 0, 0, 0, 1, 0},
        {1, 0, 1, 0, 1, 1, 1, 0, 1, 0},
        {1, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 1, 0},
        {0, 0, 0, 0, 1, 1, 1, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 1, 0},
        {0, 0, 0, 1, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 1, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0}};

    @Test
    public void SampleTest() {
        assertEquals(true, BattleField.fieldValidator(battleField));
    }

}
