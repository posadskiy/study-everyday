package dev.posadskiy.exception;

public class Start {
    public static void main(String[] args) {
        try {
            throw new NullPointerException();
        } catch (NullPointerException e) {
            throw e;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
