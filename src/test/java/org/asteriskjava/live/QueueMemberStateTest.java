package org.asteriskjava.live;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class QueueMemberStateTest {
    @Test
    void testValueOf() {
        assertEquals(QueueMemberState.DEVICE_INUSE, QueueMemberState.valueOf("DEVICE_INUSE"));
        assertEquals(QueueMemberState.DEVICE_INUSE, QueueMemberState.valueOf(2));
    }

    @Test
    void testToString() {
        assertEquals("DEVICE_INUSE", QueueMemberState.DEVICE_INUSE.toString());
    }
}
