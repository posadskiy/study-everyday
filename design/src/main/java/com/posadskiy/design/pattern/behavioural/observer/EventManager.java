package com.posadskiy.design.pattern.behavioural.observer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventManager {
    private final Map<EventType, List<Listener>> listeners = new HashMap<>();

    public void subscribe(EventType eventType, Listener listener) {
        listeners.computeIfAbsent(eventType, (key) -> new ArrayList<>()).add(listener);
    }

    public void unsubscribe(EventType type, Listener listener) {
        listeners.computeIfAbsent(type, (key) -> new ArrayList<>()).remove(listener);
    }

    public void notify(EventType eventType, Event event) {
        listeners.get(eventType).forEach(listener -> listener.update(event));
    }
}
