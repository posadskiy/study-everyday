package com.posadskiy.design.pattern.creational.abstractfactory;

import com.posadskiy.design.pattern.creational.abstractfactory.factory.Factory;
import com.posadskiy.design.pattern.creational.abstractfactory.factory.FactoryV1;
import com.posadskiy.design.pattern.creational.abstractfactory.factory.FactoryV2;
import com.posadskiy.design.pattern.creational.abstractfactory.factory.FactoryV3;

public class AbstractFactory {
    public static Factory getFactory(String type) {
        return switch (type) {
            case "V1" -> new FactoryV1();
            case "V2" -> new FactoryV2();
            case "V3" -> new FactoryV3();
            default -> throw new IllegalArgumentException("Unknown factory type: " + type);
        };
    }
}
