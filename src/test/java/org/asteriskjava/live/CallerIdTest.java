package org.asteriskjava.live;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class CallerIdTest
{
    @Test
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

    @Test
    public void testValueOf()
    {
        CallerId callerId = new CallerId("Hans Wurst", "1234");
        assertEquals(callerId, CallerId.valueOf("\"Hans Wurst\" <1234>"));
        assertEquals(callerId, CallerId.valueOf("Hans Wurst <1234>"));
        assertEquals(callerId, CallerId.valueOf(callerId.toString()));
    }

    @Test
    public void testValueOfWithNullLiteralInName()
    {
        CallerId callerId = new CallerId(null, "1234");
        assertEquals(callerId, CallerId.valueOf("\"\" <1234>"));
        assertEquals(callerId, CallerId.valueOf("\"<Unknown>\" <1234>"));
        assertEquals(callerId, CallerId.valueOf("<1234>"));
        assertEquals(callerId, CallerId.valueOf(callerId.toString()));
    }

    @Test
    public void testValueOfWithNullLiteralInNumber()
    {
        CallerId callerId = new CallerId("Hans Wurst", null);
        assertEquals(callerId, CallerId.valueOf("\"Hans Wurst\" <>"));
        // assertEquals(callerId,
        // CallerId.valueOf("\"Hans Wurst\" <<Unknown>>"));
        // assertEquals(callerId, CallerId.valueOf("Hans Wurst <<Unknown>>"));
    }

    @Test
    public void testValueOfWithNullLiteralInNameAndNumber()
    {
        CallerId callerId = new CallerId(null, null);
        assertEquals(callerId, CallerId.valueOf("\"\" <>"));
        // assertEquals(callerId, CallerId.valueOf("<<Unknown>>"));
        // assertEquals(callerId,
        // CallerId.valueOf("\"<Unknown>\" <<Unknown>>"));
        // assertEquals(callerId, CallerId.valueOf("<Unknown> <<Unknown>>"));
        assertEquals(callerId, CallerId.valueOf("<Unknown>"));
        // assertEquals(callerId, CallerId.valueOf("\"<Unknown>\""));
    }

    @Test
    public void testConstructorWithNullLiteral()
    {
        assertEquals(new CallerId(null, "1234"), new CallerId("<unknown>", "1234"));
    }

    @Test
    public void testToString()
    {
        assertEquals("\"Hans Wurst\" <1234>", new CallerId("Hans Wurst", "1234").toString());
        assertEquals("<1234>", new CallerId(null, "1234").toString());
        assertEquals("\"Hans Wurst\"", new CallerId("Hans Wurst", null).toString());
    }
}
