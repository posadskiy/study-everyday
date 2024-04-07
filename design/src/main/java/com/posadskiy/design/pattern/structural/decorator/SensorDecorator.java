package com.posadskiy.design.pattern.structural.decorator;

import java.util.Map;

public class SensorDecorator extends Sensor {
    protected Sensor sensor;
    
    public SensorDecorator(Sensor sensor) {
        this.sensor = sensor;
    }

    @Override
    public Map<String, String> getMeasurements() {
        return sensor.getMeasurements();
    }
}
