package org.asteriskjava.pbx.internal.eventQueue;

import org.asteriskjava.pbx.util.LogTime;
import org.asteriskjava.util.Log;
import org.asteriskjava.util.LogFactory;

public class EventLifeMonitor<T>
{
    private static final Log logger = LogFactory.getLog(EventLifeMonitor.class);

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
