package core.exception;

public class DoubleThrow {
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
