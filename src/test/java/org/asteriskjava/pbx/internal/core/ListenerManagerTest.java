package org.asteriskjava.pbx.internal.core;

import org.asteriskjava.pbx.ListenerPriority;
import org.asteriskjava.pbx.asterisk.wrap.events.ManagerEvent;
import org.asteriskjava.pbx.asterisk.wrap.events.VarSetEvent;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ListenerManagerTest {
    volatile boolean managereventRecevied = false;

    @Test
    void listenerTest() throws InterruptedException {
        List<FilteredManagerListener<ManagerEvent>> listeners = new LinkedList<>();

        ListenerManager queue = new ListenerManager();

        for (int i = 0; i < 1000; i++) {
            FilteredManagerListener<ManagerEvent> listener = getListener();
            listeners.add(listener);
            queue.addListener(listener);
        }
        Collections.shuffle(listeners);
        for (FilteredManagerListener<ManagerEvent> listener : listeners) {
            assertTrue(queue.removeListener(listener));
        }

        assertTrue(queue.size() == 0);

    }

    private FilteredManagerListener<ManagerEvent> getListener() {
        return new FilteredManagerListener<ManagerEvent>() {

            @Override
            public Set<Class<? extends ManagerEvent>> requiredEvents() {
                Set<Class<? extends ManagerEvent>> events = new HashSet<>();
                events.add(VarSetEvent.class);
                return events;
            }

            @Override
            public void onManagerEvent(ManagerEvent event) {
                managereventRecevied = true;
            }

            @Override
            public ListenerPriority getPriority() {
                return ListenerPriority.NORMAL;
            }

            @Override
            public String getName() {
                return "test";
            }
        };
    }

}
