package org.asteriskjava.manager.event;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class NewStateEventTest
{
    private NewStateEvent newStateEvent;

    @Before
    public void setUp()
    {
        newStateEvent = new NewStateEvent(this);
    }

    @Test
    public void testWithState()
    {
        newStateEvent.setState("Ring");
        assertEquals(new Integer(4), newStateEvent.getChannelState());
        assertEquals("Ring", newStateEvent.getChannelStateDesc());
    }

    @Test
    public void testWithUnknownState()
    {
        newStateEvent.setState("Unknown (4)");
        assertEquals(new Integer(4), newStateEvent.getChannelState());
    }
}
