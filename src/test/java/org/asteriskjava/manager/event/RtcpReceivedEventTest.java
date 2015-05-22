package org.asteriskjava.manager.event;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class RtcpReceivedEventTest
{
    private RtcpReceivedEvent rtcpReceivedEvent;

    @Before
    public void setUp()
    {
        rtcpReceivedEvent = new RtcpReceivedEvent(this);
    }

    @Test
    public void testFrom()
    {
        rtcpReceivedEvent.setFrom("192.168.0.1:1234");
        assertEquals("192.168.0.1", rtcpReceivedEvent.getFromAddress().getHostAddress());
        assertEquals(new Integer(1234), rtcpReceivedEvent.getFromPort());
    }

    @Test
    public void testPt()
    {
        rtcpReceivedEvent.setPt("200(Sender Report)");
        assertEquals(new Long(200), rtcpReceivedEvent.getPt());
        assertEquals(new Long(RtcpReceivedEvent.PT_SENDER_REPORT), rtcpReceivedEvent.getPt());
    }

    @Test
    public void testDlSr()
    {
        rtcpReceivedEvent.setDlSr("1.2345(sec)");
        assertEquals(1.2345, rtcpReceivedEvent.getDlSr(), 0.00001);
    }

    @Test
    public void testDlSrWithSpace()
    {
        rtcpReceivedEvent.setDlSr("1.2345 (sec)"); // as used in RTCPSent
        assertEquals(1.2345, rtcpReceivedEvent.getDlSr(), 0.00001);
    }

    @Test
    public void testRtt()
    {
        rtcpReceivedEvent.setRtt("12345(sec)");
        assertEquals(new Double(12345), rtcpReceivedEvent.getRtt());
    }
}
