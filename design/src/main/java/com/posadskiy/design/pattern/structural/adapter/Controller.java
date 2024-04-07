package com.posadskiy.design.pattern.structural.adapter;

public class Controller {
    public double getTemperature() {
        return 20.0; // mocked reading from internal sensor
    }

    public double getHumidity() {
        return 50.0; // mocked reading from internal sensor
    }
    
    public void setMode(Mode mode) {
        // setting provided mode
    }
}
