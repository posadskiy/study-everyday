package com.posadskiy.kata;

import static org.junit.Assert.assertArrayEquals;

import org.junit.Test;

public class TheLiftTest {

    @Test
    public void testUp() {
        final int[][] queues = {
            new int[0], // G
            new int[0], // 1
            new int[]{5, 5, 5}, // 2
            new int[0], // 3
            new int[0], // 4
            new int[0], // 5
            new int[0], // 6
        };
        final int[] result = TheLift.theLift(queues, 5);
        assertArrayEquals(new int[]{0, 2, 5, 0}, result);
    }

    @Test
    public void testDown() {
        final int[][] queues = {
            new int[0], // G
            new int[0], // 1
            new int[]{1, 1}, // 2
            new int[0], // 3
            new int[0], // 4
            new int[0], // 5
            new int[0], // 6
        };
        final int[] result = TheLift.theLift(queues, 5);
        assertArrayEquals(new int[]{0, 2, 1, 0}, result);
    }

    @Test
    public void testUpAndUp() {
        final int[][] queues = {
            new int[0], // G
            new int[]{3}, // 1
            new int[]{4}, // 2
            new int[0], // 3
            new int[]{5}, // 4
            new int[0], // 5
            new int[0], // 6
        };
        final int[] result = TheLift.theLift(queues, 5);
        assertArrayEquals(new int[]{0, 1, 2, 3, 4, 5, 0}, result);
    }

    @Test
    public void testDownAndDown() {
        final int[][] queues = {
            new int[0], // G
            new int[]{0}, // 1
            new int[0], // 2
            new int[0], // 3
            new int[]{2}, // 4
            new int[]{3}, // 5
            new int[0], // 6
        };
        final int[] result = TheLift.theLift(queues, 5);
        assertArrayEquals(new int[]{0, 5, 4, 3, 2, 1, 0}, result);
    }

    @Test
    public void testTrickyQueues() {
        final int[][] queues = {
            new int[0],
            new int[]{0, 0, 0, 6},
            new int[0],
            new int[0],
            new int[0],
            new int[]{6, 6, 0, 0, 0, 6},
            new int[0]
        };
        final int[] result = TheLift.theLift(queues, 5);
        assertArrayEquals(new int[]{0, 1, 5, 6, 5, 1, 0, 1, 0}, result);
    }

    @Test
    public void testUpAndDown() {
        final int[][] queues = {
            new int[]{3},
            new int[]{2},
            new int[]{0},
            new int[]{2},
            new int[0],
            new int[0],
            new int[]{5}
        };
        final int[] result = TheLift.theLift(queues, 5);
        assertArrayEquals(new int[]{0, 1, 2, 3, 6, 5, 3, 2, 0}, result);
    }

    @Test
    public void testLiftFullUp() {
        final int[][] queues = {
            new int[]{3, 3, 3, 3, 3, 3},
            new int[0],
            new int[0],
            new int[0],
            new int[0],
            new int[0],
            new int[0]
        };
        final int[] result = TheLift.theLift(queues, 5);
        assertArrayEquals(new int[]{0, 3, 0, 3, 0}, result);
    }

    @Test
    public void testLiftFullUpAndDown() {
        final int[][] queues = {
            new int[]{3, 3, 3, 3, 3, 3},
            new int[0],
            new int[0],
            new int[0],
            new int[0],
            new int[]{4, 4, 4, 4, 4, 4},
            new int[0]
        };
        final int[] result = TheLift.theLift(queues, 5);
        assertArrayEquals(new int[]{0, 3, 5, 4, 0, 3, 5, 4, 0}, result);
    }

    @Test
    public void testYoYo() {
        final int[][] queues = {
            new int[0],
            new int[0],
            new int[]{4, 4, 4, 4},
            new int[0],
            new int[]{2, 2, 2, 2},
            new int[0],
            new int[0]
        };
        final int[] result = TheLift.theLift(queues, 2);
        assertArrayEquals(new int[]{0, 2, 4, 2, 4, 2, 0}, result);
    }

    @Test
    public void testFireDrill() {
        final int[][] queues = {
            new int[0],
            new int[]{0, 0, 0, 0},
            new int[]{0, 0, 0, 0},
            new int[]{0, 0, 0, 0},
            new int[]{0, 0, 0, 0},
            new int[]{0, 0, 0, 0},
            new int[]{0, 0, 0, 0}
        };
        final int[] result = TheLift.theLift(queues, 5);
        assertArrayEquals(new int[]{0, 6, 5, 4, 3, 2, 1, 0, 5, 4, 3, 2, 1, 0, 4, 3, 2, 1, 0, 3, 2, 1, 0, 1, 0}, result);
    }

    /**
     * 0 - 0 1 - 2 2 - 3 3 - 1
     */
    @Test
    public void testHighlander() {
        final int[][] queues = {
            new int[0],
            new int[]{2},
            new int[]{3, 3, 3},
            new int[]{1},
            new int[0],
            new int[0],
            new int[0]
        };
        final int[] result = TheLift.theLift(queues, 1);
        assertArrayEquals(new int[]{0, 1, 2, 3, 1, 2, 3, 2, 3, 0}, result);
    }

    /**
     * 0 -> 3 3 3 1 -> 3 3 3 2 2 -> 3 3 3 3 3 -> 2 2 -> 0 1 0 1 -> 0 0 0 0 0 -> 1 -> 0 0 ->
     */
    @Test
    public void testRandom() {
        final int[][] queues = {
            new int[]{3, 3, 3},
            new int[]{2, 0, 0, 0},
            new int[]{3, 0, 1, 0},
            new int[]{2}
        };
        final int[] result = TheLift.theLift(queues, 4);
        assertArrayEquals(new int[]{0, 1, 2, 3, 2, 1, 0, 1, 0}, result);
    }

    /**
     * 0 - 1 6
     * 1 - 6 10
     * 5 - 6 10
     * 6 - 10 8
     * 7 - 10 8
     * 8 - 10
     * 10 - 11
     * 11 - 6
     * 10 - 6 8
     * 8 - 6 6
     * 6 - 3
     * 3 - 
     * 0 - 6 10
     *
     */
    @Test
    public void testRandom2() {
        final int[][] queues = {
            new int[]{1, 6, 6, 10}, //0
            new int[]{10}, //1
            new int[0], //2
            new int[0], //3
            new int[0], //4
            new int[]{10}, //5
            new int[]{8, 3}, //6
            new int[]{10}, //7
            new int[]{6, 1, 2, 3}, //8
            new int[0], //9
            new int[]{11, 8, 7, 5}, //10
            new int[]{6} //11
        };
        final int[] result = TheLift.theLift(queues, 2);
        assertArrayEquals(new int[]{0, 1, 5, 6, 7, 8, 10, 11, 10, 8, 6, 3, 0, 5, 6, 7, 10, 8, 7, 5, 10, 8, 2, 1, 8, 3, 0}, result);
    }

    /**
     * 0 - 2 8
     * 1 - 2 8 4 14
     * 2 - 8 4 14
     * 3 - 8 4 14 6
     * 4 - 8 14 6 9
     * 5 - 8 14 6 9
     * 6 - 8 14 9 9
     * 7 - 8 14 9 9
     * 8 - 14 9 9 14
     * 9 - 14 14
     * 10 - 14 14 14
     * 11 - 14 14 14
     * 12 - 14 14 14 13
     * 13 - 14 14 14
     * 14 - 16
     * 15 !
     * 16 - 
     */
    @Test
    public void testRandom3() {
        final int[][] queues = {
            new int[]{2, 8}, //0
            new int[]{4, 14, 8}, //1
            new int[0], //2
            new int[]{6}, //3
            new int[]{9}, //4
            new int[]{2, 3, 17, 8}, //5
            new int[]{9}, //6
            new int[]{2, 9, 5}, //7
            new int[]{6, 14, 7, 10}, //8
            new int[0], //9
            new int[]{14}, //10
            new int[0], //11
            new int[]{13, 13}, //12
            new int[]{6, 8, 0}, //13
            new int[]{10, 7, 16}, //14
            new int[]{14, 0}, //15
            new int[]{4, 2, 14}, //16
            new int[]{4}, //17
            new int[]{8, 3}, //18
        };
        final int[] result = TheLift.theLift(queues, 4);
        assertArrayEquals(new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 12, 13, 14, 16, 18, 17, 16, 15, 14, 13, 8, 7, 6, 5, 4, 3, 2, 1, 5, 7, 8, 9, 10, 12, 13, 17, 16, 15, 14, 13, 10, 8, 7, 5, 3, 2, 0, 13, 8, 6, 0}, result);
    }

    /**
     * 0 - 5
     * 1 - 5 8 3
     * 2 !
     * 3 - 5 8
     * 4 - 5 8 14 9 11
     * 5 - 8 14 9 11 9
     * 6 !
     * 7 !
     * 8 - 14 9 11 9
     * 9 - 14 11 
     * 10 - 14 11 11 12
     * 11 - 14 12
     * 12 - 14 
     * 13 !
     * 14 - 
     * 13 - 2
     * 12 - 2 8
     * 11 - 2 8 3 2 7
     * 10 - 2 8 3 2 7
     * 9 - 2 8 3 2 7
     * 8 - 2 3 2 7
     * 7 - 2 3 2
     * 6 - 2 3 2 1
     * 5 !
     * 4 - 2 3 2 1 1
     * 3 - 2 2 1 1 2
     * 2 - 1 1 1 0 1
     * 1 - 0
     * 0 - 
     * 5 - 8 13
     * 8 - 13
     * 13 - 
     * 10 - 6 2
     * 9 - 6 2 6
     * 6 - 2
     * 2 - 0
     * 0
     */
    @Test
    public void testRandom4() {
        final int[][] queues = {
            new int[]{5}, //0
            new int[]{8, 3}, //1
            new int[]{1, 0, 1, 0}, //2
            new int[]{2}, //3
            new int[]{14, 9, 11, 1}, //4
            new int[]{9, 8, 13}, //5
            new int[]{1}, //6
            new int[0], //7
            new int[0], //8
            new int[]{6}, //9
            new int[]{6, 2, 11, 12}, //10
            new int[]{3, 2, 7}, //11
            new int[]{8}, //12
            new int[]{2}, //13
            new int[0] //14
        };
        final int[] result = TheLift.theLift(queues, 5);
        assertArrayEquals(new int[]{0, 1, 3, 4, 5, 8, 9, 10, 11, 12, 14, 13, 12, 11, 10, 9, 8, 8, 7, 6, 4, 3, 2, 1, 0, 5, 8, 13, 10, 9, 6, 2, 0}, result);
    }
}
