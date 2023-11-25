package com.posadskiy.kata;

public class RgbToHex {
    public static String rgb(int r, int g, int b) {
        return String.format("%02X%02X%02X", validate(r), validate(g), validate(b));
    }
    
    private static int validate(int value) {
        if (value > 255) return 255;
        if (value < 0) return 0;
        
        return value;
    }
}
