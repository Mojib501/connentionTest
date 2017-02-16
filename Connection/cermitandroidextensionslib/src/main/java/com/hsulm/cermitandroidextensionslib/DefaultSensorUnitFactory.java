package com.hsulm.cermitandroidextensionslib;

import android.content.Context;

import java.io.IOException;

import de.hsulm.cermit.commandinterface.CermitException;
import de.hsulm.cermit.commandinterface.Controller;
import de.hsulm.cermit.exchangableinterface.IDeviceDescriptor;
import de.hsulm.cermit.exchangableinterface.ISensorUnitFactory;
import de.hsulm.cermit.implementations.BluetoothDeviceDescriptor;
import de.hsulm.cermit.sensorunitcommunication.Connection;
import de.hsulm.cermit.eventinterface.IStateChangedCallback;
import de.hsulm.cermit.commandinterface.SensorUnit;
import de.hsulm.cermit.eventinterface.ICtrlObserver;
import de.hsulm.cermit.eventinterface.IDataObserver;


/**
 * Represents a factory able to instantiate a SensorUnit that
 *   - is a Bluetooth device able to use SPP connections
 *   - uses the BackdoorCommProtocol
 *
 * Implemented as an eagerly initialized Singleton
 *      Reasons:
 *                  - Only one instance needed;
 *                  - Cost resulting from eager initialization is not relevant here
 *                  - Thread safety (Though it probably won't be accessed by more than one thread))
 *
 * @author Peter Fischer
 */
public class DefaultSensorUnitFactory implements ISensorUnitFactory {
    private static DefaultSensorUnitFactory uniqueInstance = new DefaultSensorUnitFactory(); //eagerly initialized singleton (see class description)
    private Context context;
    private DefaultSensorUnitFactory(){
        // SINGLETON => No instantiation using the constructor from the outside
    }

    public static DefaultSensorUnitFactory getInstance(){
        return uniqueInstance;
    }

    /**
     * Creates an instance of the Sensor Unit from a device descriptor
     * @param iDeviceDescriptor describes the physical device that shall be represented by the
     *                         created Sensor Unit object
     * @param iStateChangedCallback callback that is used to inform the client application about
     *                             state changes
     * @return the newly created Sensor Unit
     */
    @Override
    public SensorUnit produce(IDeviceDescriptor iDeviceDescriptor, IStateChangedCallback iStateChangedCallback, ICtrlObserver iCtrlObserver, IDataObserver iDataObserver) {

        if(context== null) throw new RuntimeException("The context has to be set first before " +
                "DefaultSensorUnitFactory.produce(IDeviceDescriptor deviceDescriptor) can be called");

        BluetoothDeviceDescriptor androidBluetoothDeviceDescriptor;
        try{
            androidBluetoothDeviceDescriptor = (BluetoothDeviceDescriptor) iDeviceDescriptor;
        }
        catch (ClassCastException e)
        {
            throw new IllegalArgumentException("The passed deviceDescriptor is not of class " +
                    "BluetoothDeviceDescriptor. This class only supports Bluetooth devices.");
        }
        Connection connection;
        try {
            connection = new SppSocketAndroid(androidBluetoothDeviceDescriptor.getMAC_Address(), context);
        } catch (IOException e) {
            Controller.getInstance().getErrorCallback().handleException( new CermitException(iDeviceDescriptor,
                    "Could not establish a connection to "+androidBluetoothDeviceDescriptor.getName(),e));
            return null;
        }

        return new SensorUnit(connection, androidBluetoothDeviceDescriptor.getName(), iDeviceDescriptor,
                iStateChangedCallback, iCtrlObserver, iDataObserver);
    }


    public void setContext(Context context) {
        this.context = context;
    }
}
