package org.asteriskjava.manager.internal;

import java.lang.ref.WeakReference;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.asteriskjava.manager.event.ManagerEvent;
import org.asteriskjava.manager.response.ManagerResponse;
import org.asteriskjava.pbx.agi.RateLimiter;
import org.asteriskjava.pbx.util.LogTime;
import org.asteriskjava.util.Log;
import org.asteriskjava.util.LogFactory;

/**
 * AsyncEventPump delivers events and responses to a Dispatcher without blocking
 * the thread which is producing the events and responses. AsyncEventPump also
 * adds logging around timely handling of events
 * 
 * @author rsutton
 */
public class AsyncEventPump implements Dispatcher, Runnable
{
    private final Log logger = LogFactory.getLog(AsyncEventPump.class);

    private static final long MAX_SAFE_EVENT_AGE = 500;

    private final LinkedBlockingQueue<EventWrapper> queue = new LinkedBlockingQueue<>(20000);
    private final Dispatcher dispatcher;
    private volatile boolean stop = false;
    private final WeakReference<Object> owner;

    private final Thread thread;

    /**
     * @param owner a weak reference to the owner is created, should it be
     *            garbage collected then AsyncEventPump will shutdown.
     * @param dispatcher the dispatcher that AsyncEventPump should deliver
     *            events to.
     * @param threadName the AsyncEventPump's thread will be named with a
     *            variant of threadName
     */
    AsyncEventPump(Object owner, Dispatcher dispatcher, String threadName)
    {
        this.dispatcher = dispatcher;
        this.owner = new WeakReference<>(owner);
        thread = new Thread(this, threadName + ":AsyncEventPump");
        thread.start();
    }

    @Override
    public void run()
    {
        try
        {
            RateLimiter rateLimiter = new RateLimiter(2);
            while (!stop || !queue.isEmpty())
            {
                try
                {
                    EventWrapper wrapper = queue.poll(1, TimeUnit.MINUTES);
                    if (wrapper != null)
                    {
                        if (wrapper.timer.timeTaken() > MAX_SAFE_EVENT_AGE && rateLimiter.tryAcquire())
                        {
                            logger.warn("The following message will only appear once per second!\n"
                                    + "Event being dispatched " + wrapper.timer.timeTaken()
                                    + " MS after arriving, your ManagerEvent handlers are too slow!\n"
                                    + "You should also check for Garbage Collection issues.\n" + "There are " + queue.size()
                                    + " events waiting to be processed in the queue.\n");

                        }
                        // assume we need to process all queued events in
                        // MAX_SAFE_EVENT_AGE
                        int requiredHandlingTime = (int) (MAX_SAFE_EVENT_AGE / Math.max(1, queue.size()));

                        if (wrapper.response != null)
                        {
                            dispatcher.dispatchResponse(wrapper.response, requiredHandlingTime);
                        }
                        else if (wrapper.event != null)
                        {
                            dispatcher.dispatchEvent(wrapper.event, requiredHandlingTime);
                        }
                        else if (wrapper.poison != null)
                        {
                            wrapper.poison.countDown();
                        }
                    }
                    else if (owner.get() == null)
                    {
                        stop = true;
                        logger.error("The owner has been garbage collected!");
                    }

                }
                catch (InterruptedException e)
                {
                    logger.error(e);
                }
                catch (Exception e)
                {
                    logger.error(e, e);
                }
            }
        }
        finally
        {
            logger.warn("AsyncEventPump has exited");
        }

    }

    /**
     * call stop() to cause the AsyncEventPump to stop, it will first empty the
     * queue.
     */
    public void stop()
    {
        stop = true;
        EventWrapper poisonWrapper = new EventWrapper();
        queue.add(poisonWrapper);
        logger.warn("Requesting AsyncEventPump to stop");
        int ctr = 0;
        try
        {
            while (!poisonWrapper.poison.await(1, TimeUnit.SECONDS))
            {
                logger.info("Waiting for AsyncEventPump to Stop... ");
                ctr++;
                if (ctr > 60)
                {
                    throw new RuntimeException("Failed to shutdown AsyncEventPump cleanly!");
                }
            }
        }
        catch (InterruptedException e1)
        {
            logger.error(e1);
        }
        logger.info("AsyncEventPump consumed the poision, and may have exited before this message was logged.");
    }

    /**
     * add a ManagerResponse to the queue, only if the queue is not full
     */
    @Override
    public void dispatchResponse(ManagerResponse response, Integer requiredHandlingTime)
    {
        if (!queue.offer(new EventWrapper(response)))
        {
            logger.error("Event queue is full, not processing ManagerResponse " + response);
        }
    }

    /**
     * add a ManagerEvent to the queue, only if the queue is not full
     */
    @Override
    public void dispatchEvent(ManagerEvent event, Integer requiredHandlingTime)
    {
        if (!queue.offer(new EventWrapper(event)))
        {
            logger.error("Event queue is full, not processing ManagerEvent " + event);
        }
    }

    class EventWrapper
    {
        EventWrapper()
        {
            // poison
            poison = new CountDownLatch(1);
        }

        EventWrapper(ManagerResponse response)
        {
            this.response = response;
        }

        EventWrapper(ManagerEvent event)
        {
            this.event = event;
        }

        LogTime timer = new LogTime();
        ManagerResponse response;
        ManagerEvent event;
        CountDownLatch poison;
    }

}
