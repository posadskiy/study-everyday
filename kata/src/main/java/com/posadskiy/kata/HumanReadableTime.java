package com.posadskiy.kata;

public class HumanReadableTime {
    public static String makeReadable(int second) {
        final int seconds = second % 60;
        final int minutes = (second - seconds) / 60 % 60;
        final int hours = (second - seconds - minutes*60) / (60*60);
        
        return format(hours) + ":" + format(minutes) + ":" + format(seconds);
    }
    
    private static String format(int value) {
        return value > 9 ? String.valueOf(value) : "0" + value;
    }
    
    public static String makeReadableBest(int second) {
        return String.format("%02d:%02d:%02d", second / 3600, (second / 60) % 60, second % 60);
    }
}
