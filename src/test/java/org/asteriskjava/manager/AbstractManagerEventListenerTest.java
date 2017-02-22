package org.asteriskjava.manager;

import static org.junit.Assert.assertTrue;

import org.asteriskjava.manager.event.JoinEvent;
import org.asteriskjava.manager.event.LeaveEvent;
import org.junit.Test;

public class AbstractManagerEventListenerTest {

    @Test
    public void shouldHandleJoinEvent() {
        //given
        EventListener listener = new EventListener();

        //when
        listener.onManagerEvent(new JoinEvent(this));

        //then
        assertTrue(listener.joinEventHandled);
    }

    @Test
    public void shouldHandleLeaveEvent() {
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
        protected void handleEvent(JoinEvent event) {
            this.joinEventHandled = true;
        }

        @Override
        protected void handleEvent(LeaveEvent event) {
            this.leaveEventHandled = true;
        }
    }
}