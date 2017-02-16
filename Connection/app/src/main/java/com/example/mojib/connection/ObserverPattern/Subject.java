package com.example.mojib.connection.ObserverPattern;

/**
 * Created by mojib on 14.02.2017.
 */

public abstract class Subject {
    public abstract void register(IFeature observer);
    public abstract void unregister(IFeature observer);
    public abstract void notifyObserver(IFeature observer);
    public abstract void notifyAllObserver();
}
