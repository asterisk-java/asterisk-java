package org.asteriskjava.manager.response;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SipShowPeerResponseTest {
    private SipShowPeerResponse response;

    @BeforeEach
    void setUp() {
        response = new SipShowPeerResponse();
    }

    @Test
    void testSetQualifyFreq() {
        response.setQualifyFreq("6000 ms");
        assertEquals(6000, (int) response.getQualifyFreq(), "Incorrect qualifyFreq");
    }

    @Test
    void testSetQualifyFreqWithWorkaround() {
        response.setQualifyFreq(": 6000 ms\n");
        assertEquals(6000, (int) response.getQualifyFreq(), "Incorrect qualifyFreq");
    }

    @Test
    void testSetQualifyFreqWithWorkaroundAndChanVariable() {
        response.setQualifyFreq(": 60000 ms\nChanVariable:\n PHBX_ID,191");
        assertEquals(60000, (int) response.getQualifyFreq(), "Incorrect qualifyFreq");
    }

    @Test
    void testSetMohsuggest() {
        response.setMohsuggest("default");
        assertEquals("default", response.getMohsuggest(), "Incorrect mohsuggest");
    }


}
