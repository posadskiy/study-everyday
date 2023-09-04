import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.util.stream.Collectors;

public class StringNewApi {
    private final static Logger log = System.getLogger("default");

    public static void main(String[] args) {
        log.log(Level.INFO, "".isBlank());
        log.log(Level.INFO, "\ntest\ntest".lines().collect(Collectors.toList()));
        log.log(Level.INFO, "a".repeat(10));
        log.log(Level.INFO, "   this  is    an    string      example   ".strip());
    }

}
