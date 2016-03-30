package model;

import controller.Observer;

import java.util.ArrayList;

public abstract class Observable {
    ArrayList<Observer> subscribers;

    public Observable() {
        subscribers = new ArrayList<>();
    }

    public void attach(Observer observer) {
        subscribers.add(observer);
    }

    public void detach(Observer observer) {
        if (subscribers.contains(observer)) {
            subscribers.remove(observer);
        }
    }

    public void notifyObservers() {
        System.out.println("Notifying observers\n");
        subscribers.forEach(Observer::update);
    }

    public void notifyObservers2() {
        System.out.println("Notifying observers\n");
        subscribers.forEach(Observer::update2);
    }
}
