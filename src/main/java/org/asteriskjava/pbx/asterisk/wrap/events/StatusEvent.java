package org.asteriskjava.pbx.asterisk.wrap.events;

import java.util.Map;

import org.asteriskjava.pbx.CallerID;
import org.asteriskjava.pbx.Channel;
import org.asteriskjava.pbx.DialPlanExtension;
import org.asteriskjava.pbx.InvalidChannelName;
import org.asteriskjava.pbx.PBXFactory;
import org.asteriskjava.pbx.internal.core.AsteriskPBX;

public class StatusEvent extends ResponseEvent implements ChannelEvent
{
    private static final long serialVersionUID = 1L;

    private final Channel channel;
    private final CallerID callerId;
    private final String accountCode;
    private final ChannelState channelState;
    private final String channelStateDesc;
    private final String context;
    private final DialPlanExtension extension;
    private final Integer priority;
    private final Integer seconds;
    private final Channel bridgedChannel;
    private final Map<String, String> variables;
    private final String uniqueId;

    public StatusEvent(final org.asteriskjava.manager.event.StatusEvent event) throws InvalidChannelName
    {
        super(event);
        final AsteriskPBX pbx = (AsteriskPBX) PBXFactory.getActivePBX();

        this.channel = pbx.internalRegisterChannel(event.getChannel(), event.getUniqueId());
        this.callerId = pbx.buildCallerID(event.getCallerIdNum(), event.getCallerIdName());
        this.accountCode = event.getAccountCode();
        this.channelState = ChannelState.valueOfDesc(event.getChannelStateDesc());
        this.channelStateDesc = event.getChannelStateDesc();
        this.context = event.getContext();
        this.extension = pbx.buildDialPlanExtension(event.getExtension());
        this.priority = event.getPriority();
        this.seconds = event.getSeconds();
        if (event.getBridgedChannel() != null)
            this.bridgedChannel = pbx.internalRegisterChannel(event.getBridgedChannel(), event.getBridgedUniqueId());
        else
            this.bridgedChannel = null;
        this.variables = event.getVariables();
        this.uniqueId = event.getUniqueId();
    }

    @Override
    public final Channel getChannel()
    {
        return this.channel;
    }

    public final CallerID getCallerId()
    {
        return this.callerId;
    }

    public final String getAccountCode()
    {
        return this.accountCode;
    }

    public final ChannelState getState()
    {
        return this.channelState;
    }

    public final String getChannelStateDesc()
    {
        return this.channelStateDesc;
    }

    public final String getContext()
    {
        return this.context;
    }

    public final DialPlanExtension getExtension()
    {
        return this.extension;
    }

    public final Integer getPriority()
    {
        return this.priority;
    }

    public final Integer getSeconds()
    {
        return this.seconds;
    }

    public final Channel getBridgedChannel()
    {
        return this.bridgedChannel;
    }

    public final Map<String, String> getVariables()
    {
        return this.variables;
    }

    public String getUniqueId()
    {
        return this.uniqueId;
    }
}
