import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LocalVariableSyntaxForLambdaParameters {

    private final static Logger log = System.getLogger("default");

    public static void main(String[] args) {
        var values = Stream.of(1, 2, 3)
            .map((@NotNull var value) -> value + 1)
            .peek((@NotNull var value) -> log.log(Level.INFO, value))
            .collect(Collectors.toList());
    }

}
