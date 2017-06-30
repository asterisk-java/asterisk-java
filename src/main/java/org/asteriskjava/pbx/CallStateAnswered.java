package org.asteriskjava.pbx;

import org.asteriskjava.util.Log;
import org.asteriskjava.util.LogFactory;

public class CallStateAnswered extends CallStateData
{
    @SuppressWarnings("unused")
    private static final Log logger = LogFactory.getLog(CallStateAnswered.class);

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
