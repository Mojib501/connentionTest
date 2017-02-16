package com.example.mojib.connection.Model;

import de.hsulm.cermit.messages.DataMessageSingle;

/**
 * Created by mojib on 14.02.2017.
 */

public class Measurement {
    private DataMessageSingle singleMsg;
    private long timeStamp;
    private int mdID;
    private static int counter=0;


    public Measurement(DataMessageSingle singleMsg, long timeStamp, int mdID){
        this.singleMsg=singleMsg;
        this.timeStamp=timeStamp;
        this.mdID=mdID;
    }


    public long getTimeStamp(){
        return timeStamp;
    }
    public Integer[] getMetrics(){
        return singleMsg.getMetrics();
    }
}
