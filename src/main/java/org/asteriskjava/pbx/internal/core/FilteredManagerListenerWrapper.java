package org.asteriskjava.pbx.internal.core;

import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import org.asteriskjava.pbx.asterisk.wrap.events.BridgeEvent;
import org.asteriskjava.pbx.asterisk.wrap.events.LinkEvent;
import org.asteriskjava.pbx.asterisk.wrap.events.ManagerEvent;
import org.asteriskjava.pbx.asterisk.wrap.events.UnlinkEvent;

public class FilteredManagerListenerWrapper
{
    FilteredManagerListener<ManagerEvent> _listener;
    Set<Class< ? extends ManagerEvent>> requiredEvents;

    private final static AtomicInteger seed = new AtomicInteger();
    Integer equalityBuster = seed.incrementAndGet();

    public FilteredManagerListenerWrapper(FilteredManagerListener<ManagerEvent> listener)
    {
        this._listener = listener;
        this.requiredEvents = listener.requiredEvents();

        if (requiredEvents.contains(BridgeEvent.class))
        {
            // add LinkEvent and UnlinkEvent, as BridgeEvent only will
            // miss some events
            requiredEvents.add(LinkEvent.class);
            requiredEvents.add(UnlinkEvent.class);
        }

        for (Class< ? extends ManagerEvent> event : requiredEvents)
        {
            if (!CoherentEventFactory.mapEvents.values().contains(event)
                    && !CoherentEventFactory.mapResponses.values().contains(event))
            {
                throw new RuntimeException(
                        "The requested event type of " + event + "+isn't known by " + CoherentEventFactory.class);
            }
        }
    }

    @Override
    public String toString()
    {
        return this._listener.getName();
    }

}
