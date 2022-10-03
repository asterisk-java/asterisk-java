package org.asteriskjava.manager.event;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class ChannelReloadEventTest {
    private ChannelReloadEvent event;

    @BeforeEach
    void setUp() {
        this.event = new ChannelReloadEvent(this);
    }

    @Test
    void testNullReloadReason() {
        event.setReloadReason(null);
        assertNull(event.getReloadReasonCode());
        assertNull(event.getReloadReasonDescription());
    }

    @Test
    void testGetReloadReasonCode() {
        event.setReloadReason("CLIRELOAD (Channel module reload by CLI command)");
        assertEquals("CLIRELOAD", event.getReloadReasonCode());
        assertEquals(ChannelReloadEvent.REASON_CLI_RELOAD, event.getReloadReasonCode());
    }

    @Test
    void testGetReloadReasonDescription() {
        event.setReloadReason("CLIRELOAD (Channel module reload by CLI command)");
        assertEquals("Channel module reload by CLI command", event.getReloadReasonDescription());
    }
}
