package slidingWindow;

import org.junit.Test;

import static org.junit.Assert.*;

public class LongestSubstringWithoutRepeatingCharactersTest {

    @Test
    public void test() {
        final int result = new LongestSubstringWithoutRepeatingCharacters().lengthOfLongestSubstring("dvdf");
        
        assertEquals(3, result );
    }
    @Test
    public void test2() {
        final int result = new LongestSubstringWithoutRepeatingCharacters().lengthOfLongestSubstring("abcabcbb");
        
        assertEquals(3, result );
    }
    @Test
    public void test3() {
        final int result = new LongestSubstringWithoutRepeatingCharacters().lengthOfLongestSubstring("abba");
        
        assertEquals(2, result );
    }
}
