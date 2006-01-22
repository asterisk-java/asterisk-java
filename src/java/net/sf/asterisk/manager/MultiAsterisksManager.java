/*
 * Created on 1 f�vr. 2005 by Pierre-Yves ROGER.
 *
 */
package net.sf.asterisk.manager;

import java.io.IOException;
import java.util.*;

import net.sf.asterisk.manager.action.*;
import net.sf.asterisk.manager.event.*;
import net.sf.asterisk.util.Log;
import net.sf.asterisk.util.LogFactory;

/**
 * @author PY
 */
public class MultiAsterisksManager implements AsteriskManager, ManagerEventHandler
{
    private final Log log = LogFactory.getLog(this.getClass());
    /**
     * A map of all Asterisk servers connections by their unique hostname.
     */
    private Map managerConnections;

    /**
     * A map of all active channel by theirs hostname of connection and unique id. keyMap ->
     * "hostname:uniqueId"
     */
    private Map channels;
    private Map queues;
    private List queuedEvents;

    /**
     * A map of all "Initiallize flag" per connection. keyMap -> "hostname"
     */
    private Map initialized;

    public MultiAsterisksManager()
    {
        this.channels = Collections.synchronizedMap(new HashMap());
        this.queues = Collections.synchronizedMap(new HashMap());
        this.queuedEvents = Collections.synchronizedList(new ArrayList());
        this.managerConnections = Collections.synchronizedMap(new HashMap());
        this.initialized = Collections.synchronizedMap(new HashMap());
    }

    public void addManagerConnection(ManagerConnection connection)
    {
        managerConnections.put(connection.getAsteriskServer(), connection);

        Hashtable initializedBoolean = new Hashtable(3, 1);
        initializedBoolean.put("channelsInitialized", new Boolean(false));
        // TODO fix detection of queuesInitialized
        initializedBoolean.put("queuesInitialized", new Boolean(true));
        initializedBoolean.put("initialized", new Boolean(false));

        initialized.put(connection.getAsteriskServer(), initializedBoolean);

    }

    public void initialize() throws TimeoutException, IOException, AuthenticationFailedException
    {
        Iterator iterConnect = managerConnections.values().iterator();
        while (iterConnect.hasNext())
        {
            ManagerConnection connection = (ManagerConnection) iterConnect.next();
            connection.addEventHandler(this);
            connection.login();
            connection.sendAction(new StatusAction());
            connection.sendAction(new QueueStatusAction());
        }
    }

    /**
     * Returns a map of all active channel by their unique id: "hostname:ChannelId".
     */
    public Map getChannels()
    {
        return channels;
    }

    public Map getQueues()
    {
        return queues;
    }

    /**
     * Handles all events received from the asterisk server.<br>
     * Events are queued until channels and queues are initialized and then delegated to the
     * dispatchEvent method.
     */
    public void handleEvent(ManagerEvent event)
    {
        System.out.println("received: " + event);

        Hashtable initializedBoolean = (Hashtable) initialized.get((AsteriskServer) event.getSource());

        if (!((Boolean) initializedBoolean.get("initialized")).booleanValue())
        {
            if (event instanceof StatusEvent)
            {
                handleStatusEvent((StatusEvent) event);
            }
            else if (event instanceof StatusCompleteEvent)
            {
                handleStatusCompleteEvent((StatusCompleteEvent) event);
            }
            else if (event instanceof QueueParamsEvent)
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
            else
            {
                queuedEvents.add(event);
            }

            if (((Boolean) initializedBoolean.get("channelsInitialized")).booleanValue()
                    && ((Boolean) initializedBoolean.get("queuesInitialized")).booleanValue())
            {
                Iterator i = queuedEvents.iterator();

                while (i.hasNext())
                {
                    ManagerEvent queuedEvent = (ManagerEvent) i.next();
                    dispatchEvent(queuedEvent);
                    i.remove();
                }

                initializedBoolean.put("initialized", new Boolean(true));
            }
        }
        else
        {
            dispatchEvent(event);
        }
    }

    protected void dispatchEvent(ManagerEvent event)
    {
        if (event instanceof NewChannelEvent)
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
    }

    protected void addChannel(Channel channel)
    {
        channels.put(channel.getAsteriskServer().getHostname() + "/" + channel.getId(), channel);
    }

    protected void removeChannel(Channel channel)
    {
        channels.remove(channel.getAsteriskServer().getHostname() + "/" + channel.getId());
    }

    protected void addQueue(Queue queue)
    {
        queues.put(queue.getName(), queue);
    }

    protected void removeQueue(Queue queue)
    {
        queues.remove(queue.getName());
    }

    protected void handleStatusEvent(StatusEvent event)
    {
        Channel channel;
        boolean isNew = false;

        channel = (Channel) channels
                .get(((AsteriskServer) event.getSource()).getHostname() + "/" + event.getUniqueId());
        if (channel == null)
        {
            channel = new Channel(event.getChannel(), event.getUniqueId(), (AsteriskServer) event.getSource());
            if (event.getSeconds() != null)
            {
                channel
                        .setDateOfCreation(new Date(System.currentTimeMillis() - (event.getSeconds().intValue() * 1000)));
            }
            isNew = true;
        }

        synchronized (channel)
        {
            channel.setCallerId(event.getCallerId());
            channel.setAccount(event.getAccount());
            channel.setState(ChannelStateEnum.getEnum(event.getState()));
            // TODO handle extension
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
            log.info("Adding new channel " + channel.getName() + ", from server "
                    + channel.getAsteriskServer().getHostname());
            addChannel(channel);
        }
    }

    protected void handleStatusCompleteEvent(StatusCompleteEvent event)
    {
        log.info("Channels are now initialized");
        Hashtable initializedBoolean = (Hashtable) initialized.get((AsteriskServer) event.getSource());
        initializedBoolean.put("channelsInitialized", new Boolean(true));
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
            log.info("Adding new queue " + queue.getName());
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
            log.error("ignored QueueEntryEvent for unknown queue " + event.getQueue());
            return;
        }
        if (channel == null)
        {
            log.error("ignored QueueEntryEvent for unknown channel " + event.getChannel());
            return;
        }

        if (!queue.getEntries().contains(channel))
        {
            queue.addEntry(channel);
        }
    }

    /**
     * Returns a channel by its name.
     * 
     * @param name name of the channel to return
     * @return the channel with the given name
     */
    private Channel getChannelByName(String name)
    {
        // TODO must get unique
        Channel channel = null;
        Iterator channelIterator = channels.values().iterator();
        while (channelIterator.hasNext())
        {
            Channel tmp = (Channel) channelIterator.next();
            if (tmp.getName() != null && tmp.getName().equals(name))
            {
                channel = tmp;
            }
        }
        return channel;
    }

    protected void handleNewChannelEvent(NewChannelEvent event)
    {
        Channel channel = new Channel(event.getChannel(), event.getUniqueId(), (AsteriskServer) event.getSource());

        channel.setCallerId(event.getCallerId());
        channel.setState(ChannelStateEnum.getEnum(event.getState()));

        log.info("Adding channel " + channel.getName() + ", on server " + channel.getAsteriskServer().getHostname());
        addChannel(channel);
    }

    protected void handleNewExtenEvent(NewExtenEvent event)
    {
        Channel channel = (Channel) channels.get(((AsteriskServer) event.getSource()).getHostname() + "/"
                + event.getUniqueId());
        if (channel == null)
        {
            log.error("Ignored NewExtenEvent for unknown channel " + event.getChannel());
            return;
        }

        synchronized (channel)
        {
            // TODO handle new extension
        }
    }

    protected void handleNewStateEvent(NewStateEvent event)
    {
        Channel channel = (Channel) channels.get(((AsteriskServer) event.getSource()).getHostname() + "/"
                + event.getUniqueId());
        if (channel == null)
        {
            log.error("Ignored NewStateEvent for unknown channel " + event.getChannel());
            return;
        }

        channel.setState(ChannelStateEnum.getEnum(event.getState()));
    }

    protected void handleHangupEvent(HangupEvent event)
    {
        Channel channel = (Channel) channels.get(((AsteriskServer) event.getSource()).getHostname() + "/"
                + event.getUniqueId());
        if (channel == null)
        {
            log.error("Ignored HangupEvent for unknown channel " + event.getChannel());
            return;
        }

        synchronized (channel)
        {
            channel.setState(ChannelStateEnum.HUNGUP);
        }

        log.info("Removing channel " + channel.getName() + " due to hangup");
        removeChannel(channel);
    }

    protected void handleLinkEvent(LinkEvent event)
    {
        Channel channel1 = (Channel) channels.get(((AsteriskServer) event.getSource()).getHostname() + "/"
                + event.getUniqueId1());
        Channel channel2 = (Channel) channels.get(((AsteriskServer) event.getSource()).getHostname() + "/"
                + event.getUniqueId2());

        if (channel1 == null)
        {
            log.error("Ignored LinkEvent for unknown channel " + event.getChannel1());
            return;
        }
        if (channel2 == null)
        {
            log.error("Ignored LinkEvent for unknown channel " + event.getChannel2());
            return;
        }

        log.info("Linking channels " + channel1.getName() + " and " + channel2.getName());
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
            log.error("Ignored UnlinkEvent for unknown channel " + event.getChannel1());
            return;
        }
        if (channel2 == null)
        {
            log.error("Ignored UnlinkEvent for unknown channel " + event.getChannel2());
            return;
        }

        log.info("Unlinking channels " + channel1.getName() + " and " + channel2.getName());
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
        Channel channel = (Channel) channels.get(((AsteriskServer) event.getSource()).getHostname() + "/"
                + event.getUniqueId());

        log.info("Renaming channel '" + channel.getName() + "' to '" + event.getNewname() + "'");
        channel.setName(event.getNewname());
    }

    /*
     * (non-Javadoc)
     * 
     * @see net.sf.asterisk.manager.AsteriskManager#originateCall(net.sf.asterisk.manager.action.OriginateAction)
     */
    public Call originateCall(Originate originate) throws TimeoutException, IOException
    {
        // TODO Auto-generated method stub
        return null;
    }

    public int[] getVersion(String file)
    {
        throw new UnsupportedOperationException();
    }

    public String getVersion()
    {
        throw new UnsupportedOperationException();
    }
}
