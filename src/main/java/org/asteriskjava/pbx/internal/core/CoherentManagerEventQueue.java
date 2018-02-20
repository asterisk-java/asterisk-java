package org.asteriskjava.pbx.internal.core;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.asteriskjava.manager.ManagerConnection;
import org.asteriskjava.manager.ManagerEventListener;
import org.asteriskjava.pbx.asterisk.wrap.events.BridgeEvent;
import org.asteriskjava.pbx.asterisk.wrap.events.LinkEvent;
import org.asteriskjava.pbx.asterisk.wrap.events.ManagerEvent;
import org.asteriskjava.pbx.asterisk.wrap.events.UnlinkEvent;
import org.asteriskjava.pbx.internal.eventQueue.EventLifeMonitor;
import org.asteriskjava.pbx.util.LogTime;
import org.asteriskjava.util.Log;
import org.asteriskjava.util.LogFactory;

/**
 * This class provides a method of accepting, queueing and delivering manager
 * events. Asterisk is very sensitive to delays in receiving events. This class
 * ensures that events are received and queued rapidly. A separate thread
 * dequeues them and delivers them to the manager listener. Used this queue as
 * follows: manager.addEventListener(new
 * CoherentManagerEventQueue(originalListener)); This affectively daisy changes
 * the originalListener via our queue.
 * 
 * @author bsutton
 */
class CoherentManagerEventQueue implements ManagerEventListener, Runnable
{
    private static final Log logger = LogFactory.getLog(CoherentManagerEventQueue.class);

    private final ListenerManager listeners = new ListenerManager();

    private boolean _stop = false;
    private final Thread _th;
    private final BlockingQueue<EventLifeMonitor<org.asteriskjava.manager.event.ManagerEvent>> _eventQueue = new LinkedBlockingQueue<>();

    private int _queueMaxSize;

    private long _queueSum;

    private long _queueCount;

    private HashSet<Class< ? extends ManagerEvent>> globalEvents = new HashSet<>();

    public CoherentManagerEventQueue(String name, ManagerConnection connection)
    {

        connection.addEventListener(this);

        this._th = new Thread(this);
        this._th.setName("EventQueue: " + name);//$NON-NLS-1$
        this._th.setDaemon(true);
        this._th.start();
    }

    /**
     * handles manager events passed to us in our role as a listener. We queue
     * the event so that it can be read, by the run method of this class, and
     * subsequently passed on to the original listener.
     */
    @Override
    public void onManagerEvent(final org.asteriskjava.manager.event.ManagerEvent event)
    {

        // logger.error(event);
        boolean wanted = false;
        /**
         * Dump any events we arn't interested in ASAP to minimise the
         * processing overhead of these events.
         */
        // Only enqueue the events that are of interest to one of our listeners.
        synchronized (this.globalEvents)
        {
            Class< ? extends ManagerEvent> shadowEvent = CoherentEventFactory.getShadowEvent(event);
            if (this.globalEvents.contains(shadowEvent))
            {
                wanted = true;
            }
        }

        if (wanted)
        {

            // We don't support all events.
            this._eventQueue.add(new EventLifeMonitor<>(event));
            final int queueSize = this._eventQueue.size();
            if (this._queueMaxSize < queueSize)
            {
                this._queueMaxSize = queueSize;
            }
            this._queueSum += queueSize;
            this._queueCount++;

            if (CoherentManagerEventQueue.logger.isDebugEnabled())
            {
                if (this._eventQueue.size() > ((this._queueMaxSize + (this._queueSum / this._queueCount)) / 2))
                {
                    CoherentManagerEventQueue.logger.debug("queue gtr max avg: size=" + queueSize + " max:" //$NON-NLS-1$ //$NON-NLS-2$
                            + this._queueMaxSize + " avg:" + (this._queueSum / this._queueCount)); //$NON-NLS-1$
                }
            }
        }
    }

    @Override
    public void run()
    {

        while (this._stop == false)
        {
            try
            {
                final EventLifeMonitor<org.asteriskjava.manager.event.ManagerEvent> elm = this._eventQueue.poll(2,
                        TimeUnit.SECONDS);
                if (elm != null)
                {
                    // A poison queue event means its time to shutdown.
                    if (elm.getEvent().getClass() == PoisonQueueEvent.class)
                        break;

                    final ManagerEvent iEvent = CoherentEventFactory.build(elm.getEvent());
                    if (iEvent != null)
                    {
                        dispatchEvent(iEvent);
                        elm.assessAge();
                    }
                }
            }
            catch (final Exception e)
            {
                /**
                 * If an exception is thrown whilst we are shutting down then we
                 * don't care. If it is thrown when we aren't shutting down then
                 * we have a problem and we need to log it.
                 */
                if (this._stop == false)
                {
                    CoherentManagerEventQueue.logger.error(e, e);
                }
            }

        }

    }

    class PoisonQueueEvent extends org.asteriskjava.manager.event.ManagerEvent
    {
        private static final long serialVersionUID = 1L;

        public PoisonQueueEvent()
        {
            super("PoisonQueueEvent"); //$NON-NLS-1$
        }

    }

    public void stop()
    {
        this._stop = true;
        try
        {
            this._eventQueue.put(new EventLifeMonitor<org.asteriskjava.manager.event.ManagerEvent>(new PoisonQueueEvent()));
        }
        catch (InterruptedException e)
        {
            logger.error(e, e);

        }
    }

    // TODO: make this multi threaded
    private final ExecutorService executors = Executors.newFixedThreadPool(1);

    /**
     * Events are sent here from the CoherentManagerEventQueue after being
     * converted from a ManagerEvent to an ManagerEvent. This method is called
     * from a dedicated thread attached to the event queue which it uses for
     * dispatching events.
     */
    public void dispatchEvent(final ManagerEvent event)
    {
        if (logger.isDebugEnabled())
        {
            logger.debug("dispatch=" + event.toString()); //$NON-NLS-1$
        }

        // take a copy of the listeners so they can be modified whilst we
        // iterate over them
        // The iteration may call some long running processes.
        final List<FilteredManagerListenerWrapper> listenerCopy;
        synchronized (this.listeners)
        {
            listenerCopy = this.listeners.getCopyAsList();
        }

        try
        {
            final LogTime totalTime = new LogTime();

            CountDownLatch latch = new CountDownLatch(listenerCopy.size());

            for (final FilteredManagerListenerWrapper filter : listenerCopy)
            {
                if (filter.requiredEvents.contains(event.getClass()))
                {
                    dispatchEventOnThread(event, filter, latch);
                }
                else
                {
                    // this listener didn't want the event, so just decrease the
                    // countdown
                    latch.countDown();
                }
            }

            latch.await();

            if (totalTime.timeTaken() > 500)
            {
                logger.warn("Too long to process event " + event + " time taken: " + totalTime.timeTaken()); //$NON-NLS-1$ //$NON-NLS-2$
            }
        }
        catch (InterruptedException e)
        {
            Thread.interrupted();
        }
    }

    private void dispatchEventOnThread(final ManagerEvent event, final FilteredManagerListenerWrapper filter,
            final CountDownLatch latch)
    {
        Runnable runner = new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    final LogTime time = new LogTime();

                    filter._listener.onManagerEvent(event);
                    if (time.timeTaken() > 500)
                    {
                        logger.warn("ManagerListener :" + filter._listener.getName() //$NON-NLS-1$
                                + " is taken too long to process event " + event + " time taken: " + time.timeTaken()); //$NON-NLS-1$ //$NON-NLS-2$
                    }
                }
                catch (Exception e)
                {
                    logger.error(e, e);
                }
                finally
                {
                    latch.countDown();
                }
            }
        };

        executors.execute(runner);

    }

    /**
     * Adds a listener which will be sent all events that its filterEvent
     * handler will accept. All events are dispatch by way of a shared queue
     * which is read via a thread which is shared by all listeners. Whilst poor
     * performance of you listener can affect other listeners you can't affect
     * the read thread which takes events from asterisk and enqueues them.
     * 
     * @param listener
     */
    public void addListener(final FilteredManagerListener<ManagerEvent> listener)
    {
        synchronized (this.listeners)
        {
            this.listeners.addListener(listener);
            synchronized (this.globalEvents)
            {
                Collection<Class< ? extends ManagerEvent>> expandEvents = expandEvents(listener.requiredEvents());
                this.globalEvents.addAll(expandEvents);
            }
        }
        logger.debug("listener  added " + listener);
    }

    /**
     * in order to get Bridge Events, we must subscribe to Link and Unlink
     * events for asterisk 1.4, so we automatically add them if the Bridge Event
     * is required
     * 
     * @param events
     * @return
     */
    Collection<Class< ? extends ManagerEvent>> expandEvents(Collection<Class< ? extends ManagerEvent>> events)
    {
        Collection<Class< ? extends ManagerEvent>> requiredEvents = new HashSet<>();
        for (Class< ? extends ManagerEvent> event : events)
        {
            requiredEvents.add(event);
            if (event.equals(BridgeEvent.class))
            {
                requiredEvents.add(UnlinkEvent.class);
                requiredEvents.add(LinkEvent.class);
            }
        }

        return requiredEvents;
    }

    public void removeListener(final FilteredManagerListener<ManagerEvent> melf)
    {
        if (melf != null)
        {
            synchronized (this.listeners)
            {
                this.listeners.removeListener(melf);

                // When we remove a listener we must unfortunately
                // completely
                // recalculate the set of required events.
                synchronized (this.globalEvents)
                {
                    this.globalEvents.clear();
                    Iterator<FilteredManagerListenerWrapper> itr = this.listeners.iterator();
                    while (itr.hasNext())
                    {
                        FilteredManagerListenerWrapper readdContainer = itr.next();
                        this.globalEvents.addAll(expandEvents(readdContainer._listener.requiredEvents()));
                    }
                }

            }

        }

    }

    /**
     * transfers the listeners from one queue to another.
     * 
     * @param eventQueue
     */
    public void transferListeners(CoherentManagerEventQueue eventQueue)
    {
        synchronized (this.listeners)
        {
            synchronized (eventQueue.listeners)
            {
                Iterator<FilteredManagerListenerWrapper> itr = eventQueue.listeners.iterator();
                while (itr.hasNext())
                {
                    FilteredManagerListenerWrapper listener = itr.next();

                    this.addListener(listener._listener);
                }
                eventQueue.listeners.clear();
            }
        }
    }
}
