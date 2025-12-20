package com.posadskiy.kata;

import org.junit.Test;

import static org.testng.AssertJUnit.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;

public class SortMyTextbooksTest {
    public static final SortMyTextbooks sorter = new SortMyTextbooks();

    @Test
    public void basicTest() {
        ArrayList<String> expected  = new ArrayList<String>(Arrays.asList("Algebra", "English", "Geometry", "History"));
        ArrayList<String> textbooks = new ArrayList<String>(Arrays.asList("Algebra", "History", "Geometry", "English"));
        assertEquals(expected, sorter.sort(textbooks));
    }
    @Test
    public void capitalizationTest() {
        ArrayList<String> expected  = new ArrayList<String>(Arrays.asList("Algebra", "english", "Geometry", "History"));
        ArrayList<String> textbooks = new ArrayList<String>(Arrays.asList("Algebra", "History", "Geometry", "english"));
        assertEquals(expected, sorter.sort(textbooks));
    }
    @Test
    public void symbolsTest() {
        ArrayList<String> expected  = new ArrayList<String>(Arrays.asList("$istory", "**English", "Alg#bra", "Geom^try"));
        ArrayList<String> textbooks = new ArrayList<String>(Arrays.asList("Alg#bra", "$istory", "Geom^try", "**English"));
        assertEquals(expected, sorter.sort(textbooks));
    }
}
