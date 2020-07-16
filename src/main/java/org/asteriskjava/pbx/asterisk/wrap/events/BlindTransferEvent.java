package org.asteriskjava.pbx.asterisk.wrap.events;

import java.util.Date;

import org.asteriskjava.pbx.Channel;
import org.asteriskjava.pbx.InvalidChannelName;
import org.asteriskjava.pbx.PBXFactory;
import org.asteriskjava.pbx.internal.core.AsteriskPBX;
import org.asteriskjava.util.Log;
import org.asteriskjava.util.LogFactory;

public class BlindTransferEvent extends ManagerEvent
{
    private static final long serialVersionUID = 1L;

    @SuppressWarnings("unused")
    private static final Log logger = LogFactory.getLog(BlindTransferEvent.class);

    org.asteriskjava.manager.event.BlindTransferEvent rawEvent;

    private final Channel transfereeChannel;

    private final Channel transfererChannel;

    public BlindTransferEvent(final org.asteriskjava.manager.event.BlindTransferEvent event) throws InvalidChannelName
    {
        super(event);
        rawEvent = event;

        final AsteriskPBX pbx = (AsteriskPBX) PBXFactory.getActivePBX();

        this.transfereeChannel = pbx.internalRegisterChannel(event.getTransfereeChannel(), event.getTransfereeUniqueId());
        this.transfererChannel = pbx.internalRegisterChannel(event.getTransfererChannel(), event.getTransfererUniqueId());

    }

    public String getBridgeUniqueId()
    {
        return rawEvent.getBridgeUniqueId();
    }

    public String getBridgeType()
    {
        return rawEvent.getBridgeType();
    }

    public Integer getBridgeNumChannels()
    {
        return rawEvent.getBridgeNumChannels();
    }

    public String getBridgeCreator()
    {
        return rawEvent.getBridgeCreator();
    }

    public String getBridgeName()
    {
        return rawEvent.getBridgeName();
    }

    public String getBridgeTechnology()
    {
        return rawEvent.getBridgeTechnology();
    }

    public String getCallerIdName()
    {
        return rawEvent.getCallerIdName();
    }

    public String getAccountCode()
    {
        return rawEvent.getAccountCode();
    }

    public String getBridgevideosourcemode()
    {
        return rawEvent.getBridgevideosourcemode();
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

    public Date getDateReceived()
    {
        return rawEvent.getDateReceived();
    }

    public String getPrivilege()
    {
        return rawEvent.getPrivilege();
    }

    public String getExtension()
    {
        return rawEvent.getExtension();
    }

    public String getIsexternal()
    {
        return rawEvent.getIsexternal();
    }

    public String getResult()
    {
        return rawEvent.getResult();
    }

    public final String getServer()
    {
        return rawEvent.getServer();
    }

    public String getFile()
    {
        return rawEvent.getFile();
    }

    public Integer getLine()
    {
        return rawEvent.getLine();
    }

    public String getFunc()
    {
        return rawEvent.getFunc();
    }

    public Integer getSequenceNumber()
    {
        return rawEvent.getSequenceNumber();
    }

    public Object getSource()
    {
        return rawEvent.getSource();
    }

    public String getTransfererUniqueId()
    {
        return rawEvent.getTransfererUniqueId();
    }

    public String getTransfererConnectedLineNum()
    {
        return rawEvent.getTransfererConnectedLineNum();
    }

    public String getTransfererConnectedLineName()
    {
        return rawEvent.getTransfererConnectedLineName();
    }

    public String getTransfererCallerIdName()
    {
        return rawEvent.getTransfererCallerIdName();
    }

    public String getTransfererCallerIdNum()
    {
        return rawEvent.getTransfererCallerIdNum();
    }

    public Channel getTransfererChannel()
    {
        return transfererChannel;
    }

    public String getTransfererChannelState()
    {
        return rawEvent.getTransfererChannelState();
    }

    public String getTransfererChannelStateDesc()
    {
        return rawEvent.getTransfererChannelStateDesc();
    }

    public Integer getTransfererPriority()
    {
        return rawEvent.getTransfererPriority();
    }

    public String getTransfererContext()
    {
        return rawEvent.getTransfererContext();
    }

    public String getTransfereeUniqueId()
    {
        return rawEvent.getTransfereeUniqueId();
    }

    public String getTransfereeConnectedLineNum()
    {
        return rawEvent.getTransfereeConnectedLineNum();
    }

    public String getTransfereeConnectedLineName()
    {
        return rawEvent.getTransfereeConnectedLineName();
    }

    public String getTransfereeCallerIdName()
    {
        return rawEvent.getTransfereeCallerIdName();
    }

    public String getTransfereeCallerIdNum()
    {
        return rawEvent.getTransfereeCallerIdNum();
    }

    public Channel getTransfereeChannel()
    {
        return transfereeChannel;
    }

    public String getTransfereeChannelState()
    {
        return rawEvent.getTransfereeChannelState();
    }

    public String getTransfereeChannelStateDesc()
    {
        return rawEvent.getTransfereeChannelStateDesc();
    }

    public Integer getTransfereePriority()
    {
        return rawEvent.getTransfereePriority();
    }

    public final Double getTimestamp()
    {
        return rawEvent.getTimestamp();
    }

    public String getTransfereeContext()
    {
        return rawEvent.getTransfereeContext();
    }

    public String getTransfereeExten()
    {
        return rawEvent.getTransfereeExten();
    }

    public String getTransfereeLinkedId()
    {
        return rawEvent.getTransfereeLinkedId();
    }

    public String getTransfererAccountCode()
    {
        return rawEvent.getTransfererAccountCode();
    }

    public String getTransfererExten()
    {
        return rawEvent.getTransfererExten();
    }

    public String getTransfererLanguage()
    {
        return rawEvent.getTransfererLanguage();
    }

    public String getSystemName()
    {
        return rawEvent.getSystemName();
    }

    public String getTransfererLinkedId()
    {
        return rawEvent.getTransfererLinkedId();
    }

    public String getTransfereeLanguage()
    {
        return rawEvent.getTransfereeLanguage();
    }

    public String toString()
    {
        return rawEvent.toString();
    }

}
