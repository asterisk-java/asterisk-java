package org.asteriskjava.manager.event;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RtcpReceivedEventTest {
    private RtcpReceivedEvent rtcpReceivedEvent;

    @BeforeEach
    void setUp() {
        rtcpReceivedEvent = new RtcpReceivedEvent(this);
    }

    @Test
    void testFrom() {
        rtcpReceivedEvent.setFrom("192.168.0.1:1234");
        assertEquals(rtcpReceivedEvent.getFromAddress().getHostAddress(), "192.168.0.1");
        assertEquals(new Integer(1234), rtcpReceivedEvent.getFromPort());
    }

    @Test
    void testPt() {
        rtcpReceivedEvent.setPt("200(Sender Report)");
        assertEquals(new Long(200), rtcpReceivedEvent.getPt());
        assertEquals(new Long(RtcpReceivedEvent.PT_SENDER_REPORT), rtcpReceivedEvent.getPt());
    }

    @Test
    void testDlSr() {
        rtcpReceivedEvent.setDlSr("1.2345(sec)");
        assertEquals(1.2345, rtcpReceivedEvent.getDlSr(), 0.00001);
    }

    @Test
    void testDlSrWithSpace() {
        rtcpReceivedEvent.setDlSr("1.2345 (sec)"); // as used in RTCPSent
        assertEquals(1.2345, rtcpReceivedEvent.getDlSr(), 0.00001);
    }

    @Test
    void testRtt() {
        rtcpReceivedEvent.setRtt("12345(sec)");
        assertEquals(new Double(12345), rtcpReceivedEvent.getRtt());
    }
}
