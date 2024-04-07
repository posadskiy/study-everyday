package com.posadskiy.design.pattern.structural.decorator;

import java.util.Map;

public class HumiditySensorDecorator extends SensorDecorator {

    public HumiditySensorDecorator(Sensor sensor) {
        super(sensor);
    }

    @Override
    public Map<String, String> getMeasurements() {
        var measurements = super.getMeasurements();
        measurements.put("humidity", "55.0");
        return measurements;
    }
}
