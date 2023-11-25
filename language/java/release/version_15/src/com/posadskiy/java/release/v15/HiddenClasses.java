package com.posadskiy.java.release.v15;

import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.io.Serializable;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

/**
 * JEP 371: Hidden Classes
 * <a href="https://openjdk.org/jeps/371">Docs</a>
 * <a href="https://www.baeldung.com/java-hidden-classes">Cooking</a>
 */
@Log4j2
public class HiddenClasses {

    public static void main(String[] args) {
        log.info(Arrays.toString(getClassBytes()));
        try {
            var loadedClass = MethodHandles.lookup().defineHiddenClass(getClassBytes(), true).lookupClass();
            log.info(loadedClass.getName());
            // Returns public fields only
            log.info(Arrays.toString(loadedClass.getFields()));
            // Returns true
            log.info(loadedClass.isHidden());

            // Impossible
            // var instance = (User) loadedClass.getConstructor().newInstance();
            //log.log(Level.INFO, instance.a);
            //log.log(Level.INFO, instance.b);

            var instance = (Valuable) loadedClass.getConstructor().newInstance();
            log.info(instance.c);
        } catch (IllegalAccessException | InvocationTargetException | InstantiationException |
                 NoSuchMethodException e) {
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
