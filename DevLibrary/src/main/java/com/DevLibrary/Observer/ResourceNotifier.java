package com.DevLibrary.Observer;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ResourceNotifier {
    // Stores all registered observers.
    private List<ResourceObserver> observers = new ArrayList<>();

    public ResourceNotifier() {
        // Add the logger observer so it can receive and log events.
        observers.add(new ActivityLoggerObserver());
    }

    public void addObserver(ResourceObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(ResourceObserver observer) {
        observers.remove(observer);
    }
    // Send the event message to every observer so each one can react in its own way.
    public void notifyObservers(String message) {
        for (ResourceObserver observer : observers) {
            observer.update(message);
        }
    }
}