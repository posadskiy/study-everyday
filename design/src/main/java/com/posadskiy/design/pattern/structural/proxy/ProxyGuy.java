package com.posadskiy.design.pattern.structural.proxy;

public class ProxyGuy implements Guy {
    private final SimpleGuy simpleGuy;
    private Double balance = 0.0;

    public ProxyGuy(SimpleGuy simpleGuy) {
        this.simpleGuy = simpleGuy;
    }


    @Override
    public boolean spend(double purchaseValue) {
        if (balance - purchaseValue < 100.0) {
            return false;
        }

        balance = balance - purchaseValue;
        simpleGuy.spend(purchaseValue);
        return true;
    }

    @Override
    public boolean earn(double earnedValue) {
        // making some money
        balance = balance + earnedValue;
        simpleGuy.earn(earnedValue);

        return true;
    }
}
