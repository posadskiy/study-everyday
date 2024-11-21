package com.posadskiy.kata;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class PathFinderTest {

    @Test
    public void sampleTests() {

        String a = "000\n" +
            "000\n" +
            "000",

            b = "010\n" +
                "010\n" +
                "010",

            c = "010\n" +
                "101\n" +
                "010",

            d = "0707\n" +
                "7070\n" +
                "0707\n" +
                "7070",

            e = "700000\n" +
                "077770\n" +
                "077770\n" +
                "077770\n" +
                "077770\n" +
                "000007",

            f = "777000\n" +
                "007000\n" +
                "007000\n" +
                "007000\n" +
                "007000\n" +
                "007777",

            g = "000000\n" +
                "000000\n" +
                "000000\n" +
                "000010\n" +
                "000109\n" +
                "001010",
        
        h = """
            3877425818268144
            1937329511852907
            8175460830884760
            1170178773522424
            2223725747490523
            0839601162614127
            6988919095839219
            2733777843347581
            4982144129892238
            3248172470003010
            8408552265808115
            8925885059090939
            1246138847849023
            0336119000124192
            5853437260160869
            5326136729394888
            """,
        
        j = """
            11111
            99991
            11111
            19999
            11111
            """;

        assertEquals(0,PathFinder.pathFinder(a));
        assertEquals(2,PathFinder.pathFinder(b));
        assertEquals(4,PathFinder.pathFinder(c));
        assertEquals(42,PathFinder.pathFinder(d));
        assertEquals(14,PathFinder.pathFinder(e));
        assertEquals(0,PathFinder.pathFinder(f));
        assertEquals(4,PathFinder.pathFinder(g));
        assertEquals(51,PathFinder.pathFinder(h));
        assertEquals(0,PathFinder.pathFinder(j));
    }
}
