package org.asteriskjava.manager.event;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 *
 */
public class T38FaxStatusEventTest
{
    @Test
    public void testStripUnit()
    {
        T38FaxStatusEvent event = new T38FaxStatusEvent(this);
        assertEquals("0.022", event.stripUnit("0.022 sec."));
    }

    @Test
    public void testParseProperties()
    {
        T38FaxStatusEvent event = new T38FaxStatusEvent(this);
        event.setTotalLag("-9 ms");
        event.setMaxLag("4 ms");
        event.setT38SessionDuration("0.022 sec.");
        event.setAverageLag("-1.80 ms");
        event.setAverageTxDataRate("363 bps");
        event.setAverageRxDataRate("0 bps");

        assertEquals(-9, event.getTotalLagInMilliSeconds().intValue());
        assertEquals(4, event.getMaxLagInMilliSeconds().intValue());
        assertEquals(0.022, event.getT38SessionDurationInSeconds(), 0.00001);
        assertEquals(-1.8, event.getAverageLagInMilliSeconds(), 0.00001);
        assertEquals(363, event.getAverageTxDataRateInBps().intValue());
        assertEquals(0, event.getAverageRxDataRateInBps().intValue());
    }
}
