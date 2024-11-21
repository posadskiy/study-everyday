package com.posadskiy.kata;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class RailFenceCipherTest {

    @Test
    public void encodeSampleTests() {
        String[][] encodes = {{"WEAREDISCOVEREDFLEEATONCE", "WECRLTEERDSOEEFEAOCAIVDEN"},  // 3 rails
            {"Hello, World!", "Hoo!el,Wrdl l"},    // 3 rails
            {"Hello, World!", "H !e,Wdloollr"},    // 4 rails
            {"", ""}                               // 3 rails (even if...)
        };
        int[] rails = {3, 3, 4, 3};
        for (int i = 0; i < encodes.length; i++) {
            assertEquals(encodes[i][1], RailFenceCipher.encode(encodes[i][0], rails[i]));
        }
    }


    @Test
    public void decodeSampleTests() {
        String[][] decodes = {{"WECRLTEERDSOEEFEAOCAIVDEN", "WEAREDISCOVEREDFLEEATONCE"},    // 3 rails
            {"H !e,Wdloollr", "Hello, World!"},    // 4 rails
            {"", ""}                               // 3 rails (even if...)
        };
        int[] rails = {3, 4, 3};
        for (int i = 0; i < decodes.length; i++) {
            assertEquals(decodes[i][1], RailFenceCipher.decode(decodes[i][0], rails[i]));
        }
    }

}
