package org.asteriskjava.pbx.internal.eventQueue;

import org.apache.log4j.Logger;
import org.asteriskjava.pbx.util.LogTime;

public class EventLifeMonitor<T>
{
	static Logger logger = Logger.getLogger(EventLifeMonitor.class);

	/*
	 * this class is used to hold an event while its in the queue and calculate
	 * how long it spent in the que before being processed.
	 */

	private final T theEvent;

	private final LogTime age;

	public EventLifeMonitor(final T event)
	{
		this.theEvent = event;
		this.age = new LogTime();
	}

	public void assessAge()
	{
		if (this.age.timeTaken() > 2000)
		{
			EventLifeMonitor.logger.warn("event age : " + this.age.timeTaken()); //$NON-NLS-1$
		}

	}

	public T getEvent()
	{
		return this.theEvent;
	}
}
