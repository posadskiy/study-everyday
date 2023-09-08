package com.posadskiy.language.java.core.concurrency;

public class Synchronized {
    private final static Object mutex = new Object();

    /**
     * Можно добавить в сигнатуру метода лексему synchronized или обернуть код, который должен выполняться синхронно в
     * блок synchronized. В первом случае синхронизация устанавливается на метод, как на объект. Во втором, на объект
     * mutex, который должен быть объявлен для всех потоков.
     *
     * @param value значение для вывода на консоль
     */
    private static void printValue(String value) {
        synchronized (mutex) {
            for (int i = 0; i < 10; ++i) {
                System.out.println(value);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private synchronized static void printOneValue(int value) {
        if (value == 10) return;

        System.out.println(value);
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        printOneValue(value + 1);
    }

    public static void main(String[] args) {
        int currentCase = 1;
        if (currentCase == 0) {
            Runnable threadOne = () -> Synchronized.printValue("1");
            Runnable threadTwo = () -> Synchronized.printValue("2");

            new Thread(threadOne).start();
            new Thread(threadTwo).start();
        }

        if (currentCase == 1) {
            Runnable threadOne = () -> Synchronized.printOneValue(0);
            Runnable threadTwo = () -> Synchronized.printOneValue(0);

            new Thread(threadOne).start();
            new Thread(threadTwo).start();
        }
    }
}
