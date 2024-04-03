package com.posadskiy.design.pattern.creational.abstractfactory.factory;

import com.posadskiy.design.pattern.creational.abstractfactory.controller.Controller;
import com.posadskiy.design.pattern.creational.abstractfactory.sensor.Sensor;

public interface Factory {
    Controller createController();
    Sensor createSensor();
}
