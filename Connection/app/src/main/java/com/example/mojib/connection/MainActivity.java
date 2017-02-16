package com.example.mojib.connection;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.mojib.connection.Controller.AppController;

import java.util.ArrayList;

import de.hsulm.cermit.exchangableinterface.IDeviceDescriptor;

public class MainActivity extends AppCompatActivity{

    Button bConnect;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ArrayList<IDeviceDescriptor> mSensorUnitsToConnect = new ArrayList<>();

        Context context = getApplicationContext();
        CharSequence msg = "connection button is pressed";
        int duration = Toast.LENGTH_SHORT;
        final Toast toast = Toast.makeText(context, msg ,duration);
        final AppController cermitController = new AppController();
        bConnect = (Button) findViewById(R.id.button_connection);
        bConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cermitController.setup();

                cermitController.getDeviceSearcheCallback().receive(SensorUnits);
                toast.show();


            }
        });
    }

}
