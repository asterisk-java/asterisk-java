package org.asteriskjava.manager;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.asteriskjava.manager.event.ManagerEvent;

/**
 * Proxies a ManagerEventListener and dispatches events asynchronously
 * by using a single threaded executor. 
 * 
 * @author srt
 * @since 0.3
 * @version $Id$
 */
public class ManagerEventListenerProxy implements ManagerEventListener
{
    private final ManagerEventListener target;
    private final ExecutorService executor;

    /**
     * Creates a new ManagerEventListenerProxy that notifies the given target
     * asynchronously when new events are received.
     * 
     * @param target the target listener to invoke.
     */
    public ManagerEventListenerProxy(ManagerEventListener target)
    {
        this.target = target;
        this.executor = Executors.newSingleThreadExecutor();
    }

    public void onManagerEvent(final ManagerEvent event)
    {
        executor.execute(new Runnable()
        {
            public void run()
            {
                target.onManagerEvent(event);
            }
        });
    }
}
