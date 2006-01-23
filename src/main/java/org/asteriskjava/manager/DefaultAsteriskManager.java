/*
 * Copyright  2004-2005 Stefan Reuter
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
package org.asteriskjava.manager;

import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.asteriskjava.manager.action.CommandAction;
import org.asteriskjava.manager.action.OriginateAction;
import org.asteriskjava.manager.action.QueueStatusAction;
import org.asteriskjava.manager.action.StatusAction;
import org.asteriskjava.manager.event.ConnectEvent;
import org.asteriskjava.manager.event.DisconnectEvent;
import org.asteriskjava.manager.event.HangupEvent;
import org.asteriskjava.manager.event.JoinEvent;
import org.asteriskjava.manager.event.LeaveEvent;
import org.asteriskjava.manager.event.LinkEvent;
import org.asteriskjava.manager.event.ManagerEvent;
import org.asteriskjava.manager.event.NewCallerIdEvent;
import org.asteriskjava.manager.event.NewChannelEvent;
import org.asteriskjava.manager.event.NewExtenEvent;
import org.asteriskjava.manager.event.NewStateEvent;
import org.asteriskjava.manager.event.OriginateEvent;
import org.asteriskjava.manager.event.OriginateFailureEvent;
import org.asteriskjava.manager.event.QueueEntryEvent;
import org.asteriskjava.manager.event.QueueMemberEvent;
import org.asteriskjava.manager.event.QueueParamsEvent;
import org.asteriskjava.manager.event.RenameEvent;
import org.asteriskjava.manager.event.StatusEvent;
import org.asteriskjava.manager.event.UnlinkEvent;
import org.asteriskjava.manager.response.CommandResponse;
import org.asteriskjava.manager.response.ManagerResponse;
import org.asteriskjava.util.Log;
import org.asteriskjava.util.LogFactory;


/**
 * Default implementation of the AsteriskManager interface.
 * 
 * @see org.asteriskjava.manager.AsteriskManager
 * @author srt
 * @version $Id: DefaultAsteriskManager.java,v 1.23 2005/10/29 12:09:05 srt Exp $
 */
public class DefaultAsteriskManager
        implements
            AsteriskManager,
            ManagerEventHandler
{
    private static final Pattern SHOW_VERSION_FILES_PATTERN = Pattern
            .compile("^([\\S]+)\\s+Revision: ([0-9\\.]+)");

    private final Log logger = LogFactory.getLog(this.getClass());

    /**
     * The underlying manager connection used to talk to Asterisk.
     */
    private ManagerConnection connection;

    /**
     * A map of all active channel by their unique id.
     */
    private final Map<String, Channel> channels;

    /**
     * A map of ACD queues by there name.
     */
    private final Map<String, Queue> queues;

    /**
     * The version of the Asterisk server we are connected to.<br>
     * Contains <code>null</code> until lazily initialized.
     */
    private String version;

    /**
     * Holds the version of Asterisk's source files.<br>
     * That corresponds to the output of the CLI command
     * <code>show version files</code>.<br>
     * Contains <code>null</code> until lazily initialized.
     */
    private Map versions;

    /**
     * Flag to skip initializing queues as that results in a timeout on Asterisk
     * 1.0.x.
     */
    private boolean skipQueues;

    /**
     * Creates a new instance.
     */
    public DefaultAsteriskManager()
    {
        this.channels = Collections.synchronizedMap(new HashMap<String, Channel>());
        this.queues = Collections.synchronizedMap(new HashMap<String, Queue>());
    }

    /**
     * Creates a new instance.
     * 
     * @param connection the manager connection to use
     */
    public DefaultAsteriskManager(ManagerConnection connection)
    {
        this();
        this.connection = connection;
    }

    /**
     * Determines if queue status is retrieved at startup. If you don't need
     * queue information and still run Asterisk 1.0.x you can set this to
     * <code>true</code> to circumvent the startup delay caused by the missing
     * QueueStatusComplete event.<br>
     * Default is <code>false</code>.
     * 
     * @param skipQueues <code>true</code> to skip queue initialization,
     *            <code>false</code> to not skip.
     * @since 0.2
     */
    public void setSkipQueues(boolean skipQueues)
    {
        this.skipQueues = skipQueues;
    }

    public void setManagerConnection(ManagerConnection connection)
    {
        this.connection = connection;
    }

    public void initialize() throws TimeoutException, IOException,
            AuthenticationFailedException
    {
        connection.login();

        initializeChannels();
        initializeQueues();

        connection.addEventHandler(this);
    }

    private void initializeChannels() throws EventTimeoutException, IOException
    {
        ResponseEvents re;
        Iterator i;

        re = connection.sendEventGeneratingAction(new StatusAction());
        i = re.getEvents().iterator();
        while (i.hasNext())
        {
            ManagerEvent event;

            event = (ManagerEvent) i.next();
            if (event instanceof StatusEvent)
            {
                handleStatusEvent((StatusEvent) event);
            }
        }
    }

    private void initializeQueues() throws IOException
    {
        ResponseEvents re;
        Iterator i;

        if (skipQueues)
        {
            return;
        }

        try
        {
            re = connection.sendEventGeneratingAction(new QueueStatusAction());
        }
        catch (EventTimeoutException e)
        {
            // this happens with Asterisk 1.0.x as it doesn't send a
            // QueueStatusCompleteEvent
            re = e.getPartialResult();
        }

        i = re.getEvents().iterator();
        while (i.hasNext())
        {
            ManagerEvent event;

            event = (ManagerEvent) i.next();
            if (event instanceof QueueParamsEvent)
            {
                handleQueueParamsEvent((QueueParamsEvent) event);
            }
            else if (event instanceof QueueMemberEvent)
            {
                handleQueueMemberEvent((QueueMemberEvent) event);
            }
            else if (event instanceof QueueEntryEvent)
            {
                handleQueueEntryEvent((QueueEntryEvent) event);
            }
        }
    }

    /* Implementation of the AsteriskManager interface */

    public Call originateCall(Originate originate) throws TimeoutException,
            IOException
    {
        OriginateAction originateAction;
        ResponseEvents responseEvents;
        Long timeout;

        if (originate.getTimeout() == null)
        {
            timeout = new Long(30000);
        }
        else
        {
            timeout = originate.getTimeout();
        }

        originateAction = new OriginateAction();
        originateAction.setAccount(originate.getAccount());
        originateAction.setApplication(originate.getApplication());
        originateAction.setCallerId(originate.getCallerId());
        originateAction.setChannel(originate.getChannel());
        originateAction.setContext(originate.getContext());
        originateAction.setData(originate.getData());
        originateAction.setExten(originate.getExten());
        originateAction.setPriority(originate.getPriority());
        originateAction.setTimeout(timeout);
        originateAction.setVariables(originate.getVariables());

        // must set async to true to receive OriginateEvents.
        originateAction.setAsync(Boolean.TRUE);

        // 2000 ms extra for the OriginateFailureEvent should be fine
        responseEvents = connection.sendEventGeneratingAction(originateAction,
                timeout.longValue() + 2000);

        return originateEvent2Call((OriginateEvent) responseEvents.getEvents()
                .toArray()[0]);
    }

    /**
     * Returns a map of all active channel by their unique id.
     */
    public Map getChannels()
    {
        return channels;
    }

    public Map getQueues()
    {
        return queues;
    }

    public String getVersion()
    {
        if (version == null)
        {
            ManagerResponse response;
            try
            {
                response = connection.sendAction(new CommandAction(
                        "show version"));
                if (response instanceof CommandResponse)
                {
                    List result;

                    result = ((CommandResponse) response).getResult();
                    if (result.size() > 0)
                    {
                        version = (String) result.get(0);
                    }
                }
            }
            catch (Exception e)
            {
                logger.warn("Unable to send 'show version' command.", e);
            }
        }

        return version;
    }

    public int[] getVersion(String file)
    {
        String fileVersion = null;
        String[] parts;
        int[] intParts;

        if (versions == null)
        {
            Map<String, String> map;
            ManagerResponse response;

            map = new HashMap<String, String>();
            try
            {
                response = connection.sendAction(new CommandAction(
                        "show version files"));
                if (response instanceof CommandResponse)
                {
                    List<String> result;

                    result = ((CommandResponse) response).getResult();
                    for (int i = 2; i < result.size(); i++)
                    {
                        String line;
                        Matcher matcher;

                        line = (String) result.get(i);
                        matcher = SHOW_VERSION_FILES_PATTERN.matcher(line);
                        if (matcher.find())
                        {
                            String key = matcher.group(1);
                            String value = matcher.group(2);

                            map.put(key, value);
                        }
                    }

                    fileVersion = (String) map.get(file);
                    versions = map;
                }
            }
            catch (Exception e)
            {
                logger.warn("Unable to send 'show version files' command.", e);
            }
        }
        else
        {
            synchronized (versions)
            {
                fileVersion = (String) versions.get(file);
            }
        }

        if (fileVersion == null)
        {
            return null;
        }

        parts = fileVersion.split("\\.");
        intParts = new int[parts.length];

        for (int i = 0; i < parts.length; i++)
        {
            try
            {
                intParts[i] = Integer.parseInt(parts[i]);
            }
            catch (NumberFormatException e)
            {
                intParts[i] = 0;
            }
        }

        return intParts;
    }

    /* Implementation of the ManagerEventHandler interface */

    /**
     * Handles all events received from the asterisk server.<br>
     * Events are queued until channels and queues are initialized and then
     * delegated to the dispatchEvent method.
     */
    public void handleEvent(ManagerEvent event)
    {
        if (event instanceof ConnectEvent)
        {
            handleConnectEvent((ConnectEvent) event);
        }
        else if (event instanceof DisconnectEvent)
        {
            handleDisconnectEvent((DisconnectEvent) event);
        }
        else if (event instanceof NewChannelEvent)
        {
            handleNewChannelEvent((NewChannelEvent) event);
        }
        else if (event instanceof NewExtenEvent)
        {
            handleNewExtenEvent((NewExtenEvent) event);
        }
        else if (event instanceof NewStateEvent)
        {
            handleNewStateEvent((NewStateEvent) event);
        }
        else if (event instanceof NewCallerIdEvent)
        {
            handleNewCallerIdEvent((NewCallerIdEvent) event);
        }
        else if (event instanceof LinkEvent)
        {
            handleLinkEvent((LinkEvent) event);
        }
        else if (event instanceof UnlinkEvent)
        {
            handleUnlinkEvent((UnlinkEvent) event);
        }
        else if (event instanceof RenameEvent)
        {
            handleRenameEvent((RenameEvent) event);
        }
        else if (event instanceof HangupEvent)
        {
            handleHangupEvent((HangupEvent) event);
        }
        else if (event instanceof JoinEvent)
        {
            handleJoinEvent((JoinEvent) event);
        }
        else if (event instanceof LeaveEvent)
        {
            handleLeaveEvent((LeaveEvent) event);
        }
    }

    /* Private helper methods */

    protected void addChannel(Channel channel)
    {
        synchronized (channels)
        {
            channels.put(channel.getId(), channel);
        }
    }

    protected void removeChannel(Channel channel)
    {
        synchronized (channels)
        {
            channels.remove(channel.getId());
        }
    }

    protected void addQueue(Queue queue)
    {
        synchronized (queues)
        {
            queues.put(queue.getName(), queue);
        }
    }

    protected void removeQueue(Queue queue)
    {
        synchronized (queues)
        {
            queues.remove(queue.getName());
        }
    }

    protected void handleStatusEvent(StatusEvent event)
    {
        Channel channel;
        Extension extension;
        boolean isNew = false;

        channel = getChannelById(event.getUniqueId());
        if (channel == null)
        {
            channel = new Channel(event.getChannel(), event.getUniqueId());
            if (event.getSeconds() != null)
            {
                channel.setDateOfCreation(new Date(System.currentTimeMillis()
                        - (event.getSeconds().intValue() * 1000)));
            }
            isNew = true;
        }

        if (event.getContext() == null && event.getExtension() == null
                && event.getPriority() == null)
        {
            extension = null;
        }
        else
        {
            extension = new Extension(event.getDateReceived(), event
                    .getContext(), event.getExtension(), event.getPriority());
        }

        synchronized (channel)
        {
            channel.setCallerId(event.getCallerId());
            channel.setCallerIdName(event.getCallerIdName());
            channel.setAccount(event.getAccount());
            channel.setState(ChannelStateEnum.getEnum(event.getState()));
            channel.addExtension(extension);

            if (event.getLink() != null)
            {
                Channel linkedChannel = getChannelByName(event.getLink());
                if (linkedChannel != null)
                {
                    channel.setLinkedChannel(linkedChannel);
                    synchronized (linkedChannel)
                    {
                        linkedChannel.setLinkedChannel(channel);
                    }
                }
            }
        }

        if (isNew)
        {
            logger.info("Adding new channel " + channel.getName());
            addChannel(channel);
        }
    }

    /**
     * Resets the internal state when the connection to the asterisk server is
     * lost.
     */
    protected void handleDisconnectEvent(DisconnectEvent disconnectEvent)
    {
        // reset version information as it might have changed while Asterisk
        // restarted
        version = null;
        versions = null;

        channels.clear();
        queues.clear();
    }

    /**
     * Requests the current state from the asterisk server after the connection
     * to the asterisk server is restored.
     */
    protected void handleConnectEvent(ConnectEvent connectEvent)
    {
        try
        {
            initializeChannels();
        }
        catch (Exception e)
        {
            logger.error("Unable to initialize channels after reconnect.", e);
        }

        try
        {
            initializeQueues();
        }
        catch (IOException e)
        {
            logger.error("Unable to initialize queues after reconnect.", e);
        }
    }

    protected void handleQueueParamsEvent(QueueParamsEvent event)
    {
        Queue queue;
        boolean isNew = false;

        queue = (Queue) queues.get(event.getQueue());

        if (queue == null)
        {
            queue = new Queue(event.getQueue());
            isNew = true;
        }

        synchronized (queue)
        {
            queue.setMax(event.getMax());
        }

        if (isNew)
        {
            logger.info("Adding new queue " + queue.getName());
            addQueue(queue);
        }
    }

    protected void handleQueueMemberEvent(QueueMemberEvent event)
    {

    }

    protected void handleQueueEntryEvent(QueueEntryEvent event)
    {
        Queue queue = (Queue) queues.get(event.getQueue());
        Channel channel = getChannelByName(event.getChannel());

        if (queue == null)
        {
            logger.error("ignored QueueEntryEvent for unknown queue "
                    + event.getQueue());
            return;
        }
        if (channel == null)
        {
            logger.error("ignored QueueEntryEvent for unknown channel "
                    + event.getChannel());
            return;
        }

        if (!queue.getEntries().contains(channel))
        {
            queue.addEntry(channel);
        }
    }

    protected void handleJoinEvent(JoinEvent event)
    {
        Queue queue = (Queue) queues.get(event.getQueue());
        Channel channel = getChannelByName(event.getChannel());

        if (queue == null)
        {
            logger.error("ignored JoinEvent for unknown queue "
                    + event.getQueue());
            return;
        }
        if (channel == null)
        {
            logger.error("ignored JoinEvent for unknown channel "
                    + event.getChannel());
            return;
        }

        if (!queue.getEntries().contains(channel))
        {
            queue.addEntry(channel);
        }
    }

    protected void handleLeaveEvent(LeaveEvent event)
    {
        Queue queue = (Queue) queues.get(event.getQueue());
        Channel channel = getChannelByName(event.getChannel());

        if (queue == null)
        {
            logger.error("ignored LeaveEvent for unknown queue "
                    + event.getQueue());
            return;
        }
        if (channel == null)
        {
            logger.error("ignored LeaveEvent for unknown channel "
                    + event.getChannel());
            return;
        }

        if (queue.getEntries().contains(channel))
        {
            queue.removeEntry(channel);
        }
    }

    /**
     * Returns a channel by its name.
     * 
     * @param name name of the channel to return
     * @return the channel with the given name
     */
    public Channel getChannelByName(String name)
    {
        Channel channel = null;

        synchronized (channels)
        {
            Iterator channelIterator = channels.values().iterator();
            while (channelIterator.hasNext())
            {
                Channel tmp = (Channel) channelIterator.next();
                if (tmp.getName() != null && tmp.getName().equals(name))
                {
                    channel = tmp;
                }
            }
        }
        return channel;
    }

    /**
     * Returns a channel by its unique id.
     * 
     * @param id the unique id of the channel to return
     * @return the channel with the given unique id
     */
    public Channel getChannelById(String id)
    {
        Channel channel = null;

        synchronized (channels)
        {
            channel = (Channel) channels.get(id);
        }
        return channel;
    }

    protected void handleNewChannelEvent(NewChannelEvent event)
    {
        Channel channel = new Channel(event.getChannel(), event.getUniqueId());

        channel.setDateOfCreation(event.getDateReceived());
        channel.setCallerId(event.getCallerId());
        channel.setCallerIdName(event.getCallerIdName());
        channel.setState(ChannelStateEnum.getEnum(event.getState()));

        logger.info("Adding channel " + channel.getName());
        addChannel(channel);
    }

    protected void handleNewExtenEvent(NewExtenEvent event)
    {
        Channel channel;
        Extension extension;

        channel = getChannelById(event.getUniqueId());
        if (channel == null)
        {
            logger.error("Ignored NewExtenEvent for unknown channel "
                    + event.getChannel());
            return;
        }

        extension = new Extension(event.getDateReceived(), event.getContext(),
                event.getExtension(), event.getPriority(), event
                        .getApplication(), event.getAppData());

        synchronized (channel)
        {
            channel.addExtension(extension);
        }
    }

    protected void handleNewStateEvent(NewStateEvent event)
    {
        Channel channel = getChannelById(event.getUniqueId());

        if (channel == null)
        {
            logger.error("Ignored NewStateEvent for unknown channel "
                    + event.getChannel());
            return;
        }

        synchronized (channel)
        {
            channel.setState(ChannelStateEnum.getEnum(event.getState()));
        }
    }

    protected void handleNewCallerIdEvent(NewCallerIdEvent event)
    {
        Channel channel = getChannelById(event.getUniqueId());

        if (channel == null)
        {
            logger.error("Ignored NewCallerIdEvent for unknown channel "
                    + event.getChannel());
            return;
        }

        synchronized (channel)
        {
            channel.setCallerId(event.getCallerId());
            channel.setCallerIdName(event.getCallerIdName());
        }
    }

    protected void handleHangupEvent(HangupEvent event)
    {
        Channel channel = getChannelById(event.getUniqueId());
        if (channel == null)
        {
            logger.error("Ignored HangupEvent for unknown channel "
                    + event.getChannel());
            return;
        }

        synchronized (channel)
        {
            channel.setState(ChannelStateEnum.HUNGUP);
        }

        logger.info("Removing channel " + channel.getName() + " due to hangup");
        removeChannel(channel);
    }

    protected void handleLinkEvent(LinkEvent event)
    {
        Channel channel1 = (Channel) channels.get(event.getUniqueId1());
        Channel channel2 = (Channel) channels.get(event.getUniqueId2());

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
        synchronized (this)
        {
            channel1.setLinkedChannel(channel2);
            channel2.setLinkedChannel(channel1);
        }
    }

    protected void handleUnlinkEvent(UnlinkEvent event)
    {
        Channel channel1 = getChannelByName(event.getChannel1());
        Channel channel2 = getChannelByName(event.getChannel2());

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
            channel1.setLinkedChannel(null);
        }

        synchronized (channel2)
        {
            channel2.setLinkedChannel(null);
        }
    }

    protected void handleRenameEvent(RenameEvent event)
    {
        Channel channel = getChannelById(event.getUniqueId());

        if (channel == null)
        {
            logger
                    .error("Ignored RenameEvent for unknown channel with uniqueId "
                            + event.getUniqueId());
            return;
        }

        logger.info("Renaming channel '" + channel.getName() + "' to '"
                + event.getNewname() + "'");
        channel.setName(event.getNewname());
    }

    protected Call originateEvent2Call(OriginateEvent event)
    {
        Call call;
        Channel channel;

        channel = (Channel) channels.get(event.getUniqueId());
        call = new Call();
        call.setUniqueId(event.getUniqueId());
        call.setChannel(channel);
        call.setStartTime(event.getDateReceived());
        if (event instanceof OriginateFailureEvent)
        {
            call.setEndTime(event.getDateReceived());
        }
        call.setReason(event.getReason());

        return call;
    }
}
