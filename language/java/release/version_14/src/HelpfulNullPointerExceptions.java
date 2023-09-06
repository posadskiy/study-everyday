import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.util.List;

/**
 * JEP 358: Helpful NullPointerExceptions
 * <a href="https://openjdk.org/jeps/358">Docs</a>
 */
public class HelpfulNullPointerExceptions {
    private final static Logger log = System.getLogger("default");

    public static void main(String[] args) {
        First first = null;
        log.log(Level.INFO, first.second.third.field);
    }
    
    class First {
        Second second;
    }
    
    class Second {
        Third third;
    }
    
    class Third {
        String field;
    }
}
