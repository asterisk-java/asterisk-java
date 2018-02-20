package org.asteriskjava.pbx.internal.core;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.asteriskjava.pbx.Channel;
import org.asteriskjava.pbx.EndPoint;
import org.asteriskjava.pbx.ListenerPriority;
import org.asteriskjava.pbx.NewExtensionListener;
import org.asteriskjava.pbx.PBXFactory;
import org.asteriskjava.pbx.asterisk.wrap.actions.SipPeersAction;
import org.asteriskjava.pbx.asterisk.wrap.actions.StatusAction;
import org.asteriskjava.pbx.asterisk.wrap.events.DndStateEvent;
import org.asteriskjava.pbx.asterisk.wrap.events.ManagerEvent;
import org.asteriskjava.pbx.asterisk.wrap.events.MasqueradeEvent;
import org.asteriskjava.pbx.asterisk.wrap.events.NewChannelEvent;
import org.asteriskjava.pbx.asterisk.wrap.events.NewStateEvent;
import org.asteriskjava.pbx.asterisk.wrap.events.PeerEntryEvent;
import org.asteriskjava.pbx.asterisk.wrap.events.PeerStatusEvent;
import org.asteriskjava.pbx.asterisk.wrap.events.PeerlistCompleteEvent;
import org.asteriskjava.pbx.asterisk.wrap.events.StatusCompleteEvent;
import org.asteriskjava.pbx.asterisk.wrap.events.StatusEvent;
import org.asteriskjava.pbx.internal.managerAPI.EventListenerBaseClass;
import org.asteriskjava.util.Log;
import org.asteriskjava.util.LogFactory;

/*
 * this class tracks the status of all peers on asterisk.
 */
public class PeerMonitor extends EventListenerBaseClass implements Runnable
{

    private static final Log logger = LogFactory.getLog(PeerMonitor.class);

    LinkedList<Peer> peerList = new LinkedList<>();

    boolean initSip = false;

    private final Thread markAndSweepThread;

    private static PeerMonitor self;

    private static NewExtensionListener listener;

    /**
     * needs to notify PhoneBookDisplayController.getInstance().addExtensions();
     * 
     * @param _listener
     */
    public synchronized static void init(final NewExtensionListener _listener)
    {
        PeerMonitor.listener = _listener;

        if (PeerMonitor.self == null)
        {
            PeerMonitor.self = new PeerMonitor();
        }
        else if (_listener != null)
        {
            logger.error("Call to PeerMonitor.init, but it's already initialized. Listener will not be set");
        }
    }

    public static synchronized PeerMonitor getInstance()
    {
        if (PeerMonitor.self == null)
        {
            throw new IllegalStateException("You must call PeerMonitor.init()"); //$NON-NLS-1$
        }
        return PeerMonitor.self;
    }

    private PeerMonitor()
    {
        super("PeerMonitor", PBXFactory.getActivePBX()); //$NON-NLS-1$
        this.peerList = new LinkedList<>();
        this.startListener();
        this.addSipsToMonitor();
        try
        {
            if (PeerMonitor.listener != null)
            {
                PeerMonitor.listener.newExtension();
            }
            else
            {
                logger.warn("Peer monitor listener is null");
            }
        }
        catch (final Exception e)
        {
            PeerMonitor.logger.error(e, e);
        }

        this.markAndSweepThread = new Thread(this);
        this.markAndSweepThread.setName("PeerMonitor-MarkAndSweep"); //$NON-NLS-1$
        this.markAndSweepThread.setDaemon(true);
        this.markAndSweepThread.start();

    }

    public void addSipsToMonitor()
    {
        /*
         * request asterisk to send a list of sip peers. The monitor will
         * dynamically add the peers to its list
         */
        this.initSip = false;

        final StatusAction sa = new StatusAction();
        final SipPeersAction t = new SipPeersAction();
        try
        {
            AsteriskPBX pbx = (AsteriskPBX) PBXFactory.getActivePBX();
            pbx.sendAction(t, 5000);
            pbx.sendAction(sa, 5000);
        }
        catch (final Exception e)
        {
            PeerMonitor.logger.error(e, e);
        }

    }

    synchronized public Peer registerPeer(final Channel newChannel)
    {
        final Peer peer = this.registerPeer(newChannel.getEndPoint());
        return peer;
    }

    synchronized public Peer registerPeer(final EndPoint endPoint)
    {
        if (endPoint.isLocal())
        {
            return null;
        }

        Peer peer = this.findPeer(endPoint);
        if (peer == null)
        {
            peer = new Peer(endPoint);
            this.peerList.add(peer);
        }

        return peer;
    }

    synchronized public Peer findPeer(final EndPoint peerEndPoint)
    {
        Peer found = null;

        for (final Peer peer : this.peerList)
        {
            if (peer.getEndPoint().isSame(peerEndPoint))
            {
                found = peer;
                break;
            }
        }
        return found;
    }

    @SuppressWarnings("unchecked")
    public Iterator<Peer> getIterator()
    {
        final List<Peer> clone = (LinkedList<Peer>) this.peerList.clone();
        final List<Peer> tmpList = clone;
        return tmpList.iterator();

    }

    @Override
    public HashSet<Class< ? extends ManagerEvent>> requiredEvents()
    {
        HashSet<Class< ? extends ManagerEvent>> required = new HashSet<>();

        required.add(NewChannelEvent.class);
        required.add(PeerStatusEvent.class);
        required.add(PeerEntryEvent.class);
        required.add(PeerlistCompleteEvent.class);
        required.add(NewStateEvent.class);
        required.add(StatusEvent.class);
        required.add(StatusCompleteEvent.class);
        required.add(DndStateEvent.class);

        return required;
    }

    @Override
    synchronized public void onManagerEvent(final ManagerEvent event)
    {
        /*
         * This function is called from the base class. Here we process events
         * we are interested in.
         */

        if (event instanceof PeerStatusEvent)
        {
            this.handleEvent((PeerStatusEvent) event);
        }
        else if (event instanceof PeerlistCompleteEvent)
        {
            this.handleEvent((PeerlistCompleteEvent) event);
        }
        else if (event instanceof PeerEntryEvent)
        {
            this.handleEvent((PeerEntryEvent) event);
        }
        else if (event instanceof NewChannelEvent)
        {
            this.handleEvent((NewChannelEvent) event);
        }
        else if (event instanceof MasqueradeEvent)
        {
            this.handleEvent((MasqueradeEvent) event);
        }
        else if (event instanceof StatusEvent)
        {
            this.handleEvent((StatusEvent) event);
        }
        else if (event instanceof StatusCompleteEvent)
        {
            this.handleEvent((StatusCompleteEvent) event);
        }
        else if (event instanceof NewStateEvent)
        {
            this.handleEvent((NewStateEvent) event);
        }
    }

    private void handleEvent(NewStateEvent event)
    {
        for (Peer peer : this.peerList)
        {
            peer.handleEvent(event);
        }
    }

    private void handleEvent(final NewChannelEvent event)
    {
        for (Peer peer : this.peerList)
        {
            peer.handleEvent(event);
        }
    }

    private void handleEvent(final MasqueradeEvent event)
    {
        for (Peer peer : this.peerList)
        {
            peer.handleEvent(event);
        }
    }

    private void handleEvent(final StatusEvent event)
    {
        for (Peer peer : this.peerList)
        {
            peer.handleEvent(event);
        }
    }

    private void handleEvent(final PeerEntryEvent event)
    {
        final EndPoint endPoint = event.getPeer();
        this.registerPeer(endPoint);
    }

    private void handleEvent(final PeerlistCompleteEvent b)
    {
        this.initSip = true;
    }

    /**
     * @param event
     */
    private void handleEvent(final PeerStatusEvent event)
    {
        this.registerPeer(event.getPeer());
    }

    /**
     * We receive the StatusComplete event once the Mark and Sweep channel
     * operation has completed. We now call endSweep which will remove any
     * channels that were not marked during the operation.
     * 
     * @param event
     */
    private synchronized void handleEvent(final StatusCompleteEvent event)
    {

        for (final Peer peer : this.peerList)
        {
            peer.endSweep();
        }
        PeerMonitor.logger.debug("Channel Mark and Sweep complete"); //$NON-NLS-1$
    }

    boolean isInitialized()
    {
        return this.initSip;
    }

    public void stop()
    {
        this.close();

    }

    /**************************************************************************************
     * Mark and Sweep logic follows
     **************************************************************************************/

    /**
     * Runs the mark and sweep operation every 120 seconds on all channels to
     * clean up any channels that have died and for which (for some reason) we
     * missed the hangup event.
     */
    @Override
    public void run()
    {
        while (true)
        {
            try
            {
                Thread.sleep(120000);
                PeerMonitor.getInstance().startSweep();
            }
            catch (final InterruptedException e)
            {
                PeerMonitor.logger.error(e, e);

            }
        }

    }

    /**
     * Check every channel to make certain they are still active. We do this in
     * case we missed a hangup event along the way somewhere. This allows us to
     * cleanup any old channels. We start by clearing the mark on all channels
     * and then generates a Asterisk status message for every active channel. At
     * the end of the process any channels which haven't been marked are then
     * discarded.
     */
    public void startSweep()
    {
        PeerMonitor.logger.debug("Starting channel mark and sweep"); //$NON-NLS-1$

        // Mark every channel as 'clearing'
        synchronized (PeerMonitor.class)
        {
            for (final Peer peer : this.peerList)
            {
                peer.startSweep();
            }
        }

        /**
         * Request Asterisk to send us a status update for every channel.
         */
        final StatusAction sa = new StatusAction();
        try
        {
            AsteriskPBX pbx = (AsteriskPBX) PBXFactory.getActivePBX();
            pbx.sendAction(sa, 5000);
        }
        catch (final Exception e)
        {
            PeerMonitor.logger.error(e, e);
        }

    }

    @Override
    public ListenerPriority getPriority()
    {
        return ListenerPriority.HIGH;
    }

}
