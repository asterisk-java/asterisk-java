package org.asteriskjava.util;

/**
 * MixMonitorDirection Which part of the recording to mute: read, write or both
 * (from channel, to channel or both channels).
 * 
 * @author adrian.videanu
 */
public enum MixMonitorDirection
{

    FROM_CHANNEL("read"), TO_CHANNEL("write"), BOTH("both");

    String stateName;

    MixMonitorDirection(String name)
    {
        this.stateName = name;
    }

    public String getStateName()
    {
        return stateName;
    }

    public void setStateName(String stateName)
    {
        this.stateName = stateName;
    }

}
