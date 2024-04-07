package com.posadskiy.design.pattern.structural.adapter;

public class Sensor {
    public double getTemperature() {
        return 23.0; // mocked reading from internal sensor
    }

    public double getHumidity() {
        return 65.0; // mocked reading from internal sensor
    }
}
