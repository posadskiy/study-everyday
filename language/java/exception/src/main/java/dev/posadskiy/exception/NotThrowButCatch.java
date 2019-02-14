package dev.posadskiy.exception;

public class NotThrowButCatch {

    public static void main(String[] args) {
        try {
            String s = "This code don't generate any exceptions";
        } catch (NullPointerException | IllegalArgumentException/*FileNotFoundException*/ e) {
            e.printStackTrace();
        }
    }

}
