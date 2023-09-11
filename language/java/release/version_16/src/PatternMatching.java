/**
 * JEP 394: Pattern Matching for instanceof
 * <a href="https://openjdk.org/jeps/394">Docs</a>
 */
public class PatternMatching {
    private final static System.Logger log = System.getLogger("default");
    
    public static void main(String[] args) {
        Object object = "Some sentence";

        // Basic example
        if (object instanceof String sentence) {
            log.log(System.Logger.Level.INFO, sentence);
        }

        // Using variable "s" for future operations
        if (object instanceof String s && s.length() > 5) {
            log.log(System.Logger.Level.INFO, s);
        }

        /* 
        Can't compile, because if first part is false, then no variable "s" available for second part
        if (object instanceof String s || s.length() > 5) {
            log.log(System.Logger.Level.INFO, s);
        }
        */
    }
}

class PointExtender {
    int x;
    int y;
    
    public final boolean equals(Object o) {
        return (o instanceof PointExtender other) 
            && x == other.x
            && y == other.y;
    }
}
