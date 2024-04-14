package com.posadskiy.design.pattern.structural.proxy;

public class SimpleGuy implements Guy {
    public boolean spend(double purchaseValue) {
        return true;
    }

    public boolean earn(double earnedValue) {
        return true;
    }
}
