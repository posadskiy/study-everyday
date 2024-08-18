package com.posadskiy.design.pattern.behavioural.state;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class SleepingState implements State {
    @Override
    public void turnOn() {
        log.info("Turning on if setpoint in the range");
    }

    @Override
    public void turnOff() {
        log.info("Turning off if setpoint in the comfort for sleeping range");
    }

    @Override
    public void setSetpoint(int setpoint) {
        log.info("Setting setpoint to {}, only for setpoint in the range, otherwise - skip", setpoint);
    }
}
