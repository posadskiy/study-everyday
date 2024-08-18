package com.posadskiy.design.pattern.behavioural.state;

public interface State {
    void turnOn();
    void turnOff();
    void setSetpoint(int setpoint);
}
