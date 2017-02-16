package com.example.mojib.connection.Model;

/**
 * Created by mojib on 16.02.2017.
 */

public class MeasurementDeviceDescriptor {
    //mit mId = 0 werden alle MeasurementDevices einer SensorUnit angesprochen
    private int mId;
    private BODYPART mBODYPART;

    public enum BODYPART {
        UNDEFINED,
        LEFTWRIST,
        RIGHTWRIST,
        LEFTANKLE,
        RIGHTANKLE,
    }

    public MeasurementDeviceDescriptor(int id, BODYPART BODYPART) {
        mId = id;
        mBODYPART = BODYPART;
    }

    public int getId() {
        return mId;
    }

    public BODYPART getPosition() {
        return mBODYPART;
    }

    public void setPosition(BODYPART BODYPART) {
        mBODYPART = BODYPART;
    }
}
