package org.asteriskjava.pbx.internal.managerAPI;

import org.asteriskjava.pbx.Channel;

public class OriginateResult
{

    private boolean channelHungup = false;

    private boolean success = false;

    private Channel newChannel = null;

    private String abortReason;

    OriginateResult()
    {
        // not used
    }

    public String getAbortReason()
    {
        return this.abortReason;
    }

    void setAbortReason(final String reason)
    {
        this.abortReason = reason;

    }

    public void setChannelData(final Channel channel)
    {
        this.newChannel = channel;
    }

    public Channel getChannel()
    {

        return this.newChannel;
    }

    public boolean isSuccess()
    {
        return this.success;
    }

    public void setSuccess(boolean success)
    {
        this.success = success;
    }

    public boolean isChannelHungup()
    {
        return this.channelHungup;
    }

    public void setChannelHungup(boolean channel1Hungup)
    {
        this.channelHungup = channel1Hungup;
    }

}
