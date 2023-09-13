package com.posadskiy.design.solid;

public class OpenClosePrinciple {
}

class ConsumptionCalculator {
    public Consumption calculate(Double kWh, Integer hours) {
        return new Consumption(kWh * hours);
    }
}

class ConsumptionOccupancyRateCalculator extends ConsumptionCalculator {
    @Override
    public Consumption calculate(Double kWh, Integer hours) {
        return new Consumption(kWh * hours * OccupancyRateService.getOccupancyRate());
    }
}

class OccupancyRateService {
    public static Double getOccupancyRate() {
        return 0.75; // Should be dynamic ofc
    }
}

record Consumption (Double value) {
}
