package slidingWindow;

import org.junit.Test;

import static org.junit.Assert.*;

public class FindLargestSubstringOfUniquesTest {

    @Test
    public void test() {
        final FindLargestSubstringOfUniques findLargestSubstringOfUniques = new FindLargestSubstringOfUniques();
        var expected = 8;

        final int result = findLargestSubstringOfUniques.find("abccbadfsdafsadfasdfasdfsadfsadfasdrqwrfasfwerwrtyetfasfdatwretwfsdfa", 10);
        
        assertEquals(expected, result);
    }
}
