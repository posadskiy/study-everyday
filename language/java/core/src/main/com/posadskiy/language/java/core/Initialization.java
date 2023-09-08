package com.posadskiy.language.java.core;

class Initialization {
    private String whereInit = "Field init";
    private int value;

    {
        System.out.println("Block initialization");
        System.out.println(whereInit);
        this.whereInit = "Block init";
        System.out.println(whereInit);
    }

    public void Initialization() {
        System.out.println("Constructor initialization");
        System.out.println(whereInit);
        this.whereInit = "Constructor init";
        System.out.println(whereInit);
    }

    public void checkValue() {
        System.out.println(value);

        int localValue;
        // System.out.println(localValue); NOT COMPILE
    }

    public static void main(String[] args) {
        Initialization init = new Initialization();
    }
}
