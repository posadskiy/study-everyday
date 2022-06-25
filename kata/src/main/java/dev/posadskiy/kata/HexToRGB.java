package dev.posadskiy.kata;

public class HexToRGB {
    public static int[] hexStringToRGB(String hex) {

        final int red = (Character.digit(hex.charAt(1), 16) << 4) + Character.digit(hex.charAt(2), 16);
        final int green = (Character.digit(hex.charAt(3), 16) << 4) + Character.digit(hex.charAt(4), 16);
        final int blue = (Character.digit(hex.charAt(5), 16) << 4) + Character.digit(hex.charAt(6), 16);

        return new int[]{red, green, blue};
    }
}
