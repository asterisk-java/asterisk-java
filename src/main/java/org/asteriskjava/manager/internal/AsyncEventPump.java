package org.asteriskjava.manager.internal;

import java.lang.ref.WeakReference;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.asteriskjava.lock.Locker;
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
    private volatile boolean terminated = false;

    private final String name;

    /**
     * @param owner: A weak reference to the owner is created, should it be
     *            garbage collected then AsyncEventPump will shutdown.
     * @param dispatcher: The dispatcher that AsyncEventPump should deliver
     *            events to.
     * @param threadName: The AsyncEventPump's thread will be named with a
     *            variant of threadName
     */
    AsyncEventPump(Object owner, Dispatcher dispatcher, String threadName)
    {
        this.dispatcher = dispatcher;
        this.owner = new WeakReference<>(owner);
        name = threadName + ":AsyncEventPump";
        thread = new Thread(this, name);
        thread.start();
    }

    @Override
    public void run()
    {
        try
        {
            logger.info("starting");
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
            terminated = true;
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
        logger.info(name + " Requesting AsyncEventPump to stop");
        if (terminated)
        {
            logger.warn(name + " AsyncEventPump is already stopped");
            if (!queue.isEmpty())
            {
                logger.error(name + " There are unprocessed events in the queue");
            }

            return;
        }
        EventWrapper poisonWrapper = new EventWrapper();
        queue.add(poisonWrapper);
        LogTime timer = new LogTime();
        try
        {
            int queueSize = queue.size();
            while (!poisonWrapper.poison.await(5, TimeUnit.SECONDS))
            {
                // still waiting for the poison to be consumed.
                if (queueSize == queue.size())
                {
                    Locker.dumpThread(thread, name + " AsyncEventPump thread is blocked here...");
                    throw new RuntimeException(name + " Failed to shutdown AsyncEventPump cleanly!");

                }
                queueSize = queue.size();
                logger.info(name + " Waiting for AsyncEventPump to Stop... ");

                if (timer.timeTaken() > 60_000)
                {
                    throw new RuntimeException(name + " Failed to shutdown AsyncEventPump cleanly!");
                }
            }
        }
        catch (InterruptedException e1)
        {
            logger.error(name + e1.getMessage());
        }
    }

    /**
     * add a ManagerResponse to the queue, only if the queue is not full
     */
    @Override
    public void dispatchResponse(ManagerResponse response, Integer requiredHandlingTime)
    {
        if (!queue.offer(new EventWrapper(response)))
        {
            logger.error(name + " Event queue is full, not processing ManagerResponse " + response);
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
            logger.error(name + " Event queue is full, not processing ManagerEvent " + event);
        }
    }

    private static class EventWrapper
    {
        LogTime timer = new LogTime();
        ManagerResponse response;
        ManagerEvent event;
        CountDownLatch poison;

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

    }

}
