package com.posadskiy.algorithm.slidingWindow;

import com.posadskiy.algorithm.slidingWindow.FindLargestSubstringOfEquals;
import org.junit.Test;

import static org.junit.Assert.*;

public class FindLargestSubstringOfEqualsTest {

    @Test
    public void find_sevenElementsInTheRow_Returns7() {
        final FindLargestSubstringOfEquals findLargestSubstringOfEquals = new FindLargestSubstringOfEquals();
        var expected = 7;

        final int result = findLargestSubstringOfEquals.find("abccbadfsdafsadfasdfasdfsadfwwwwwwwsadfasdrqwrfasfwerwrtyetfasfdatwretwfsdfa", 10);

        assertEquals(expected, result);
    }

    @Test
    public void find_twoElementsInTheRowInTheFirstWindow_returns2() {
        final FindLargestSubstringOfEquals findLargestSubstringOfEquals = new FindLargestSubstringOfEquals();
        var expected = 2;

        final int result = findLargestSubstringOfEquals.find("abccbadfsdafsadfasdfasdfsadfsadfasdrqwrfasfwerwrtyetfasfdatwretwfsdfa", 10);

        assertEquals(expected, result);
    }
}
