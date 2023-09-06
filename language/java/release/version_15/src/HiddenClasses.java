import java.io.IOException;
import java.io.Serializable;
import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

/**
 * JEP 371: Hidden Classes
 * <a href="https://openjdk.org/jeps/371">Docs</a>
 * <a href="https://www.baeldung.com/java-hidden-classes">Cooking</a>
 */
public class HiddenClasses {

    private final static Logger log = System.getLogger("default");


    public static void main(String[] args) {
        log.log(Level.INFO, Arrays.toString(getClassBytes()));
        try {
            var loadedClass = MethodHandles.lookup().defineHiddenClass(getClassBytes(), true).lookupClass();
            log.log(Level.INFO, loadedClass.getName());
            // Returns public fields only
            log.log(Level.INFO, Arrays.toString(loadedClass.getFields()));
            // Returns true
            log.log(Level.INFO, loadedClass.isHidden());

            // Impossible
            // var instance = (User) loadedClass.getConstructor().newInstance();
            //log.log(Level.INFO, instance.a);
            //log.log(Level.INFO, instance.b);

            var instance = (Valuable) loadedClass.getConstructor().newInstance();
            log.log(Level.INFO, instance.c);
        } catch (IllegalAccessException | InvocationTargetException | InstantiationException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    private static byte[] getClassBytes() {
        final Class<User> userClass = User.class;
        try {
            return userClass.getClassLoader().getResourceAsStream(userClass.getName().replace('.', '/') + ".class").readAllBytes();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}

class User implements Serializable, Valuable {

    public int a;
    int b;

    public User() {
    }
}

interface Valuable {

    int c = 5;
}
