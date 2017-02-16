package com.example.mojib.connection.Model;

import de.hsulm.cermit.exchangableinterface.IDeviceDescriptor;

/**
 * Created by mojib on 14.02.2017.
 */
public class QueueIdentifier {
    private IDeviceDescriptor desc;
    private int mdId;

    public QueueIdentifier(IDeviceDescriptor desc, int mdId){
        this.mdId = mdId;
        this.desc = desc;
    }

    public IDeviceDescriptor getDesc(){
        return this.desc;
    }
    public int getmDid(){
        return this.mdId;
    }
    @Override
    public boolean equals(Object other){
        if(!(other instanceof QueueIdentifier)){
            return false;
        }
        else if(other==null){
            return false;
        }
        else{
            return other.toString().equals(this.toString());
        }

    }

    @Override
    public int hashCode(){
        return toString().hashCode();
    }

    @Override
    public String toString(){
        return this.desc.getUniqueIdentifier()+"_"+mdId;
    }
}
