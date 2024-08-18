package com.posadskiy.design.pattern.behavioural.state;

import lombok.AllArgsConstructor;
import lombok.Setter;

@Setter
@AllArgsConstructor
public class Room {
    private State state;

    public void turnOn() {
        state.turnOn();
    }

    public void turnOff() {
        state.turnOff();
    }

    public void setSetpoint(int setpoint) {
        state.setSetpoint(setpoint);
    }
}
