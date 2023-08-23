package dev.posadskiy.kata;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.testng.Assert.assertEquals;

public class SoManyPermutationsTest {
    @Test
    public void example1() {
        assertEquals(new ArrayList<>(List.of("a")),
            SoManyPermutations.singlePermutations("a").stream().sorted().collect(Collectors.toList()));
    }

    @Test
    public void example2() {
        assertEquals(new ArrayList<>(Arrays.asList("ab", "ba")),
            SoManyPermutations.singlePermutations("ab").stream().sorted().collect(Collectors.toList()));
    }
    
    @Test
    public void example4() {
        assertEquals(new ArrayList<>(Arrays.asList("abc", "acb", "bac", "bca", "cab", "cba")),
            SoManyPermutations.singlePermutations("abc").stream().sorted().collect(Collectors.toList()));
    }

    @Test
    public void example3() {
        assertEquals(new ArrayList<>(Arrays.asList("aabb", "abab", "abba", "baab", "baba", "bbaa")),
            SoManyPermutations.singlePermutations("aabb").stream().sorted().collect(Collectors.toList()));
    }

}
