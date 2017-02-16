package com.hsulm.cermitandroidextensionslib;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import java.io.IOException;
import java.util.UUID;

import de.hsulm.cermit.implementations.BluetoothDeviceDescriptor;
import de.hsulm.cermit.sensorunitcommunication.CommandManager;
import de.hsulm.cermit.sensorunitcommunication.Connection;
import de.hsulm.cermit.sensorunitcommunication.Receiver;

/**
 * Represents Serial Port Profile (SPP) Socket implementation of connection for Android systems.
 * Responsible for Connecting to and disconnecting from physical devices
 *
 * @author Peter Fischer
 */
public class SppSocketAndroid extends Connection {
    private static final String TAG = SppSocketAndroid.class.getSimpleName();
    protected boolean disconnected = false;
    protected Context context;
    protected BluetoothSocket socket;
    protected String macAddress;

    /**
     * Constructor - creates an SPP Socket connection to a physical device and initializes the
     * Receiver and CommandManager
     *
     * @param MAC_Address      MAC Adress of the physical device that shall be connected
     * @param context the Android application's context
     */
    public SppSocketAndroid(String MAC_Address, Context context) throws IOException{
        this.context = context;
        BluetoothDevice device = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(MAC_Address);//
        UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
        /*Hardcoded UUID:
        *   1101    => SPP
        *   rest    => default "Bluetooth_Base_UUID" // see http://people.csail.mit.edu/rudolph/Teaching/Articles/PartOfBTBook.pdf
        */
        macAddress = device.getAddress();
        socket = device.createInsecureRfcommSocketToServiceRecord(uuid);
        /**before connecting, make sure your device is paired in bluetooth-settings of your smartphone*/
        socket.connect();

        //VW: Created new BluetoothDevice Descriptor
        BluetoothDeviceDescriptor bluetoothDeviceDescriptor = new BluetoothDeviceDescriptor(macAddress, device.getName());

        this.receiver = new Receiver(bluetoothDeviceDescriptor, socket.getInputStream());
        commandManager = new CommandManager(bluetoothDeviceDescriptor, socket.getOutputStream());
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        context.registerReceiver(broadcastReceiver,filter);
    }


    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
           if(disconnected = BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action) && device.getAddress().equals(macAddress)){
               context.unregisterReceiver(broadcastReceiver);
           }
        }
    };

    /**
     * checks whether there is still a physical connection
     *
     * @return true if there is a physical connection
     */
    @Override
    public boolean isStillPhysicallyConnected() {
        return !disconnected;
    }

    @Override
    public int readConnectionStrength() {
        //TODO: Add content
        return 0;
    }

    @Override
    public void open() {
        if(!receiver.isAlive()) {
            /**Receiver muss gestartet werden, um einen Statuswechsel registrieren zu können*/
            this.receiver.start();
        }
    }


    /**
     * Enables to disconnect from the physical Sensor Unit in an orderly fashion
     */
    @Override
    public void disconnect() {
        if(isStillPhysicallyConnected()) {
            try {
                socket.close();
                commandManager.close();
            } catch (IOException e) {
                e.printStackTrace();
                Log.e(TAG, "Socket could not be closed (disconnected) due to IOException");
            }
            //super.disconnect();
            disconnected = true;
        }
    }
}
