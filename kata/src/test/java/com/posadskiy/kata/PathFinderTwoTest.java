package com.posadskiy.kata;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class PathFinderTwoTest {

    @Test
    public void sampleTests() {

        String a = """
            .W.
            .W.
            ...
            """,

            b = """
                .W.
                .W.
                W..
                """,

            c = """
                ......
                ......
                ......
                ......
                ......
                ......
                """,

            d = """
                ......
                ......
                ......
                ......
                .....W
                ....W.
                """,
            
            e = """
                .W...W...W...
                .W.W.W.W.W.W.
                .W.W.W.W.W.W.
                .W.W.W.W.W.W.
                .W.W.W.W.W.W.
                .W.W.W.W.W.W.
                .W.W.W.W.W.W.
                .W.W.W.W.W.W.
                .W.W.W.W.W.W.
                .W.W.W.W.W.W.
                .W.W.W.W.W.W.
                .W.W.W.W.W.W.
                ...W...W...W.
                """;

        assertEquals(a, 4, PathFinderTwo.pathFinder(a));
        assertEquals(b, -1, PathFinderTwo.pathFinder(b));
        assertEquals(c, 10, PathFinderTwo.pathFinder(c));
        assertEquals(d, -1, PathFinderTwo.pathFinder(d));
        assertEquals(d, 96, PathFinderTwo.pathFinder(e));
    }
}
