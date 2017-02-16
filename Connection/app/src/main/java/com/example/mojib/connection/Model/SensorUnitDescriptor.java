package com.example.mojib.connection.Model;

import java.util.List;

import de.hsulm.cermit.exchangableinterface.IDeviceDescriptor;
import de.hsulm.cermit.implementations.BluetoothDeviceDescriptor;
/**
 * enthält die Beschreibung der Sensoreinheiten, die per Bluetooth verbunden werden
 * erbt vom BluetoothDeviceDescriptor, da diese schon einige Infos über die SensorUnits enthält
 *
 * @author Peter Fischer, Vanessa W.
 */
public class SensorUnitDescriptor extends BluetoothDeviceDescriptor implements IDeviceDescriptor {

    private CONNECTIONSTATUS mConnectionStatus;
    private List<MeasurementDeviceDescriptor> mMeasurementDeviceDescriptors;


    public enum CONNECTIONSTATUS {
        NOT_CONNECTED,
        FOUND,
        CONNECTED,
        MEASURING,
        UNKNOWN
    }

    public SensorUnitDescriptor(String macAdress,
                                String friendlyName, CONNECTIONSTATUS connectionStatus,
                                List<MeasurementDeviceDescriptor> measurementDeviceDescriptors) {
        super(macAdress, friendlyName);
        mConnectionStatus = connectionStatus;
    }

    public CONNECTIONSTATUS getConnectionStatus() {
        return mConnectionStatus;
    }

    public void setConnectionStatus(CONNECTIONSTATUS connectionStatus) {
        mConnectionStatus = connectionStatus;
    }
    public List<MeasurementDeviceDescriptor> getMeasurementDeviceDescriptors() {
        return mMeasurementDeviceDescriptors;
    }

    @Override
    public String toString() {
        return super.getName();
    }

}
