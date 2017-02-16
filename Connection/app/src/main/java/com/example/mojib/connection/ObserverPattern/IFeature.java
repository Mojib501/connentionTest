package com.example.mojib.connection.ObserverPattern;

import com.example.mojib.connection.Model.Measurement;
import com.example.mojib.connection.Model.QueueIdentifier;

import java.util.HashMap;
import java.util.List;
import java.util.Queue;

/**
 * Created by mojib on 14.02.2017.
 */

public interface IFeature {
    public abstract void update(HashMap<QueueIdentifier, Queue<Measurement>> messageMap);
    public abstract List<QueueIdentifier> getQIdList();
    public abstract double getOverlabFactor();
    public abstract int getDeltaTimeStamp();
    //public abstract Channel getChannel();
    //public abstract void calc();
}
