package org.asteriskjava.manager;

import org.asteriskjava.manager.event.JoinEvent;
import org.asteriskjava.manager.event.LeaveEvent;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class AbstractManagerEventListenerTest {

    @Test
    void shouldHandleJoinEvent() {
        //given
        EventListener listener = new EventListener();

        //when
        listener.onManagerEvent(new JoinEvent(this));

        //then
        assertTrue(listener.joinEventHandled);
    }

    @Test
    void shouldHandleLeaveEvent() {
        //given
        EventListener listener = new EventListener();

        //when
        listener.onManagerEvent(new LeaveEvent(this));

        //then
        assertTrue(listener.leaveEventHandled);
    }

    private static class EventListener extends AbstractManagerEventListener {
        public boolean joinEventHandled;
        public boolean leaveEventHandled;

        @Override
        public void handleEvent(JoinEvent event) {
            this.joinEventHandled = true;
        }

        @Override
        public void handleEvent(LeaveEvent event) {
            this.leaveEventHandled = true;
        }
    }
}
