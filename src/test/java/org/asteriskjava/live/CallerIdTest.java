package org.asteriskjava.live;

import junit.framework.TestCase;

public class CallerIdTest extends TestCase
{
    public void testEquals()
    {
        CallerId callerId1;
        CallerId callerId2;
        
        callerId1 = new CallerId("Hans Wurst", "1234");
        callerId2 = new CallerId("Hans Wurst", "1234");
        assertEquals(callerId1, callerId2);

        callerId1 = new CallerId("Hans Wurst", null);
        callerId2 = new CallerId("Hans Wurst", null);
        assertEquals(callerId1, callerId2);

        callerId1 = new CallerId(null, "1234");
        callerId2 = new CallerId(null, "1234");
        assertEquals(callerId1, callerId2);
        
        callerId1 = new CallerId(null, null);
        callerId2 = new CallerId(null, null);
        assertEquals(callerId1, callerId2);
    }

    public void testValueOf()
    {
        CallerId callerId = new CallerId("Hans Wurst", "1234");
        assertEquals(callerId, CallerId.valueOf("\"Hans Wurst\" <1234>"));
        assertEquals(callerId, CallerId.valueOf("Hans Wurst <1234>"));
        assertEquals(callerId, CallerId.valueOf(callerId.toString()));
    }

    public void testToString()
    {
        assertEquals("\"Hans Wurst\" <1234>", new CallerId("Hans Wurst", "1234").toString());
        assertEquals("<1234>", new CallerId(null, "1234").toString());
        assertEquals("\"Hans Wurst\"", new CallerId("Hans Wurst", null).toString());
    }
}
