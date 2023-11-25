package com.posadskiy.java.release.v9.features;

import java.lang.System.Logger.Level;

/**
 * JEP 264: Platform Logging API and Service Define a minimal logging API which platform classes can use to log messages, together with a
 * service interface for consumers of those messages. A library or application can provide an implementation of this service in order to
 * route platform log messages to the logging framework of its choice. If no implementation is provided then a default implementation based
 * upon the java.util.logging API is used.
 * <a href="https://openjdk.org/jeps/264">Doc</a>
 */
public class PlatformLoggingApiAndService {

    public static void main(String[] args) {
        System.getLogger("wrong_name").log(Level.INFO, System.getLogger("default").getName());
    }

}
