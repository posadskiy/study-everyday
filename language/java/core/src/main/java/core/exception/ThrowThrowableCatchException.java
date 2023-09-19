package core.exception;

public class ThrowThrowableCatchException {

    public static void main(String[] args) throws Throwable {
        try {
            Throwable t = new Exception();
            throw t;
        } catch (Exception e) {
            System.err.println("Exception");
        }
    }

}
