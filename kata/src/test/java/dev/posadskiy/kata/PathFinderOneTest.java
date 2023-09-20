package dev.posadskiy.kata;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class PathFinderOneTest {

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
                """;

        assertEquals(true, PathFinderOne.pathFinder(a));
        assertEquals(false, PathFinderOne.pathFinder(b));
        assertEquals(true, PathFinderOne.pathFinder(c));
        assertEquals(false, PathFinderOne.pathFinder(d));
    }

}
