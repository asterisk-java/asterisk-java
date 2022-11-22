package org.asteriskjava.manager.event;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class NewStateEventTest {
    private NewStateEvent newStateEvent;

    @BeforeEach
    void setUp() {
        newStateEvent = new NewStateEvent(this);
    }

    @Test
    void testWithState() {
        newStateEvent.setState("Ring");
        assertEquals(4, newStateEvent.getChannelState());
        assertEquals("Ring", newStateEvent.getChannelStateDesc());
    }

    @Test
    void testWithUnknownState() {
        newStateEvent.setState("Unknown (4)");
        assertEquals(4, newStateEvent.getChannelState());
    }
}
