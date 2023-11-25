package com.posadskiy.java.release.v9.features;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * JEP 193: Variable Handles
 * <a href="https://openjdk.org/jeps/193">Link to description</a>
 * <a
 * href="https://docs.oracle.com/javase/8/docs/api/java/lang/invoke/MethodHandles.html#collectArguments-java.lang.invoke.MethodHandle-int-java.lang.invoke.MethodHandle-">Class
 * doc</a>
 */
public class MethodHandling {

    public static void main(String[] args) {
        try {
            // Returns array with two methods - main and firstMethod
            MethodHandles.lookup().findClass("com.posadskiy.java.release.v9.features.MethodHandling").getDeclaredMethods();

            MethodHandles.dropArguments(MethodHandles.publicLookup()
                .findVirtual(MethodHandling.class, "firstMethod", MethodType.methodType(Integer.class, Integer.class)), 0);
            MethodHandles.dropArguments(MethodHandles.publicLookup()
                .findVirtual(MethodHandling.class, "firstMethod", MethodType.methodType(Integer.class, Integer.class)), 1);
        } catch (NoSuchMethodException | IllegalAccessException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        Logger.getAnonymousLogger().log(Level.INFO, String.valueOf(new MethodHandling().firstMethod(10)));
    }

    public Integer firstMethod(Integer a) {
        return a;
    }

}
