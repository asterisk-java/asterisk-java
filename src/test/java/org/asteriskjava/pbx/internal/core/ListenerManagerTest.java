package org.asteriskjava.pbx.internal.core;

import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.asteriskjava.pbx.ListenerPriority;
import org.asteriskjava.pbx.asterisk.wrap.events.ManagerEvent;
import org.asteriskjava.pbx.asterisk.wrap.events.VarSetEvent;
import org.junit.Test;

public class ListenerManagerTest
{

    volatile boolean managereventRecevied = false;

    @Test
    public void listenerTest() throws InterruptedException
    {
        List<FilteredManagerListener<ManagerEvent>> listeners = new LinkedList<>();

        ListenerManager queue = new ListenerManager();

        for (int i = 0; i < 1000; i++)
        {
            FilteredManagerListener<ManagerEvent> listener = getListener();
            listeners.add(listener);
            queue.addListener(listener);
        }
        Collections.shuffle(listeners);
        for (FilteredManagerListener<ManagerEvent> listener : listeners)
        {
            assertTrue(queue.removeListener(listener));
        }

        assertTrue(queue.size() == 0);

    }

    private FilteredManagerListener<ManagerEvent> getListener()
    {
        return new FilteredManagerListener<ManagerEvent>()
        {

            @Override
            public Set<Class< ? extends ManagerEvent>> requiredEvents()
            {
                Set<Class< ? extends ManagerEvent>> events = new HashSet<>();
                events.add(VarSetEvent.class);
                return events;
            }

            @Override
            public void onManagerEvent(ManagerEvent event)
            {
                managereventRecevied = true;
            }

            @Override
            public ListenerPriority getPriority()
            {
                return ListenerPriority.NORMAL;
            }

            @Override
            public String getName()
            {
                return "test";
            }
        };
    }

}
