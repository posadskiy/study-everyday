package com.posadskiy.design.pattern.creational.abstractfactory.factory;

import com.posadskiy.design.pattern.creational.abstractfactory.controller.Controller;
import com.posadskiy.design.pattern.creational.abstractfactory.controller.ControllerV1;
import com.posadskiy.design.pattern.creational.abstractfactory.sensor.Sensor;
import com.posadskiy.design.pattern.creational.abstractfactory.sensor.SensorV1;

public class FactoryV1 implements Factory {
    @Override
    public Controller createController() {
        return new ControllerV1();
    }

    @Override
    public Sensor createSensor() {
        return new SensorV1();
    }

}
