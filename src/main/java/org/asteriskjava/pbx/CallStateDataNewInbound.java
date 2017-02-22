package org.asteriskjava.pbx;

import org.apache.log4j.Logger;

public class CallStateDataNewInbound extends CallStateData
{
	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(CallStateDataNewInbound.class);

	CallerID _originatingPartyCallerID;

	private final String _fromDID;

	public CallStateDataNewInbound(String fromDID, CallerID originatingPartyCallerID)
	{
		this._originatingPartyCallerID = originatingPartyCallerID;
		this._fromDID = fromDID;
	}

	public CallerID getOriginatingPartyCallerID()
	{
		return this._originatingPartyCallerID;
	}

	public String getFromDID()
	{
		return this._fromDID;
	}
}
