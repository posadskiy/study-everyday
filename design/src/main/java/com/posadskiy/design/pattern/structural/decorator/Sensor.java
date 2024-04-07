package com.posadskiy.design.pattern.structural.decorator;

import java.util.HashMap;
import java.util.Map;

public class Sensor {
    public static final Map<String, String> measurements = new HashMap<>();

    public Map<String, String> getMeasurements() {
        return measurements;
    }
}
