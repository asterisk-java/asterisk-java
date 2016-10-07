package org.asteriskjava.pbx;

import org.apache.log4j.Logger;

public class CallStateDataParked extends CallStateData
{
	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(CallStateDataParked.class);

	EndPoint _parkingLot;

	public CallStateDataParked(EndPoint parkingLot)
	{
		this._parkingLot = parkingLot;
	}

}
