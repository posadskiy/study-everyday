package com.posadskiy.design.pattern.structural.decorator;

import java.util.Map;

public class TemperatureSensorDecorator extends SensorDecorator {

    public TemperatureSensorDecorator(Sensor sensor) {
        super(sensor);
    }

    @Override
    public Map<String, String> getMeasurements() {
        var measurements = super.getMeasurements();
        measurements.put("temperature", "25.0");
        return measurements;
    }
}
