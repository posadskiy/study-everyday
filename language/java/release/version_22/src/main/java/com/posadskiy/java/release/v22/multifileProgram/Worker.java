package com.posadskiy.java.release.v22.multifileProgram;

import java.util.logging.Logger;

/**
 * JEP 458: Launch Multi-File Source-Code Programs
 * <p>
 * Usage example: /Users/dmitrii/Library/Java/JavaVirtualMachines/corretto-22.0.2/Contents/Home/bin/java 
 * language/java/release/version_22/src/main/java/com/posadskiy/java/release/v22/multifileProgram/Worker.java
 * <p>
 * <a href="https://openjdk.org/jeps/458">Docs</a>
 */
public class Worker {
    public static void main(String[] args) {
        Logger.getAnonymousLogger().info(new RoomModule() + " : " + new Controller());
    }
}
