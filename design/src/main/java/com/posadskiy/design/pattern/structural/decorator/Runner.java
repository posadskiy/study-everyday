package com.posadskiy.design.pattern.structural.decorator;

import java.util.logging.Logger;

public class Runner {
    public static final Logger logger = Logger.getGlobal();

    public static void main(String[] args) {
        var sensor = new Sensor();
        
        var decoratedSensor = new PressureSensorDecorator(new HumiditySensorDecorator(new TemperatureSensorDecorator(sensor)));
        
        logger.info(decoratedSensor.getMeasurements().toString());
    }
}
