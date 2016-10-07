package org.asteriskjava.pbx;

import org.apache.log4j.Logger;

public class CallStateAnswered extends CallStateData
{
	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(CallStateAnswered.class);

	// The channel that accepted the call.
	private Channel _acceptingParty;

	public CallStateAnswered(Channel acceptingParty)
	{
		this._acceptingParty = acceptingParty;
	}

	public Channel getAcceptingParty()
	{
		return this._acceptingParty;
	}

}
