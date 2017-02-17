package com.example.mojib.connection;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Set;

public class MainActivity extends AppCompatActivity{

    private static final int DISCOVERY_REQUEST = 1 ;
    Button bConnect;
    ArrayList<String> mArrayAdapter;
    BluetoothAdapter btAdapter;
    TextView textView;
    CharSequence toastText ="";
    BluetoothDevice remoteDevice;
    BroadcastReceiver bluetoothState = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

        }
    };



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == DISCOVERY_REQUEST) {
            Toast.makeText(MainActivity.this, "Discovery in prograss", Toast.LENGTH_SHORT).show();
            //setupUI();
            findDevices();
        }
    }
    public void findDevices() {
        String lastUsedRemoteDevice = getLastUsedRemoteDevice();
        if(lastUsedRemoteDevice != null){
            toastText = "Checking for known paired devices, namely: "+lastUsedRemoteDevice;
            Toast.makeText(MainActivity.this, toastText,Toast.LENGTH_SHORT).show();
            //schaut ob Device in in der Liste ist
            Set<BluetoothDevice> pairedDevices = btAdapter.getBondedDevices();
            for (BluetoothDevice pairedDevice : pairedDevices){
                if(pairedDevice.getAddress().equals(lastUsedRemoteDevice)){
                    toastText="Found device: "+pairedDevice.getName()+"@"+lastUsedRemoteDevice;
                    Toast.makeText(MainActivity.this, toastText, Toast.LENGTH_SHORT).show();
                    remoteDevice = pairedDevice;
                }//end if
            }//end for
        }//end if
        if(remoteDevice == null){
            toastText="Starting discovery for remote devices...";
            Toast.makeText(MainActivity.this, toastText, Toast.LENGTH_SHORT).show();
            //start discovery
            if(btAdapter.startDiscovery()){
                toastText="Discovery thread started...Scanning for Devices";
                Toast.makeText(MainActivity.this, toastText, Toast.LENGTH_SHORT).show();
                registerReceiver(discoveryResult, new IntentFilter(BluetoothDevice.ACTION_FOUND));
            }//end if
        } //end if
    }//end Method

    private String getLastUsedRemoteDevice() {
        // key-value Set for small Collection
        SharedPreferences prefs = getPreferences(MODE_PRIVATE);
        String result = prefs.getString("LAST_REMOTE_DEVICE_ADDRESS", null);
        return result;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btAdapter = BluetoothAdapter.getDefaultAdapter();
        bConnect = (Button)findViewById(R.id.button_connection);
        //Schaltet Bluetooth an und macht es sichbar
        bConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String scanModeChanged = BluetoothAdapter.ACTION_SCAN_MODE_CHANGED;
                String beDiscoverable = BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE;
                IntentFilter filter = new IntentFilter(scanModeChanged);
                registerReceiver(bluetoothState,filter);
                startActivityForResult(new Intent(beDiscoverable), DISCOVERY_REQUEST);
            }
        });
    }//end onCreate()
    //BroadcastReceiver to receive device discovery
    BroadcastReceiver discoveryResult = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String remoteDeviceName = intent.getStringExtra(BluetoothDevice.EXTRA_NAME);
            BluetoothDevice remoteDevice;
            remoteDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            toastText = "Discovered: "+ remoteDeviceName;
            Toast.makeText(MainActivity.this, toastText, Toast.LENGTH_SHORT).show();
            //statusUpdate.setTeact(statustext);
        }
    };
}//end

