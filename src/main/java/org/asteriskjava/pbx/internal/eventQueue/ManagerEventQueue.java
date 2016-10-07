package org.asteriskjava.pbx.internal.eventQueue;

/**
 * This class provides a method of accepting, queueing and delivering manager
 * events.
 * 
 * Asterisk is very sensitive to delays in receiving events. This class ensures
 * that events are received and queued rapidly. A separate thread dequeues them
 * and delivers them to the manager listener.
 * 
 * Used this queue as follows:
 * 
 * manager.addEventListener(new ManagerEventQueue(originalListener));
 * 
 * This affectively daisy changes the originalListener via our queue.
 * 
 * @author bsutton
 * @deprecated use CoherentManagerEventQueue
 */
public class ManagerEventQueue // implements ManagerEventListener, Runnable
{
	// static Logger logger = Logger.getLogger(ManagerEventQueue.class);
	//
	// private final ManagerEventListener listener;
	// private boolean stop = false;
	// private final Thread th;
	// private final BlockingQueue<EventLifeMonitor<ManagerEvent>> eventQueue =
	// new LinkedBlockingQueue<>();
	//
	// private int queueMaxSize;
	//
	// private long queueSum;
	//
	// private long queueCount;
	//
	// public ManagerEventQueue(final ManagerEventListener listener)
	// {
	// this.listener = listener;
	// this.th = new Thread(this);
	//		this.th.setName("EventQueue: " + listener.getClass().getSimpleName());//$NON-NLS-1$
	// this.th.setDaemon(true);
	// this.th.start();
	// }
	//
	// /**
	// * handles manager events passed to us in our role as a listener.
	// *
	// * We queue the event so that it can be read, by the run method of this
	// * class, and subsequently passed on to the original listener.
	// */
	// @Override
	// public void onManagerEvent(final ManagerEvent event)
	// {
	// // if ((event instanceof DisconnectEvent) || (event instanceof
	// // ConnectEvent) || (event instanceof HangupEvent)
	// // || (event instanceof NewExtenEvent))
	// // {
	//
	// if (event instanceof NewExtenEvent)
	// return;
	//
	// this.eventQueue.add(new EventLifeMonitor<>(event));
	// final int queueSize = this.eventQueue.size();
	// if (this.queueMaxSize < queueSize)
	// {
	// this.queueMaxSize = queueSize;
	// }
	// this.queueSum += queueSize;
	// this.queueCount++;
	//
	// if (ManagerEventQueue.logger.isDebugEnabled())
	// {
	// if (this.eventQueue.size() > ((this.queueMaxSize + (this.queueSum /
	// this.queueCount)) / 2))
	// {
	//				ManagerEventQueue.logger.debug("queue size max avg"); //$NON-NLS-1$
	// ManagerEventQueue.logger
	//						.debug("    " + queueSize + " " + this.queueMaxSize + " " + (this.queueSum / this.queueCount)); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	// }
	//
	//			ManagerEventQueue.logger.debug("queue size " + this.eventQueue.size());//$NON-NLS-1$
	// }
	// // }
	//
	// }
	//
	// @Override
	// public void run()
	// {
	//
	// while (this.stop == false)
	// {
	// try
	// {
	// final EventLifeMonitor<ManagerEvent> elm = this.eventQueue.poll(2,
	// TimeUnit.SECONDS);
	// if (elm != null)
	// {
	// // Re-add the following line if you want to log every event.
	// // logger.error(elm.getEvent());
	// this.listener.onManagerEvent(elm.getEvent());
	// elm.assessAge();
	// }
	// }
	// catch (final Exception e)
	// {
	// /**
	// * If an exception is thrown whilst we are shutting down then we
	// * don't care. If it is thrown when we aren't shutting down then
	// * we have a problem and we need to log it.
	// */
	// if (this.stop == false)
	// {
	// ManagerEventQueue.logger.error(e, e);
	// }
	// }
	//
	// }
	//
	// }
	//
	// public void stop()
	// {
	// this.stop = true;
	// synchronized (this.eventQueue)
	// {
	// this.eventQueue.notify();
	// }
	// }

}
