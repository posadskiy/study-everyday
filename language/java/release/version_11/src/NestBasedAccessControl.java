import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * JEP 181: Nest-Based Access Control
 * <p>
 * Introduce nests, an access-control context that aligns with the existing notion of nested types in the Java programming language. Nests
 * allow classes that are logically part of the same code entity, but which are compiled to distinct class files, to access each other's
 * private members without the need for compilers to insert accessibility-broadening bridge methods.
 * <p>
 * <a href="https://openjdk.org/jeps/181">Docs</a>
 */
public class NestBasedAccessControl {

    private final static Logger log = System.getLogger("default");

    public static void main(String[] args) {
        var collect = Arrays.stream(NestBasedAccessControl.class.getNestMembers())
            .map(Class::getName)
            .collect(Collectors.toList());
        log.log(Level.INFO, collect);
    }

    class First {

    }

    class Second {

    }

    class Third {

    }

}
