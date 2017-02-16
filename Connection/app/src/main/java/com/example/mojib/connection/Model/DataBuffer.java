package com.example.mojib.connection.Model;

import com.example.mojib.connection.ObserverPattern.IFeature;
import com.example.mojib.connection.ObserverPattern.Subject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import de.hsulm.cermit.eventinterface.IDataObserver;
import de.hsulm.cermit.exchangableinterface.IDeviceDescriptor;
import de.hsulm.cermit.messages.DataMessage;
import de.hsulm.cermit.messages.DataMessageSingle;
import de.hsulm.cermit.messages.IDataMessageHelper;

/**
 * Created by mojib on 14.02.2017.
 */

public class DataBuffer extends Subject implements IDataObserver {
    /**
     * @param args the command line arguments
     */
    HashMap<IFeature,HashMap<QueueIdentifier,Queue<Measurement>>> queueMap = new LinkedHashMap<>();

    @Override
    public void register(IFeature observer) {
        //pro observer/Feature eine map mit key=qid und value=queue
        queueMap.put(observer, controlDevice(observer.getQIdList()));
    }
    @Override
    public void unregister(IFeature observer) {
        queueMap.remove(observer);
    }//HashMap<IFeatureObserver,HashMap<QueueIdentifier,Queue<Measurement>>> map, IFeatureObserver observer
    @Override
    public void notifyObserver(IFeature observer){
        //bestimmten observer benachtichtigen und liste übergeben
        observer.update(queueMap.get(observer));
        //wenn nachricht versendet wurde dann aus dem buffer löschen

    }
    @Override
    public void notifyAllObserver() {
        for(Map.Entry e: queueMap.entrySet()){
            //alle observer benachrichtigen
        }
    }
    @Override
    public void update(DataMessage dataMessage){
        dataMessage.processDataMessage(new IDataMessageHelper() {
            // jedes mal wenn eine neues device connected wird eine neue Queue
            // für dieses speziele Device erzeugt.
            @Override
            public void processMessage(DataMessageSingle singleMsg){
                controlMessage(singleMsg);
            }
        });
    }
    //sortiert die messdaten zu den jeweiligen features und füllt die queue
    private void controlMessage(DataMessageSingle singleMsg){
        IDeviceDescriptor desc = singleMsg.getSensorUnit().getDeviceDescriptor();
        int mdId = singleMsg.getMeasurementDeviceID();
        long timeStamp = singleMsg.getTime();
        QueueIdentifier qId = new QueueIdentifier(desc, mdId);
        //äussere Map
        for(Map.Entry<IFeature,HashMap<QueueIdentifier,Queue<Measurement>>> map:queueMap.entrySet()){
            //innere Map
            //Measurement zu der jeweiligen qId erstellen
            Measurement m = new Measurement(singleMsg, timeStamp, mdId);
            Queue<Measurement> queue = map.getValue().get(qId);
            queue.offer(m);
            controlBuffer(map.getKey(),map.getValue().get(qId));
        }
    }
    private void controlBuffer(IFeature feature,Queue<Measurement> queue){
//        int deltaTime = feature.getDeltaTimeStamp();
//        //if(deltaTime <= (getLastElement(queue).getTimeStamp()-getFirstElement(queue).getTimeStamp()))
//        if(deltaTime <  queue.size()){
//        notifyObserver(feature);
//        //überlappungs algorithmus
//        //noch zu überarbeiten!!!
//        int cutBorder = queue.size()*feature.getDeltaTimeStamp();
//            while(cutBorder > queue.size()){
//            queue.remove();
//            }
//        }
        notifyObserver(feature);
    }
    public Measurement getFirstElement(Queue<Measurement> queue){
        return queue.peek();
    }
    public Measurement getLastElement(Queue<Measurement> queue){
        Iterator<Measurement> iterator = queue.iterator();
        Measurement lastElement=null;
        while(iterator.hasNext()){
            lastElement = iterator.next();
        }
        return lastElement;
    }
    //erzeugt eine map mit key= qId , value = Queue
    private HashMap<QueueIdentifier,Queue<Measurement>> controlDevice(List<QueueIdentifier> list){

        HashMap<QueueIdentifier,Queue<Measurement>> map = new HashMap<QueueIdentifier,Queue<Measurement>>();
        for(QueueIdentifier e : list){
            map.put(e, new LinkedList<Measurement>());
        }
        return map;
    }

}
