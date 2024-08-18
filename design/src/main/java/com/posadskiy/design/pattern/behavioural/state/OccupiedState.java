package com.posadskiy.design.pattern.behavioural.state;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class OccupiedState implements State {
    @Override
    public void turnOn() {
        log.info("Turning on");
    }

    @Override
    public void turnOff() {
        log.info("Turning off");
    }

    @Override
    public void setSetpoint(int setpoint) {
        log.info("Set point: {}", setpoint);
    }
}
