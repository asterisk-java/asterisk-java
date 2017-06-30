package org.asteriskjava.pbx;

public class CallStateDataParked extends CallStateData
{

    EndPoint _parkingLot;

    public CallStateDataParked(EndPoint parkingLot)
    {
        this._parkingLot = parkingLot;
    }

}
