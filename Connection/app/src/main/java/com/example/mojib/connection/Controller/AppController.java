package com.example.mojib.connection.Controller;

import android.util.Log;

import com.example.mojib.connection.Model.Constants;
import com.example.mojib.connection.Model.DataBuffer;
import com.example.mojib.connection.ObserverPattern.IFeature;
import com.hsulm.cermitandroidextensionslib.AndroidBluetoothDeviceSearcher;
import com.hsulm.cermitandroidextensionslib.DefaultSensorUnitFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import de.hsulm.cermit.commandinterface.CermitException;
import de.hsulm.cermit.commandinterface.Controller;
import de.hsulm.cermit.commandinterface.MeasurementType;
import de.hsulm.cermit.commandinterface.SensorUnit;
import de.hsulm.cermit.datastorage.DataStorer;
import de.hsulm.cermit.eventinterface.ICtrlObserver;
import de.hsulm.cermit.eventinterface.IDeviceSearchCallback;
import de.hsulm.cermit.eventinterface.IErrorCallback;
import de.hsulm.cermit.eventinterface.IStateChangedCallback;
import de.hsulm.cermit.exchangableinterface.IDeviceDescriptor;
import de.hsulm.cermit.implementations.DataToCsvFile;
import de.hsulm.cermit.implementations.ProtocolConstants;
import de.hsulm.cermit.messages.ControlMessage;
import de.hsulm.cermit.messages.EvtBatteryState;
import de.hsulm.cermit.messages.ResponseMessage;
import de.hsulm.cermit.messages.RspCompleteCfg;
import de.hsulm.cermit.sensorunitmodel.MeasurementDevice;
import de.hsulm.cermit.sensorunitmodel.StateSensorUnit;

/**
 * Created by mojib on 14.02.2017.
 */

public class AppController {
    DataBuffer dataBuffer = new DataBuffer();
    private static final String TAG = AppController.class.getSimpleName();
    private Controller cermitController = Controller.getInstance();


        // instantiate callbacks as anonymous class implementations
//------------------------------------------------------------------------------------------------//
// CERMIT CallBack´s
//------------------------------------------------------------------------------------------------//

        private IErrorCallback errorCallback = new IErrorCallback() {
            public void handleException(CermitException cermitException) {
                System.out.println("Error: " + cermitException.toString());
            }

            public void handleResponseTimeout(SensorUnit sensorUnit, int messageTypeID, byte[] msg) {
                System.out.println("Response Timeout for message id "+messageTypeID+" from SU: "+sensorUnit);
            }

            public void handleResponseError(SensorUnit sensorUnit, ResponseMessage responseMsg) {
                System.out.println("Response error: "+responseMsg +" from SU: "+sensorUnit);
            }
        };

        private IDeviceSearchCallback devSearchCallback = new IDeviceSearchCallback() {
            @Override
            public void receive(List<IDeviceDescriptor> deviceDescriptors) {
                //shows the number of found Devices
               final String deviceFind = String.format("Device search completed, found %d devices:",
                        deviceDescriptors.size());
                //DefaultSensorunitFactory
                connect(deviceDescriptors);
                for (SensorUnit unit : cermitController.getSensorUnits()) {
                    if (unit != null) {
                        Log.d(TAG, "Connected: " + unit.getName());
                        // open connection --> create SPPSocket
                        unit.open();
                        unit.registerCtrlObserver(ctrlObserver);
                    } else {
                        Log.d(TAG, "Unit is null. Could not connect.");

                    }
                }
                    Log.d(TAG, "Connected " + cermitController.getSensorUnits().size()
                            + " device(s).");

                    sleep(1000);

                    for (SensorUnit unit : cermitController.getSensorUnits()) {
                    if(!unit.getStateMachineClone().getState().equals(StateSensorUnit.IDLE)) {
                        unit.cmdAcceptCfg(0);
                        }
                    }
            }
            private void sleep(int tInMs) {
                try {
                    Thread.sleep(tInMs);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        private ICtrlObserver ctrlObserver = new ICtrlObserver() {
            @Override
            public void update(ControlMessage controlMessage) {
                if (controlMessage instanceof RspCompleteCfg) {
                    Log.d(TAG, "Response Config Completed");
                }
                if (controlMessage instanceof EvtBatteryState) {
                    Log.d(TAG, "Battery Status changed to:" + controlMessage);
                }
            }
        };

         private IStateChangedCallback stateChangedCallback = new IStateChangedCallback() {
            @Override
            public void onStateChanged(StateSensorUnit newStateSensorUnit,
                                       StateSensorUnit OldstateSensorUnit,
                                       SensorUnit sensorUnit) {
                Log.d(TAG, "New state: "
                        + newStateSensorUnit.name()
                        + "- state 1: "
                        + OldstateSensorUnit.name()
                        + "(" + sensorUnit.getName() + ")");
                //sendBroadcast(Constants.ACTION_STRING_UPDATE_TEXTVIEW, null);
            }
        };

//    //Implementieren der Cermit-Speicherfunktion (DataToCSVFile)
//    ArrayList<Class<? extends DataStorer>> dataStorerClasses = new ArrayList<>();
//    dataStorerClasses.add(DataToCsvFile.class);
//
//    String sessionPath = Constants.STORAGE_PATH + "/MeasurementData";
//    File sessionDir = new File(sessionPath);
//    if (!sessionDir.exists()) {
//        if (!sessionDir.mkdirs())
//            Log.e(TAG, "failed to make session directory");
//    }
    public SensorUnit sensorUnit;
//------------------------------------------------------------------------------------------------//
// Override´s
//------------------------------------------------------------------------------------------------//


//------------------------------------------------------------------------------------------------//
// Method´s
//------------------------------------------------------------------------------------------------//
    public void setup(){
        ArrayList<Class<? extends DataStorer>> dataStorerClasses = new ArrayList<>();
        dataStorerClasses.add(DataToCsvFile.class);
        String sessionPath = Constants.STORAGE_PATH + "/MeasurementData";
        File sessionDir = new File(sessionPath);
        if (!sessionDir.exists()) {
            if (!sessionDir.mkdirs())
                Log.e(TAG, "failed to make session directory");
        }
        cermitController.setUp(AndroidBluetoothDeviceSearcher.class, dataStorerClasses, sessionPath,
                errorCallback, devSearchCallback, new ProtocolConstants());
    }

    void startMeasuring(){
        List<SensorUnit> sensorUnits = cermitController.getSensorUnits();
        cermitController.cmdStartMeasurement(sensorUnits, MeasurementType.ONLINE, 0);


        List<MeasurementDevice> mdList = sensorUnits.get(0).getMeasurementDeviceClones();
        MeasurementDevice md = mdList.get(0);
        //md.setSamplingRateHz(50);
        //sensorUnits.get(0).cmdWriteMeasurementDeviceCfg(md);
    }
    void disconnect(){
        cermitController.disconnectSensorUnit(sensorUnit);
    }

    void stopMeasuring() {
        cermitController.cmdStopMeasurement();
    }
    public void registerFeature(IFeature feature){
        //TODO: registriere Feature im data buffer. Dazu wird benötigt:
        // Sensor ID (SU ID oder Device Descriptor) und die MD ID --> Queue Identifier
        dataBuffer.register(feature);
    }

    public SensorUnit connect(IDeviceDescriptor descriptor) {
        return cermitController.connect(descriptor,
                DefaultSensorUnitFactory.getInstance(),
                stateChangedCallback,
                ctrlObserver,
                dataBuffer);
    }
    public void connect(List<IDeviceDescriptor> descriptors) {
        for (IDeviceDescriptor descriptor : descriptors) {
            connect(descriptor);
        }
    }
    public IDeviceSearchCallback getDeviceSearcheCallback(){
        return devSearchCallback;
    }
}