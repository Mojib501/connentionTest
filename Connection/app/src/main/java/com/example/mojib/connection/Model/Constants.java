package com.example.mojib.connection.Model;

import android.os.Environment;

/**
 * Created by mojib on 14.02.2017.
 */
/**
 * Konstanten, die in verschiedenen Klassen verwendet werden
 *
 * @author Peter Fischer, Vanessa W.
 */
public class Constants {
    public static final String STORAGE_PATH = Environment.
            getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).getPath()
            + "//MotionSupport";

    /**Strings for BroadcastReceiver*/
    public static final String ACTION_STRING_TRY_CONNECT = "TryToConnect";
    public static final String ACTION_STRING_CONNECTION_COMPLETE = "ConnectionCompleted";
    public static final String ACTION_STRING_UPDATE_TEXTVIEW = "UpdateTextView";
    public static final String COMMAND_VIBRATE = "VIBRATE";
    public static final String COMMAND_START_MEASUREMENT = "START_MEASUREMENT";
    public static final String COMMAND_STOP_MEASUREMENT = "STOP_MEASUREMENT";
    public static final String COMMAND_SENT_NOTIFICATION = "CmdNotificationSent";
    public static final String COMMAND_PROBANDID = "PROBAND_ID";
    public static final String COMMAND_CONNECTION_STATUS = "Connection_Status";
    public static final String COMMAND_CONNECTED_SENSORUNITS = "Number_of_Connected_SensorUnits";

    /**Strings for Spinner Vibration-Location*/
    public static final String WRISTS = "Handgelenke";
    public static final String ANKLES = "Fußgelenke";

    /**Strings Connection-States*/
    public static final String NOT_CONNECTED = "nicht verbunden";
    public static final String TRY_TO_CONNECT = "Verbindungsversuch...";

}
