package org.asteriskjava.manager.internal;

import java.lang.ref.WeakReference;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.asteriskjava.manager.event.ManagerEvent;
import org.asteriskjava.manager.response.ManagerResponse;
import org.asteriskjava.pbx.agi.RateLimiter;
import org.asteriskjava.pbx.util.LogTime;
import org.asteriskjava.util.Log;
import org.asteriskjava.util.LogFactory;

public class AsyncEventPump implements Dispatcher, Runnable
{
    private final Log logger = LogFactory.getLog(AsyncEventPump.class);

    private static final long MAX_SAFE_EVENT_AGE = 250;

    private final LinkedBlockingQueue<EventWrapper> queue = new LinkedBlockingQueue<>(20000);
    private final Dispatcher dispatcher;
    private volatile boolean stop = false;
    private final WeakReference<Object> owner;

    private final Thread thread;

    AsyncEventPump(Object owner, Dispatcher dispatcher, String string)
    {
        this.dispatcher = dispatcher;
        this.owner = new WeakReference<>(owner);
        thread = new Thread(this, string + ":AsyncEventPump");
        thread.start();
    }

    @Override
    public void run()
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
                        logger.warn("The following message will only appear once per second!");
                        logger.warn("Event being dispatched " + wrapper.timer.timeTaken()
                                + " MS after arriving, your ManagerEvent handlers are too slow!\n"
                                + "You should also check for Garbage Collection issues.");
                        logger.warn("There are " + queue.size() + " events waiting to be processed in the queue.");

                    }
                    if (wrapper.response != null)
                    {
                        dispatcher.dispatchResponse(wrapper.response);
                    }
                    else if (wrapper.event != null)
                    {
                        dispatcher.dispatchEvent(wrapper.event);
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
        logger.warn("AsyncEventPump is shutting down");

    }

    public void stop()
    {
        stop = true;
        // poison
        queue.add(new EventWrapper());
        long delay = 1;
        while (!queue.isEmpty())
        {
            try
            {
                TimeUnit.MILLISECONDS.sleep(delay);
                logger.info("Stoping " + delay);
                delay = Math.min(10, delay + 1);
            }
            catch (InterruptedException e)
            {
                logger.error(e);
            }
        }
        logger.info("Stopped");
    }

    @Override
    public void dispatchResponse(ManagerResponse response)
    {
        if (!queue.offer(new EventWrapper(response)))
        {
            logger.error("Event queue is full, not processing ManagerResponse " + response);
        }
    }

    @Override
    public void dispatchEvent(ManagerEvent event)
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
    }

}
