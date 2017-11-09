package org.asteriskjava.pbx.internal.core;

import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

import org.asteriskjava.manager.TimeoutException;
import org.asteriskjava.pbx.Channel;
import org.asteriskjava.pbx.EndPoint;
import org.asteriskjava.pbx.InvalidChannelName;
import org.asteriskjava.pbx.ListenerPriority;
import org.asteriskjava.pbx.asterisk.wrap.actions.StatusAction;
import org.asteriskjava.pbx.asterisk.wrap.events.HangupEvent;
import org.asteriskjava.pbx.asterisk.wrap.events.ManagerEvent;
import org.asteriskjava.pbx.asterisk.wrap.events.MasqueradeEvent;
import org.asteriskjava.pbx.asterisk.wrap.events.NewChannelEvent;
import org.asteriskjava.pbx.asterisk.wrap.events.RenameEvent;
import org.asteriskjava.pbx.asterisk.wrap.events.ResponseEvent;
import org.asteriskjava.pbx.asterisk.wrap.events.ResponseEvents;
import org.asteriskjava.pbx.asterisk.wrap.events.StatusEvent;
import org.asteriskjava.util.Log;
import org.asteriskjava.util.LogFactory;

/**
 * The LiveChannelManager keeps a list of all of the live channels present on an
 * asterisk system. For this to work it needs to control the stream of asterisk
 * events consumed by others. This need is caused by the fact that Asterisk can
 * rename a channel many times after its comes up. When a channel is renamed
 * Asterisk sends a rename event. After the rename event all future asterisk
 * events will refer to the channel by its new name. Masquerade events pose
 * further issues in that a channel can be replaced mid way through a call. The
 * LiveChannelManager works with the ChannelProxy to manage this process. The
 * issue is that if multiple classes are accessing the LiveChannelManager and
 * those classes are consuming asterisk events form their own direct source then
 * they may be behind or ahead of the LiveChannelManager in processing events.
 * The result is that when attempting to access a channel by name from the
 * LiveChannelManager the channel name may not match as the LiveChannelManager
 * may have processed a rename event whilst the consumer may still be processing
 * older events which are still referring to the channel via its old name. The
 * first part of the solution is to wrap the asterisk-java events with our own
 * events which pass an iChannel interface rather than a raw channel name.
 * Because the iChannel interface is a handle to a channel managed via the
 * LiveChannelManager then the name will be updated during the rename. To ensure
 * that events are processed in an orderly fashion you MUST only obtain an event
 * listener via AsteriskPBX.addListener(); The second part of the solution is
 * the ChannelProxy. The ChannelProxy handles masquerading of channels where in
 * a live channel is replaced with a new channel part way through a call. This
 * can happen when transfering a call, parking a call or other similar actions.
 * The ChannelProxy implements the iChannel interface and is affectively hidden
 * from consumers of iChannels. The LiveChannelManager uses the proxy to replace
 * a live channel during a masqurade event. A consumer holding an iChannel
 * handle will not even be aware (and does not need to be) that the underlying
 * channel has been replaced.
 * 
 * @author bsutton
 */
public class LiveChannelManager implements FilteredManagerListener<ManagerEvent>
{
    private static final Log logger = LogFactory.getLog(LiveChannelManager.class);

    /**
     * A collection of all of the live proxies in the system. We monitor the
     * channels and remove them as they hangup. The hash is keyed by the
     * channel's name. e.g. SIP/100-000000100
     */
    private final List<ChannelProxy> _liveChannels = new CopyOnWriteArrayList<>();

    public LiveChannelManager()
    {
        CoherentManagerConnection.getInstance().addListener(this);

    }

    /**
     * Find all the channels that came into existence before startup. This can't
     * be done during the Constructor call, because it requires calls back to
     * AsteriskPBX which isn't finished constructing until after the Constructor
     * returns.
     */
    void performPostCreationTasks()
    {
        StatusAction statusAction = new StatusAction();
        try
        {
            ResponseEvents events = CoherentManagerConnection.sendEventGeneratingAction(statusAction, 1000);
            for (ResponseEvent event : events.getEvents())
            {
                if (event instanceof StatusEvent)
                {
                    // do nothing. Creating the events will register the
                    // channels, which is after all what we are trying to do.
                }
            }

        }
        catch (IllegalArgumentException | IllegalStateException | IOException | TimeoutException e)
        {
            logger.error(e, e);
        }

    }

    public ChannelProxy getChannelByEndPoint(EndPoint endPoint)
    {
        ChannelProxy connectedChannel = null;
        for (final ChannelProxy channel : _liveChannels)
        {
            if (channel.isConnectedTo(endPoint))
            {
                connectedChannel = channel;
                break;
            }

        }
        return connectedChannel;
    }

    public void add(ChannelProxy proxy)
    {
        synchronized (this._liveChannels)
        {
            ChannelProxy index = findProxy(proxy);
            if (index == null)
                this._liveChannels.add(proxy);
        }
        logger.debug("Adding liveChannel " + proxy);

        dumpProxies(proxy, "Add");
        sanityCheck();

    }

    private void dumpProxies(ChannelProxy proxy, String cause)
    {
        if (logger.isDebugEnabled())
        {
            logger.debug("Dump of LiveChannels, cause:" + cause + ": " + proxy); //$NON-NLS-2$
            for (ChannelProxy aProxy : _liveChannels)
            {
                logger.debug("ChannelProxy: " + aProxy);
            }
        }
    }

    public void remove(ChannelProxy proxy)
    {

        ChannelProxy index = findProxy(proxy);
        if (index != null)
        {
            logger.info("Removing liveChannel " + proxy);
            this._liveChannels.remove(index);
        }
        dumpProxies(proxy, "Removing");

    }

    public ChannelProxy findChannel(String extendedChannelName, String uniqueID)
    {
        ChannelProxy proxy = null;
        logger.debug("Trying to find channel " + extendedChannelName + " " + uniqueID);

        String localUniqueId = uniqueID;
        if (localUniqueId == null)
            localUniqueId = ChannelImpl.UNKNOWN_UNIQUE_ID;

        // In order to get the 'best' match we first match each by unique id.
        // Sometimes we can have two channels with the same name but
        // different

        if (localUniqueId.compareTo(ChannelImpl.UNKNOWN_UNIQUE_ID) != 0)
        {

            for (ChannelProxy aChannel : _liveChannels)
            {
                if (aChannel.sameUniqueID(localUniqueId))
                {
                    proxy = aChannel;
                    break;
                }
            }
        }

        // If we don't have a match from the first pass and the new uniqueID
        // is unknown
        // then do a search matching by name.
        if (proxy == null)
        {
            for (ChannelProxy aChannel : _liveChannels)
            {

                if (aChannel.isSame(extendedChannelName, localUniqueId))
                {
                    proxy = aChannel;
                    break;
                }
            }
        }

        if (proxy == null)
        {
            logger.debug("Failed to match channel to any of...");
            for (ChannelProxy aChannel : _liveChannels)
            {
                logger.debug(aChannel);
            }

        }

        return proxy;
    }

    private ChannelProxy findProxy(Channel original)
    {
        for (ChannelProxy aChannel : _liveChannels)
        {
            if (aChannel.isSame(original))
            {
                return aChannel;
            }
        }
        return null;
    }

    @Override
    public HashSet<Class< ? extends ManagerEvent>> requiredEvents()
    {
        HashSet<Class< ? extends ManagerEvent>> required = new HashSet<>();

        required.add(MasqueradeEvent.class);
        required.add(RenameEvent.class);
        required.add(HangupEvent.class);

        // add NewChannelEvent so all channels are added to the
        // LiveChannelManager
        required.add(NewChannelEvent.class);

        return required;
    }

    ChannelProxy findProxyById(String id)
    {
        for (ChannelProxy aChannel : _liveChannels)
        {
            if (("" + aChannel.getIdentity()).equals(id))
            {
                return aChannel;
            }
        }
        return null;
    }

    @Override
    public void onManagerEvent(ManagerEvent event)
    {
        if (event instanceof MasqueradeEvent)
        {
            MasqueradeEvent masq = (MasqueradeEvent) event;
            ChannelProxy originalIndex = findProxy(masq.getOriginal());
            ChannelProxy cloneIndex = findProxy(masq.getClone());
            if (originalIndex != null && cloneIndex != null)
            {
                ChannelProxy originalProxy = (ChannelProxy) masq.getOriginal();
                ChannelProxy cloneProxy = (ChannelProxy) masq.getClone();
                try
                {
                    // After the masquerade the two underlying channels will
                    // have been switched
                    // between the two proxies.
                    // At this point the cloneProxy will be owned by a Peer
                    // (as they process NewChannelEvents)
                    // as such we cannot just discard the cloneProxy. The
                    // simplest solution then
                    // is to put the clone channel into the existing
                    // channel, as this is the core reason we have a proxy,
                    // and then put the original channel into the
                    // cloneProxy. At the end of this manuvour
                    // the Peer will still have two proxy which point to the
                    // two different channels.
                    // This is fine for the Peer as it doesn't attach any
                    // special meaning to any channel
                    // it just needs a list of all channels associated with
                    // the Peer.
                    // The original channel will shortly receive a hangup in
                    // which case the Peer will
                    // remove it from its list of channels and the Peer will
                    // be back to having
                    // one channel which will be the clone channel which is
                    // now the active channel
                    // and everyone will be happy.
                    originalProxy.masquerade(cloneProxy);
                    dumpProxies(cloneProxy, "Masquerade");
                    sanityCheck();
                }
                catch (InvalidChannelName e)
                {
                    logger.error(e, e);

                }

            }
            else
                logger.error("Either the clone or original channelProxy was missing during a masquerade: cloneIndex="
                        + cloneIndex + " originalIndex=" + originalIndex);

        }
        if (event instanceof RenameEvent)
        {
            RenameEvent rename = (RenameEvent) event;

            ChannelProxy oldChannel = findProxy(rename.getChannel());
            if (oldChannel != null)
            {
                try
                {
                    oldChannel.rename(rename.getNewName(), rename.getUniqueId());

                    dumpProxies(oldChannel, "RenameEvent");
                    sanityCheck();
                }
                catch (InvalidChannelName e)
                {
                    logger.error(e, e);
                }
            }
            else
            {
                String message = "Unable to rename channel -> Failed to find channel " + rename.getChannel();
                logger.warn(message);
                dumpProxies(null, message);
            }
        }
        else if (event instanceof HangupEvent)
        {
            // We need to process every hangup event that goes through the
            // system
            // as the LiveChannelManager has a Channel added for every channel
            // that is created via Asterisk even if it has nothing to
            // do with NJR. This is because channels are created
            // whenever an event arrives before we actually can determine
            // if we are interested in the event and its associated channels.
            HangupEvent hangup = (HangupEvent) event;
            // Yes I've seen a hangup where the channel was null, who knows
            // why?

            if (hangup.getChannel() != null)
            {

                ChannelProxy proxy = findProxy(hangup.getChannel());
                if (proxy != null)
                {
                    logger.debug("Removing proxy " + proxy);

                    this._liveChannels.remove(proxy);
                    logger.debug("Removing liveChannel " + proxy);
                    proxy.getChannel().notifyHangupListeners(hangup.getCause(), hangup.getCauseTxt());
                    dumpProxies(proxy, "HangupEvent");
                }

            }
            else
            {
                logger.error("Didn't remove hungup channel");
            }
        }
    }

    @Override
    public String getName()
    {
        return "LiveChannelManager";
    }

    @Override
    public ListenerPriority getPriority()
    {
        return ListenerPriority.CRITICAL;
    }

    public void sanityCheck()
    {

        if (logger.isDebugEnabled())
        {
            logger.error("Performing Sanity Check");
            Set<String> channels = new HashSet<>();
            for (ChannelProxy channel : _liveChannels)
            {
                if (!channels.add(channel.getChannel().getExtendedChannelName()))
                {
                    logger.error(
                            "Multiple channels by the name " + channel.getChannel().getExtendedChannelName() + " exist");
                    for (ChannelProxy channel2 : _liveChannels)
                    {
                        if (channel2.getChannel().getExtendedChannelName()
                                .equals(channel.getChannel().getExtendedChannelName()))
                        {
                            logger.error(channel2);
                        }
                    }
                    Exception ex = new Exception("called from here");
                    logger.error(ex, ex);
                }

            }
        }
    }

    public List<ChannelProxy> getChannelList()
    {
        List<ChannelProxy> channels = new LinkedList<>();
        channels.addAll(_liveChannels);
        return channels;
    }

}
