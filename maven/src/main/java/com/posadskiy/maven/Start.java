package com.posadskiy.maven;

public class Start {

    public static void main(String[] args) {
        System.out.println("Starting...");
        Counter counter = new Counter();
        System.out.println(counter.printCount());
        System.out.println("Finished");
    }

}
