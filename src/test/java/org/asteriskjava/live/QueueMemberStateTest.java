package org.asteriskjava.live;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class QueueMemberStateTest
{
    @Test
    public void testValueOf()
    {
        assertEquals(QueueMemberState.DEVICE_INUSE, QueueMemberState.valueOf("DEVICE_INUSE"));
        assertEquals(QueueMemberState.DEVICE_INUSE, QueueMemberState.valueOf(2));
    }

    @Test
    public void testToString()
    {
        assertEquals("DEVICE_INUSE", QueueMemberState.DEVICE_INUSE.toString());
    }
}
