package com.posadskiy.design.pattern.behavioural.state;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class EmptyState implements State {

    @Override
    public void turnOn() {
        log.info("nobody in the room - skipping");
    }

    @Override
    public void turnOff() {
        log.info("turning off");
    }

    @Override
    public void setSetpoint(int setpoint) {
        log.info("nobody in the room - skipping");
    }
}
