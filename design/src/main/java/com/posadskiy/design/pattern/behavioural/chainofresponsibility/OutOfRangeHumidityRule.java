package com.posadskiy.design.pattern.behavioural.chainofresponsibility;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class OutOfRangeHumidityRule extends Rule {

    @Override
    public boolean check(Sensor sensor) {
        if (sensor.humidity() < 0 || sensor.humidity() > 100) {
            log.info("Humidity out of range");
            return false;
        }

        return this.checkNext(sensor);
    }
}
