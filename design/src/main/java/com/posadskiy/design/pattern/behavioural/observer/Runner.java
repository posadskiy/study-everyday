package com.posadskiy.design.pattern.behavioural.observer;

public class Runner {
    public static void main(String[] args) {
        var manager = new EventManager();
        manager.subscribe(EventType.UserInput, new SavingToDatabaseListener());
        manager.subscribe(EventType.UserInput, new PrintingToLogsListener());
        
        var event = new UserInputEvent("settings changed to 25C");
        manager.notify(EventType.UserInput, event);
    }
}
