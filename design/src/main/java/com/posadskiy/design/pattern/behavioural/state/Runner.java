package com.posadskiy.design.pattern.behavioural.state;

public class Runner {
    
    public static void main(String[] args) {
        var emptyState = new EmptyState();
        var occupiedState = new OccupiedState();
        var sleepingState = new SleepingState();
        
        var room = new Room(emptyState);
        room.turnOn();
        
        room.setState(occupiedState);
        room.turnOn();
        room.turnOff();
        
        room.setState(sleepingState);
        room.setSetpoint(25);
        room.turnOff();
    }
}
