package com.posadskiy.java.release.v9.features;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * JEP 211: Elide Deprecation Warnings on Import Statements
 * <a href="https://openjdk.org/jeps/211">Doc</a>
 */
public class ElideDeprecationWarningsOnImportStatements {

    public static void main(String[] args) {
        deprecatedMethod(10);
    }

    @SuppressWarnings("unchecked")
    @Deprecated
    public static void deprecatedMethod(int a) {
        Logger.getAnonymousLogger().log(Level.INFO, "deprecated method log");
    }
}
