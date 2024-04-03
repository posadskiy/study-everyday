package com.posadskiy.design.pattern.creational.abstractfactory.factory;

import com.posadskiy.design.pattern.creational.abstractfactory.controller.Controller;
import com.posadskiy.design.pattern.creational.abstractfactory.controller.ControllerV3;
import com.posadskiy.design.pattern.creational.abstractfactory.sensor.Sensor;
import com.posadskiy.design.pattern.creational.abstractfactory.sensor.SensorV3;

public class FactoryV3 implements Factory {
    @Override
    public Controller createController() {
        return new ControllerV3();
    }

    @Override
    public Sensor createSensor() {
        return new SensorV3();
    }

}
