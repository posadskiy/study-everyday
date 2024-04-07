package com.posadskiy.design.pattern.structural.decorator;

import java.util.Map;

public class PressureSensorDecorator extends SensorDecorator {

    public PressureSensorDecorator(Sensor sensor) {
        super(sensor);
    }

    @Override
    public Map<String, String> getMeasurements() {
        var measurements = super.getMeasurements();
        measurements.put("pressure", "55.0");
        return measurements;
    }
}
