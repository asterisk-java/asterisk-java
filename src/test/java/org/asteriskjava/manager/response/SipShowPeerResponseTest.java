package org.asteriskjava.manager.response;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class SipShowPeerResponseTest
{
    private SipShowPeerResponse response;

    @Before
    public void setUp()
    {
        response = new SipShowPeerResponse();
    }

    @Test
    public void testSetQualifyFreq()
    {
        response.setQualifyFreq("6000 ms");
        assertEquals("Incorrect qualifyFreq", 6000, (int) response.getQualifyFreq());
    }

    @Test
    public void testSetQualifyFreqWithWorkaround()
    {
        response.setQualifyFreq(": 6000 ms\n");
        assertEquals("Incorrect qualifyFreq", 6000, (int) response.getQualifyFreq());
    }

    @Test
    public void testSetQualifyFreqWithWorkaroundAndChanVariable()
    {
        response.setQualifyFreq(": 60000 ms\nChanVariable:\n PHBX_ID,191");
        assertEquals("Incorrect qualifyFreq", 60000, (int) response.getQualifyFreq());
    }
    
    @Test
    public void testSetMohsuggest()
    {
        response.setMohsuggest("default");
        assertEquals("Incorrect mohsuggest", "default",response.getMohsuggest());
    }
    
    
}
