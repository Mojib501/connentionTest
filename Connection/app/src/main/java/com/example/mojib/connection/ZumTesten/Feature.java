package com.example.mojib.connection.ZumTesten;

import com.example.mojib.connection.Model.Measurement;
import com.example.mojib.connection.Model.QueueIdentifier;
import com.example.mojib.connection.ObserverPattern.IFeature;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import de.hsulm.cermit.exchangableinterface.IDeviceDescriptor;

/**
 * Created by mojib on 14.02.2017.
 */

public class Feature implements IFeature {
    private final int deltaTimeStamp;
    private List<QueueIdentifier> qIdList = new LinkedList<>();
    private final double overlabFactor;
    private HashMap<QueueIdentifier, Queue<Measurement>> messageMap;
    private Queue<Measurement> queue;
    private Iterator<Measurement> iter;
    private final IDeviceDescriptor desc;
    private final int mdId;

    public Feature(IDeviceDescriptor desc, int deltaTimeStamp, int mdId, double overlabFactor) {
        this.desc = desc;
        this.deltaTimeStamp = deltaTimeStamp;
        this.mdId = mdId;
        this.overlabFactor = overlabFactor;
    }

    @Override
    public void update(HashMap<QueueIdentifier, Queue<Measurement>> messageMap) {

    }

    @Override
    public List<QueueIdentifier> getQIdList() {
        return null;
    }

    @Override
    public double getOverlabFactor() {
        return 0;
    }

    @Override
    public int getDeltaTimeStamp() {
        return 0;
    }
}
