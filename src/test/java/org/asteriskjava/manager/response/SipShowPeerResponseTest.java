package org.asteriskjava.manager.response;

import junit.framework.TestCase;

public class SipShowPeerResponseTest extends TestCase
{
    private SipShowPeerResponse response;
    
    public void setUp()
    {
        response = new SipShowPeerResponse();
    }
    
    public void testSetQualifyFreq()
    {
        response.setQualifyFreq("6000 ms");
        assertEquals("Incorrect qualifyFreq", 6000, (int) response.getQualifyFreq());
    }

    public void testSetQualifyFreqWithWorkaround()
    {
        response.setQualifyFreq(": 6000 ms\n");
        assertEquals("Incorrect qualifyFreq", 6000, (int) response.getQualifyFreq());
    }
}
