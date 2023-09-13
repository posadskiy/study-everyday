package com.posadskiy.design.solid;

public class InterfaceSegregationPrinciple {
    
}

interface Occupable {
    double occupancyRate();
}

class FiveFloorsEuropeanHotel implements Occupable {
    @Override
    public double occupancyRate() {
        return 0.5; // should be dynamic
    }
}
