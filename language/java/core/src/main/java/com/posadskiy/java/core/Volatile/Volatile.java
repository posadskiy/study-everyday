package com.posadskiy.java.core.Volatile;

import java.util.logging.Logger;

public class Volatile {
    private static int number;
    private static boolean isReady;
    
    private static class Reader extends Thread {
        private final Logger logger = Logger.getGlobal();
        
        @Override
        public void run() {
            while (!isReady) {
                Thread.yield();
            }
            
            logger.info("Number: " + number);
        }
    }

    public static void main(String[] args) {
        new Reader().start();
        number = 20;
        isReady = true;
    }
}
