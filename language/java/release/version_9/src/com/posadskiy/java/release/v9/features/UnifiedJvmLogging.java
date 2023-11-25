package com.posadskiy.java.release.v9.features;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * JEP 158: Unified JVM Logging
 * <a href="https://openjdk.org/jeps/158">Link</a>
 */
public class UnifiedJvmLogging {

    public static void main(String[] args) {
        Logger.getGlobal().log(Level.INFO, "Some output");
        Logger.getGlobal().log(Level.WARNING, "Some another output");
        Logger.getGlobal().log(Level.SEVERE, "More output");
    }

}
