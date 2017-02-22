package org.asteriskjava.manager;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.asteriskjava.manager.event.ManagerEvent;
import org.asteriskjava.util.DaemonThreadFactory;

/**
 * Proxies a ManagerEventListener and dispatches events asynchronously by using
 * a single threaded executor.<p>
 * Use this proxy to prevent the reader thread from being blocked while your
 * application processes {@link org.asteriskjava.manager.event.ManagerEvent}s.
 * If you want to use the {@link org.asteriskjava.manager.ManagerConnection} for
 * sending actions in your {@link org.asteriskjava.manager.ManagerEventListener}
 * using a proxy like this one is mandatory; otherwise you will always run into
 * a timeout because the reader thread that is supposed to read the response to
 * your action is still blocked processing the event.<p>
 * If in doubt use the proxy as it won't hurt.<p>
 * Example:
 * <pre>
 * ManagerConnection connection;
 * ManagerEventListener myListener;
 * ...
 * connection.addEventListener(new ManagerEventListenerProxy(myListener));
 * </pre>
 * 
 * @author srt
 * @author fink
 * @since 0.3
 */
public class ManagerEventListenerProxy implements ManagerEventListener {
		private final ThreadPoolExecutor executor;
    private final ManagerEventListener target;


    /**
     * Creates a new ManagerEventListenerProxy that notifies the given target
     * asynchronously when new events are received.
     * 
     * @param target the target listener to invoke.
     * @see Executors#newSingleThreadExecutor(ThreadFactory)
     */
    public ManagerEventListenerProxy(ManagerEventListener target) {
	    executor = new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(), new DaemonThreadFactory());
	    this.target = target;
	    if (target == null) {
		    throw new NullPointerException("ManagerEventListener target is null!");
	    }
    }//new


    @Override public void onManagerEvent(final ManagerEvent event) {
			executor.execute(new Runnable() {
					@Override public void run() {
							target.onManagerEvent(event);
					}
			});
    }//onManagerEvent


    public void shutdown() {
        executor.shutdown();
    }

		public static class Access {
		    private Access() {
		        
		    }
		    
			public static int getThreadQueueSize (ManagerEventListenerProxy proxy) {
				return proxy.executor.getQueue().size();
			}
		}//Access
}
