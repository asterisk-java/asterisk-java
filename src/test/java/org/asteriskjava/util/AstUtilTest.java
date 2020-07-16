package org.asteriskjava.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class AstUtilTest
{
    @Test
    public void testIsTrue()
    {
        assertTrue("on must be true", AstUtil.isTrue("on"));
        assertTrue("ON must be true", AstUtil.isTrue("ON"));
        assertTrue("Enabled must be true", AstUtil.isTrue("Enabled"));
        assertTrue("true must be true", AstUtil.isTrue("true"));
        assertFalse("false must be false", AstUtil.isTrue("false"));
        assertFalse("null must be false", AstUtil.isTrue(null));
    }

    @Test
    public void testParseCallerIdName()
    {
        assertEquals("Hans Wurst", AstUtil.parseCallerId("\"Hans Wurst\"<1234>")[0]);
        assertEquals("Hans Wurst", AstUtil.parseCallerId("\"Hans Wurst\" <1234>")[0]);
        assertEquals("Hans Wurst", AstUtil.parseCallerId("\" Hans Wurst \" <1234>")[0]);
        assertEquals("Hans Wurst", AstUtil.parseCallerId("  \"Hans Wurst  \"   <1234>  ")[0]);
        assertEquals("Hans Wurst", AstUtil.parseCallerId("  \"  Hans Wurst  \"   <1234>  ")[0]);
        assertEquals("Hans Wurst", AstUtil.parseCallerId("Hans Wurst <1234>")[0]);
        assertEquals("Hans Wurst", AstUtil.parseCallerId(" Hans Wurst  <1234>  ")[0]);
        assertEquals("Hans <Wurst>", AstUtil.parseCallerId("\"Hans <Wurst>\" <1234>")[0]);
        assertEquals("Hans Wurst", AstUtil.parseCallerId("Hans Wurst")[0]);
        assertEquals("Hans Wurst", AstUtil.parseCallerId(" Hans Wurst ")[0]);
        assertEquals(null, AstUtil.parseCallerId("<1234>")[0]);
        assertEquals(null, AstUtil.parseCallerId(" <1234> ")[0]);
        assertEquals("<1234", AstUtil.parseCallerId(" <1234 ")[0]);
        assertEquals("1234>", AstUtil.parseCallerId(" 1234> ")[0]);
        assertEquals(null, AstUtil.parseCallerId(null)[0]);
        assertEquals(null, AstUtil.parseCallerId("")[0]);
        assertEquals(null, AstUtil.parseCallerId(" ")[0]);
    }

    @Test
    public void testParseCallerIdNumber()
    {
        assertEquals("1234", AstUtil.parseCallerId("\"Hans Wurst\"<1234>")[1]);
        assertEquals("1234", AstUtil.parseCallerId("\"Hans Wurst\" <1234>")[1]);
        assertEquals("1234", AstUtil.parseCallerId("Hans Wurst <  1234 >   ")[1]);
        assertEquals(null, AstUtil.parseCallerId("\"Hans Wurst\"")[1]);
        assertEquals(null, AstUtil.parseCallerId("Hans Wurst")[1]);
        assertEquals(null, AstUtil.parseCallerId("Hans Wurst <>")[1]);
        assertEquals(null, AstUtil.parseCallerId("Hans Wurst <  > ")[1]);
        assertEquals(null, AstUtil.parseCallerId(null)[1]);
        assertEquals(null, AstUtil.parseCallerId("")[1]);
        assertEquals(null, AstUtil.parseCallerId(" ")[1]);
    }

    @Test
    public void testAJ120()
    {
        String s = "\"3496853210\" <3496853210>";
        assertEquals("3496853210", AstUtil.parseCallerId(s)[0]);
        assertEquals("3496853210", AstUtil.parseCallerId(s)[1]);
    }

    @Test
    public void testIsNull()
    {
        assertTrue("null must be null", AstUtil.isNull(null));
        assertTrue("unknown must be null", AstUtil.isNull("unknown"));
        assertTrue("<unknown> must be null", AstUtil.isNull("<unknown>"));
    }

    @Test
    public void testIsNullForIpAddressInPeerEntryEvent()
    {
        assertTrue("\"-none-\" must be considered null", AstUtil.isNull("-none-"));
    }

    @Test
    public void testIsEqual()
    {
        assertTrue(AstUtil.isEqual(null, null));
        assertTrue(AstUtil.isEqual("", ""));
        assertFalse(AstUtil.isEqual(null, ""));
        assertFalse(AstUtil.isEqual("", null));
        assertFalse(AstUtil.isEqual("", "1"));

    }
}
