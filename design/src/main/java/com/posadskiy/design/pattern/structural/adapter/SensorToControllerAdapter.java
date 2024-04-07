package com.posadskiy.design.pattern.structural.adapter;

public class SensorToControllerAdapter extends Controller {
    private final Sensor sensor;
    
    public SensorToControllerAdapter(Sensor sensor) {
        this.sensor = sensor;
    }
    
    @Override
    public double getTemperature() {
        return sensor.getTemperature();
    }
    
    @Override
    public double getHumidity() {
        return sensor.getHumidity();
    }
    
    @Override
    public void setMode(Mode mode) {
        // sensor can't change mode, so just skipping
    }
}
