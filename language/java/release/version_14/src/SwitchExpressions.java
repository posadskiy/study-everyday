import java.lang.System.Logger;
import java.lang.System.Logger.Level;

public class SwitchExpressions {
    private final static Logger log = System.getLogger("default");


    public static void main(String[] args) {
        int value = 1;
        switch (value) {
            case 1, 2, 3 -> log.log(Level.INFO, "One two or three");
            case 4, 5 -> log.log(Level.INFO, "Four or five");
            case 6 -> log.log(Level.INFO, "Six");
        }
        
        int result = switch (value) {
            case 1, 2, 3 -> 0;
            case 4, 5 -> 1;
            case 6 -> 2;
            default -> throw new IllegalStateException("Unexpected value: " + value);
        };
        
        log.log(Level.INFO, result);
        
        int response = switch (value) {
            case 1, 2, 3 -> {
                log.log(Level.INFO, "Value is " + value);
                yield 0;
            }
            case 4, 5 -> 1;
            case 6 -> 2;
            default -> throw new IllegalStateException("Unexpected value: " + value);
        };
        
        log.log(Level.INFO, response);
    }

}
