package org.asteriskjava.pbx.asterisk.wrap.events;

import java.util.Date;

import org.asteriskjava.pbx.Channel;
import org.asteriskjava.pbx.InvalidChannelName;
import org.asteriskjava.pbx.PBXFactory;
import org.asteriskjava.pbx.internal.core.AsteriskPBX;

public class DialEndEvent extends ChannelEventHelper
{
    private static final long serialVersionUID = 1L;

    /**
     * The name of the destination channel.
     */
    private final Channel destChannel;

    final org.asteriskjava.manager.event.DialEndEvent rawEvent;

    public DialEndEvent(final org.asteriskjava.manager.event.DialEndEvent event) throws InvalidChannelName
    {
        super(event.getChannel(), event.getUniqueId(), event.getCallerIdNum(), event.getCallerIdName());

        final AsteriskPBX pbx = (AsteriskPBX) PBXFactory.getActivePBX();
        if (event.getDestination() != null)
            this.destChannel = pbx.internalRegisterChannel(event.getDestChannel(), event.getDestUniqueId());
        else
            this.destChannel = null;

        rawEvent = event;

    }

    public String getDestination()
    {
        return rawEvent.getDestination();
    }

    public String getLanguage()
    {
        return rawEvent.getLanguage();
    }

    public String getDestLanguage()
    {
        return rawEvent.getDestLanguage();
    }

    public String getAccountCode()
    {
        return rawEvent.getAccountCode();
    }

    public String getDestAccountCode()
    {
        return rawEvent.getDestAccountCode();
    }

    public String getDestLinkedId()
    {
        return rawEvent.getDestLinkedId();
    }

    public String getLinkedId()
    {
        return rawEvent.getLinkedId();
    }

    public String getForward()
    {
        return rawEvent.getForward();
    }

    public String getCallerIdName()
    {
        return rawEvent.getCallerIdName();
    }

    public String getConnectedLineNum()
    {
        return rawEvent.getConnectedLineNum();
    }

    public String getConnectedLineName()
    {
        return rawEvent.getConnectedLineName();
    }

    public Integer getPriority()
    {
        return rawEvent.getPriority();
    }

    public Integer getChannelState()
    {
        return rawEvent.getChannelState();
    }

    public String getChannelStateDesc()
    {
        return rawEvent.getChannelStateDesc();
    }

    public String getExten()
    {
        return rawEvent.getExten();
    }

    public String getCallerIdNum()
    {
        return rawEvent.getCallerIdNum();
    }

    public String getContext()
    {
        return rawEvent.getContext();
    }

    public String getSubEvent()
    {
        return rawEvent.getSubEvent();
    }

    public Date getDateReceived()
    {
        return rawEvent.getDateReceived();
    }

    @SuppressWarnings("deprecation")
    public String getSrc()
    {
        return rawEvent.getSrc();
    }

    public String getPrivilege()
    {
        return rawEvent.getPrivilege();
    }

    public final Double getTimestamp()
    {
        return rawEvent.getTimestamp();
    }

    public Channel getDestChannel()
    {
        return destChannel;
    }

    @SuppressWarnings("deprecation")
    public String getCallerId()
    {
        return rawEvent.getCallerId();
    }

    public final String getServer()
    {
        return rawEvent.getServer();
    }

    public String getUniqueId()
    {
        return rawEvent.getUniqueId();
    }

    public String getSystemName()
    {
        return rawEvent.getSystemName();
    }

    @SuppressWarnings("deprecation")
    public String getSrcUniqueId()
    {
        return rawEvent.getSrcUniqueId();
    }

    public String getFile()
    {
        return rawEvent.getFile();
    }

    public String getDestUniqueId()
    {
        return rawEvent.getDestUniqueId();
    }

    public Integer getLine()
    {
        return rawEvent.getLine();
    }

    public String getDialString()
    {
        return rawEvent.getDialString();
    }

    public String getDialStatus()
    {
        return rawEvent.getDialStatus();
    }

    public String getFunc()
    {
        return rawEvent.getFunc();
    }

    public Integer getSequenceNumber()
    {
        return rawEvent.getSequenceNumber();
    }

    public Integer getDestChannelState()
    {
        return rawEvent.getDestChannelState();
    }

    public String getDestContext()
    {
        return rawEvent.getDestContext();
    }

    public Integer getDestPriority()
    {
        return rawEvent.getDestPriority();
    }

    public String getDestChannelStateDesc()
    {
        return rawEvent.getDestChannelStateDesc();
    }

    public String getDestExten()
    {
        return rawEvent.getDestExten();
    }

    public String getDestConnectedLineName()
    {
        return rawEvent.getDestConnectedLineName();
    }

    public String getDestConnectedLineNum()
    {
        return rawEvent.getDestConnectedLineNum();
    }

    public String getDestCallerIdName()
    {
        return rawEvent.getDestCallerIdName();
    }

    public String getDestCallerIdNum()
    {
        return rawEvent.getDestCallerIdNum();
    }

    public Object getSource()
    {
        return rawEvent.getSource();
    }

}
