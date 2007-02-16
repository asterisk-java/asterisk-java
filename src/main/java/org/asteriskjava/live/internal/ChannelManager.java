/*
 *  Copyright 2004-2006 Stefan Reuter
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */
package org.asteriskjava.live.internal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import org.asteriskjava.live.AsteriskChannel;
import org.asteriskjava.live.CallerId;
import org.asteriskjava.live.ChannelState;
import org.asteriskjava.live.Extension;
import org.asteriskjava.live.HangupCause;
import org.asteriskjava.live.ManagerCommunicationException;
import org.asteriskjava.manager.ResponseEvents;
import org.asteriskjava.manager.action.StatusAction;
import org.asteriskjava.manager.event.CdrEvent;
import org.asteriskjava.manager.event.DialEvent;
import org.asteriskjava.manager.event.HangupEvent;
import org.asteriskjava.manager.event.LinkEvent;
import org.asteriskjava.manager.event.ManagerEvent;
import org.asteriskjava.manager.event.NewCallerIdEvent;
import org.asteriskjava.manager.event.NewChannelEvent;
import org.asteriskjava.manager.event.NewExtenEvent;
import org.asteriskjava.manager.event.NewStateEvent;
import org.asteriskjava.manager.event.RenameEvent;
import org.asteriskjava.manager.event.StatusEvent;
import org.asteriskjava.manager.event.UnlinkEvent;
import org.asteriskjava.util.DateUtil;
import org.asteriskjava.util.Log;
import org.asteriskjava.util.LogFactory;

/**
 * Manages channel events on behalf of an AsteriskServer.
 * 
 * @author srt
 * @version $Id$
 */
class ChannelManager
{
    private final Log logger = LogFactory.getLog(getClass());

    /**
     * How long do we wait before we remove hung up channels from memory?
     * (in milliseconds)
     */
    private static final long REMOVAL_THRESHOLD = 15 * 60 * 1000L; // 15 minutes

    private final AsteriskServerImpl server;
    
    /**
     * A map of all active channel by their unique id.
     */
    private final Map<String, AsteriskChannelImpl> channels;

    /**
     * Creates a new instance.
     */
    ChannelManager(AsteriskServerImpl server)
    {
        this.server = server;
        this.channels = new HashMap<String, AsteriskChannelImpl>();
    }

    void initialize() throws ManagerCommunicationException
    {
        ResponseEvents re;

        disconnected();
        re = server.sendEventGeneratingAction(new StatusAction());
        for (ManagerEvent event : re.getEvents())
        {
            if (event instanceof StatusEvent)
            {
                handleStatusEvent((StatusEvent) event);
            }
        }
    }

    void disconnected()
    {
        synchronized (channels)
        {
            channels.clear();
        }
    }

    /**
     * Returns a collection of all active AsteriskChannels.
     * 
     * @return a collection of all active AsteriskChannels.
     */
    Collection<AsteriskChannel> getChannels()
    {
        Collection<AsteriskChannel> copy;

        synchronized (channels)
        {
            copy = new ArrayList<AsteriskChannel>(channels.size() + 2);
            for (AsteriskChannel channel : channels.values())
            {
                if (channel.getState() != ChannelState.HUNGUP)
                {
                    copy.add(channel);
                }
            }
        }
        return copy;
    }

    private void addChannel(AsteriskChannelImpl channel)
    {
        synchronized (channels)
        {
            channels.put(channel.getId(), channel);
        }
    }

    private void removeOldChannels()
    {
        Iterator<AsteriskChannelImpl> i;
        
        synchronized (channels)
        {
            i = channels.values().iterator();
            while (i.hasNext())
            {
                final AsteriskChannel channel = i.next();
                final Date dateOfRemoval = channel.getDateOfRemoval();
                if (channel.getState() == ChannelState.HUNGUP && dateOfRemoval != null)
                {
                    final long diff = DateUtil.getDate().getTime() - dateOfRemoval.getTime();
                    if (diff >= REMOVAL_THRESHOLD)
                    {
                        i.remove();
                    }
                }
            }
        }
    }

    private AsteriskChannelImpl addNewChannel(String uniqueId, String name, 
            Date dateOfCreation, String callerIdNumber, String callerIdName, 
            ChannelState state)
    {
        final AsteriskChannelImpl channel;
        final String traceId;

        channel = new AsteriskChannelImpl(server, name, uniqueId, dateOfCreation);
        channel.setCallerId(new CallerId(callerIdName, callerIdNumber));
        channel.stateChanged(dateOfCreation, state);
        logger.info("Adding channel " + channel.getName() + "(" + channel.getId() + ")");

        traceId = getTraceId(channel);
        channel.setTraceId(traceId);

        addChannel(channel);

        if (traceId != null && (! name.toLowerCase(Locale.ENGLISH).startsWith("local/") || name.endsWith(",1")))
        {
            final OriginateCallbackData callbackData;
            callbackData = server.getOriginateCallbackDataByTraceId(traceId);
            if (callbackData != null && callbackData.getChannel() == null)
            {
                callbackData.setChannel(channel);
                try
                {
                    callbackData.getCallback().onDialing(channel);
                }
                catch (Throwable t)
                {
                    logger.warn("Exception dispatching originate progress", t);
                }
            }
        }
        server.fireNewAsteriskChannel(channel);
        return channel;
    }

    private ChannelState string2ChannelState(String state)
    {
        if (state == null)
        {
            return null;
        }

        return ChannelState.valueOf(state.toUpperCase());
    }

    void handleStatusEvent(StatusEvent event)
    {
        AsteriskChannelImpl channel;
        final Extension extension;
        boolean isNew = false;

        channel = getChannelImplById(event.getUniqueId());
        if (channel == null)
        {
            Date dateOfCreation;
            
            if (event.getSeconds() != null)
            {
                dateOfCreation = new Date(DateUtil.getDate().getTime()
                        - (event.getSeconds().intValue() * 1000));
            }
            else
            {
                dateOfCreation = DateUtil.getDate();
            }
            channel = new AsteriskChannelImpl(server, event.getChannel(), event.getUniqueId(), dateOfCreation);
            isNew = true;
        }

        if (event.getContext() == null && event.getExtension() == null
                && event.getPriority() == null)
        {
            extension = null;
        }
        else
        {
            extension = new Extension(event.getContext(), event.getExtension(), event.getPriority());
        }

        synchronized (channel)
        {
            channel.setCallerId(new CallerId(event.getCallerIdName(), event.getCallerIdNum()));
            channel.setAccount(event.getAccount());
            if (event.getState() != null)
            {
                channel.stateChanged(event.getDateReceived(), ChannelState.valueOf(event.getState().toUpperCase()));
            }
            channel.extensionVisited(event.getDateReceived(), extension);

            if (event.getLink() != null)
            {
                final AsteriskChannelImpl linkedChannel = getChannelImplByName(event.getLink());
                if (linkedChannel != null)
                {
                    // the date used here is not correct!
                    channel.channelLinked(event.getDateReceived(), linkedChannel);
                    synchronized (linkedChannel)
                    {
                        linkedChannel.channelLinked(event.getDateReceived(), channel);
                    }
                }
            }
        }

        if (isNew)
        {
            logger.info("Adding new channel " + channel.getName());
            addChannel(channel);
            server.fireNewAsteriskChannel(channel);
        }
    }

    AsteriskChannelImpl getChannelImplByName(String name)
    {
        AsteriskChannelImpl channel = null;

        if (name == null)
        {
            return null;
        }

        synchronized (channels)
        {
            for (AsteriskChannelImpl tmp : channels.values())
            {
                if (tmp.getName() != null && tmp.getName().equals(name))
                {
                    channel = tmp;
                }
            }
        }
        return channel;
    }

    AsteriskChannelImpl getChannelImplById(String id)
    {
        AsteriskChannelImpl channel = null;

        if (id == null)
        {
            return null;
        }

        synchronized (channels)
        {
            channel = channels.get(id);
        }
        return channel;
    }

    /**
     * Returns the other side of a local channel.
     * <p>
     * Local channels consist of two sides, like
     * "Local/1234@from-local-60b5,1" and "Local/1234@from-local-60b5,2"
     * this method returns the other side.
     * 
     * @param localChannel one side
     * @return the other side, or <code>null</code> if not available or if the given channel
     *         is not a local channel.
     */
    AsteriskChannelImpl getOtherSideOfLocalChannel(AsteriskChannel localChannel)
    {
        final String name;
        final char num;

        if (localChannel == null)
        {
            return null;
        }

        name = localChannel.getName();
        if (name == null || !name.startsWith("Local/") || name.charAt(name.length() - 2) != ',')
        {
            return null;
        }

        num = name.charAt(name.length() - 1);

        if (num == '1')
        {
            return getChannelImplByName(name.substring(0, name.length() - 1) + "2");
        }
        else if (num == '2')
        {
            return getChannelImplByName(name.substring(0, name.length() - 1) + "1");
        }
        else
        {
            return null;
        }
    }

    void handleNewChannelEvent(NewChannelEvent event)
    {
        AsteriskChannelImpl channel;

        channel = getChannelImplById(event.getUniqueId());
        if (channel == null)
        {
            channel = addNewChannel(
                    event.getUniqueId(), event.getChannel(), event.getDateReceived(), 
                    event.getCallerIdNum(), event.getCallerIdName(),
                    string2ChannelState(event.getState()));
        }
        else
        {
            // channel had already been created probably by a NewCallerIdEvent
            synchronized (channel)
            {
                channel.nameChanged(event.getDateReceived(), event.getChannel());
                channel.setCallerId(new CallerId(event.getCallerIdName(), event.getCallerIdNum()));
                channel.stateChanged(event.getDateReceived(), string2ChannelState(event.getState()));
            }
        }
    }

    void handleNewExtenEvent(NewExtenEvent event)
    {
        AsteriskChannelImpl channel;
        Extension extension;

        channel = getChannelImplById(event.getUniqueId());
        if (channel == null)
        {
            logger.error("Ignored NewExtenEvent for unknown channel "
                    + event.getChannel());
            return;
        }

        extension = new Extension(
                        event.getContext(), event.getExtension(), event.getPriority(), 
                        event.getApplication(), event.getAppData());

        synchronized (channel)
        {
            channel.extensionVisited(event.getDateReceived(), extension);
        }
    }

    void handleNewStateEvent(NewStateEvent event)
    {
        AsteriskChannelImpl channel = getChannelImplById(event.getUniqueId());

        if (channel == null)
        {
            // NewStateEvent can occur instead of a NewChannelEvent
            channel = addNewChannel(
                    event.getUniqueId(), event.getChannel(), event.getDateReceived(), 
                    event.getCallerIdNum(), event.getCallerIdName(), string2ChannelState(event.getState()));
        }
        if (event.getState() != null)
        {
            synchronized (channel)
            {
                channel.stateChanged(event.getDateReceived(), ChannelState.valueOf(event.getState().toUpperCase()));
            }
        }
    }

    void handleNewCallerIdEvent(NewCallerIdEvent event)
    {
        AsteriskChannelImpl channel = getChannelImplById(event.getUniqueId());

        if (channel == null)
        {
            // NewCallerIdEvent can occur before NewChannelEvent
            channel = addNewChannel(
                    event.getUniqueId(), event.getChannel(), event.getDateReceived(), 
                    event.getCallerIdNum(), event.getCallerIdName(), ChannelState.DOWN);
        }
        else
        {
            synchronized (channel)
            {
                channel.setCallerId(new CallerId(event.getCallerIdName(), event.getCallerIdNum()));
            }
        }
    }

    void handleHangupEvent(HangupEvent event)
    {
        HangupCause cause = null;
        AsteriskChannelImpl channel = getChannelImplById(event.getUniqueId());

        if (channel == null)
        {
            logger.error("Ignored HangupEvent for unknown channel "
                    + event.getChannel());
            return;
        }

        if (event.getCause() != null)
        {
            cause = HangupCause.getByCode(event.getCause());
        }
        
        synchronized (channel)
        {
            channel.hungup(event.getDateReceived(), cause, event.getCauseTxt());
        }

        logger.info("Removing channel " + channel.getName() + " due to hangup (" + cause + ")");
        removeOldChannels();
    }

    void handleDialEvent(DialEvent event)
    {
        AsteriskChannelImpl sourceChannel = getChannelImplById(event.getSrcUniqueId());
        AsteriskChannelImpl destinationChannel = getChannelImplById(event.getDestUniqueId());

        if (sourceChannel == null)
        {
            logger.error("Ignored LinkEvent for unknown source channel "
                    + event.getSrc());
            return;
        }
        if (destinationChannel == null)
        {
            logger.error("Ignored DialEvent for unknown destination channel "
                    + event.getDestination());
            return;
        }

        logger.info(sourceChannel.getName() + " dialed " + destinationChannel.getName());
        getTraceId(sourceChannel);
        getTraceId(destinationChannel);
        synchronized (sourceChannel)
        {
            sourceChannel.channelDialed(event.getDateReceived(), destinationChannel);
        }
        synchronized (destinationChannel)
        {
            destinationChannel.channelDialing(event.getDateReceived(), sourceChannel);
        }
    }

    void handleLinkEvent(LinkEvent event)
    {
        AsteriskChannelImpl channel1 = getChannelImplById(event.getUniqueId1());
        AsteriskChannelImpl channel2 = getChannelImplById(event.getUniqueId2());

        if (channel1 == null)
        {
            logger.error("Ignored LinkEvent for unknown channel "
                    + event.getChannel1());
            return;
        }
        if (channel2 == null)
        {
            logger.error("Ignored LinkEvent for unknown channel "
                    + event.getChannel2());
            return;
        }

        logger.info("Linking channels " + channel1.getName() + " and "
                + channel2.getName());
        synchronized (channel1)
        {
            channel1.channelLinked(event.getDateReceived(), channel2);
        }

        synchronized (channel2)
        {
            channel2.channelLinked(event.getDateReceived(), channel1);
        }
    }

    void handleUnlinkEvent(UnlinkEvent event)
    {
        AsteriskChannelImpl channel1 = getChannelImplByName(event.getChannel1());
        AsteriskChannelImpl channel2 = getChannelImplByName(event.getChannel2());

        if (channel1 == null)
        {
            logger.error("Ignored UnlinkEvent for unknown channel "
                    + event.getChannel1());
            return;
        }
        if (channel2 == null)
        {
            logger.error("Ignored UnlinkEvent for unknown channel "
                    + event.getChannel2());
            return;
        }

        logger.info("Unlinking channels " + channel1.getName() + " and "
                + channel2.getName());
        synchronized (channel1)
        {
            channel1.channelUnlinked(event.getDateReceived());
        }

        synchronized (channel2)
        {
            channel2.channelUnlinked(event.getDateReceived());
        }
    }

    void handleRenameEvent(RenameEvent event)
    {
        AsteriskChannelImpl channel = getChannelImplById(event.getUniqueId());

        if (channel == null)
        {
            logger.error("Ignored RenameEvent for unknown channel with uniqueId "
                            + event.getUniqueId());
            return;
        }

        logger.info("Renaming channel '" + channel.getName() + "' to '"
                + event.getNewname() + "'");
        synchronized (channel)
        {
            channel.nameChanged(event.getDateReceived(), event.getNewname());
        }
    }

    void handleCdrEvent(CdrEvent event)
    {
        AsteriskChannelImpl channel = getChannelImplById(event.getUniqueId());
        AsteriskChannelImpl destinationChannel = getChannelImplByName(event.getDestinationChannel());
        CallDetailRecordImpl cdr;

        if (channel == null)
        {
            logger.info("Ignored CdrEvent for unknown channel with uniqueId "
                            + event.getUniqueId());
            return;
        }

        cdr = new CallDetailRecordImpl(channel, destinationChannel, event);

        synchronized (channel)
        {
            channel.callDetailRecordReceived(event.getDateReceived(), cdr);
        }
    }

    private String getTraceId(AsteriskChannel channel)
    {
        String traceId;

        try
        {
            traceId = channel.getVariable(Constants.VARIABLE_TRACE_ID);
        }
        catch (Exception e)
        {
            traceId = null;
        }
        //logger.info("TraceId for channel " + channel.getName() + " is " + traceId);
        return traceId;
    }
}
