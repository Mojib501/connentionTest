package com.example.mojib.connection.Model.DebugData;

import com.example.mojib.connection.Model.MeasurementDeviceDescriptor;
import com.example.mojib.connection.Model.SensorUnitDescriptor;

import java.util.ArrayList;

/**
 * Created by mojib on 16.02.2017.
 */

public class DebugData {
    private ArrayList<SensorUnitDescriptor> mDummySensors = new ArrayList<>();

    public DebugData() {
        mDummySensors.add(fakeSensorUnitDescriptor("Rev3-ID4","00:07:80:6E:65:B0"));

        mDummySensors.add(fakeSensorUnitDescriptor("Rev4_ID1", "00:07:80:0F:4B:17"));
        //mDummySensors.add(fakeSensorUnitDescriptor("Rev4_ID5", "00:07:80:0F:41:AC"));
        mDummySensors.add(fakeSensorUnitDescriptor("Rev4_ID8", "00:07:80:0F:5A:C4"));
        mDummySensors.add(fakeSensorUnitDescriptor("Rev4_ID10", "00:07:80:0F:5A:C2"));
        mDummySensors.add(fakeSensorUnitDescriptor("Rev4_ID11", "00:07:80:0F:5A:C6"));
        // mDummySensors.add(fakeSensorUnitDescriptor("Rev4_ID9", "00:07:80:0F:41:AB"));
        // mDummySensors.add(fakeSensorUnitDescriptor("Rev4_ID6", "00:07:80:0F:41:AA"));
        mDummySensors.add(fakeSensorUnitDescriptor("Rev4_ID7", "00:07:80:0F:5A:C3"));
        //mDummySensors.add(fakeSensorUnitDescriptor("Rev5_ID7", "00:07:80:2B:19:61"));
        mDummySensors.add(fakeSensorUnitDescriptor("Rev5_ID8", "00:07:80:2B:1B:2E"));
        mDummySensors.add(fakeSensorUnitDescriptor("Rev5_ID9", "00:07:80:2B:19:62"));
        //       mDummySensors.add(fakeSensorUnitDescriptor("Rev5_ID12", "00:07:80:2B:1B:31"));
        mDummySensors.add(fakeSensorUnitDescriptor("Rev5_ID15", "00:07:80:2B:1B:30"));
        mDummySensors.add(fakeSensorUnitDescriptor("Rev5_ID14", "00:07:80:2B:1B:32"));

    }

    private SensorUnitDescriptor fakeSensorUnitDescriptor(String name, String mac) {
        ArrayList<MeasurementDeviceDescriptor> mdList = new ArrayList<>();
        //der Sensoreinheit werden zunächst fakeDaten als Position übergeben,
        //da beim Instanzieren die Positionen noch nicht bekannt sind
        mdList.add(new MeasurementDeviceDescriptor(0,
                MeasurementDeviceDescriptor.BODYPART.UNDEFINED));
        return new SensorUnitDescriptor(mac,
                name,
                SensorUnitDescriptor.CONNECTIONSTATUS.NOT_CONNECTED,
                mdList);
    }
    public ArrayList<SensorUnitDescriptor> getDebugList() {
        return mDummySensors;
    }
}
