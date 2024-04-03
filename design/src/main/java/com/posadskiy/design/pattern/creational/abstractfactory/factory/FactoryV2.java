package com.posadskiy.design.pattern.creational.abstractfactory.factory;

import com.posadskiy.design.pattern.creational.abstractfactory.controller.Controller;
import com.posadskiy.design.pattern.creational.abstractfactory.controller.ControllerV2;
import com.posadskiy.design.pattern.creational.abstractfactory.sensor.Sensor;
import com.posadskiy.design.pattern.creational.abstractfactory.sensor.SensorV2;

public class FactoryV2 implements Factory {
    @Override
    public Controller createController() {
        return new ControllerV2();
    }

    @Override
    public Sensor createSensor() {
        return new SensorV2();
    }

}
