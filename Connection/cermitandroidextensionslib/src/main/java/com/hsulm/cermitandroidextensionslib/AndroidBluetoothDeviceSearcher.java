package com.hsulm.cermitandroidextensionslib;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import de.hsulm.cermit.eventinterface.IDeviceSearchCallback;
import de.hsulm.cermit.exchangableinterface.IDeviceDescriptor;
import de.hsulm.cermit.exchangableinterface.IDeviceSearcher;
import de.hsulm.cermit.implementations.BluetoothDeviceDescriptor;

/**
 *  Responsible for searching Bluetooth devices when CERMIT is used in Android application
 *
 * @author Anna Kamm, Peter Fischer
 */
public class AndroidBluetoothDeviceSearcher implements IDeviceSearcher {
        private BluetoothAdapter bluetooth = BluetoothAdapter.getDefaultAdapter();
        private List<IDeviceDescriptor> devices = new ArrayList<>();
        private Context context;
        private IDeviceSearchCallback deviceSearchCallback;

        /**
         * Searches for new Bluetooth Devices which are enabled
         * @param deviceSearchCallback callback that receives updates on devices
         * @return a List of all found Devices
         */
    public void search (IDeviceSearchCallback deviceSearchCallback) {
        this.deviceSearchCallback = deviceSearchCallback;
        devices.clear();
        //getPairedDevices();   Add this method if you like to see all paired Devices
        startDiscovering();
        //TODO (pfischer)[2]: too avoid tampering with existing connections, check whether a BT device is already connected first
        }

    /**
     * NOT IMPLEMENTED YET
     * @param deviceSearchCallback callback that receives updates on devices
     * @param macAddresses NOT IMPLEMENTED YET
     * @return NOT IMPLEMENTED YET
     */
    public void search (IDeviceSearchCallback deviceSearchCallback, List<String> macAddresses){
        List<IDeviceDescriptor> deviceList = new ArrayList<IDeviceDescriptor>();
        // TODO (pfischer) [2]: implement: search for BT device with given mac address and return its device descriptor
        throw new RuntimeException("This method is not implemented yet in the BluetoothDeviceSearcher");
    }

    /**
     * Constructor surrogate - has to be called before the instance can be used
     * @param context the application's context
     */
    public void setContext (Context context){
        this.context = context;
    }


    /**
     * Starts the Discovering of the Bluetooth Adapter
     * Starts the Broadcast Receiver for ACTION_FOUND, which is like a EventListener for a new found Device
     * Starts the Broadcast Receiver for ACTION_DISCOVERY_FINISHED
     */
    private void startDiscovering() {
        if(context == null)
            throw new IllegalStateException("Context has to be set before searching for devices!");
        context.registerReceiver(discoveryResult, new IntentFilter(BluetoothDevice.ACTION_FOUND));
        context.registerReceiver(discoveryMonitor, new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED));
        if (bluetooth.isEnabled() && !bluetooth.isDiscovering()) {
            bluetooth.startDiscovery();
        }
    }

    /**
     * Adds all paired devices to the list, it does not show if the devices are enable or not
     */
    private void getPairedDevices() {
        Set<BluetoothDevice> pairedDevices = bluetooth.getBondedDevices();
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice remoteDevice : pairedDevices) {
                IDeviceDescriptor remoteDeviceDescriptor =
                        new BluetoothDeviceDescriptor(remoteDevice.getName(), remoteDevice.getAddress());
                devices.add(remoteDeviceDescriptor);
            }
        }
    }

    /**
     * Adds a new discovered Device to the device list
     */
    private final BroadcastReceiver discoveryResult = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (BluetoothDevice.ACTION_FOUND.equals(intent.getAction())) {
                BluetoothDevice remoteDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                BluetoothDeviceDescriptor remoteDeviceDescriptor =
                        new BluetoothDeviceDescriptor(remoteDevice.getAddress(),remoteDevice.getName());
                boolean alreadyFound = false;
                for(IDeviceDescriptor device : devices) {
                  alreadyFound = device.getUniqueIdentifier() == remoteDeviceDescriptor.getUniqueIdentifier();
                }
                if(!alreadyFound)
                    devices.add(remoteDeviceDescriptor);
            }
    }
    };

    /**
     * Turns off the BroadcastReceivers after finished discovery
     */
    private final BroadcastReceiver discoveryMonitor = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(intent.getAction())) {
                context.unregisterReceiver(discoveryResult);
                context.unregisterReceiver(discoveryMonitor);
                bluetooth.cancelDiscovery();
                deviceSearchCallback.receive(devices);
            }
        }
    };


}

