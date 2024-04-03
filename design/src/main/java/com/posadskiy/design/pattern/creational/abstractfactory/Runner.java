package com.posadskiy.design.pattern.creational.abstractfactory;

import java.util.logging.Logger;

public class Runner {
    public static final Logger logger = Logger.getGlobal();

    public static void main(String[] args) {
        var factory = AbstractFactory.getFactory("V2");
        
        var controller = factory.createController();
        var sensor = factory.createSensor();

        logger.info(controller.getVersion());
        logger.info(sensor.getVersion());
    }
}
