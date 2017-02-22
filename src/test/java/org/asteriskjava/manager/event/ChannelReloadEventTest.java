package org.asteriskjava.manager.event;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class ChannelReloadEventTest
{
    private ChannelReloadEvent event;

    @Before
    public void setUp()
    {
        this.event = new ChannelReloadEvent(this);
    }

    @Test
    public void testNullReloadReason()
    {
        event.setReloadReason(null);
        assertNull(event.getReloadReasonCode());
        assertNull(event.getReloadReasonDescription());
    }

    @Test
    public void testGetReloadReasonCode()
    {
        event.setReloadReason("CLIRELOAD (Channel module reload by CLI command)");
        assertEquals("CLIRELOAD", event.getReloadReasonCode());
        assertEquals(ChannelReloadEvent.REASON_CLI_RELOAD, event.getReloadReasonCode());
    }

    @Test
    public void testGetReloadReasonDescription()
    {
        event.setReloadReason("CLIRELOAD (Channel module reload by CLI command)");
        assertEquals("Channel module reload by CLI command", event.getReloadReasonDescription());
    }
}
