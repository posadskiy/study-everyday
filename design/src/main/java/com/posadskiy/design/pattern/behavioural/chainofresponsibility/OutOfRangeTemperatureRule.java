package com.posadskiy.design.pattern.behavioural.chainofresponsibility;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class OutOfRangeTemperatureRule extends Rule {

    @Override
    public boolean check(Sensor sensor) {
        if (sensor.temperature() < 10 || sensor.temperature() > 35) {
            log.info("Temperature out of range");
            return false;
        }

        return this.checkNext(sensor);
    }
}
