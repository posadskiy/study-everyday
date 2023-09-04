import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.util.List;

/**
 * JEP 286: Local-Variable Type Inference
 * <a href="https://openjdk.org/jeps/286">Docs</a>
 */
public class LocalVariableTypeInference {

    private final static Logger log = System.getLogger("default");

    public static void main(String[] args) {
        var value = List.of(1, 2, 3);

        log.log(Level.INFO, value.size());
    }

}
