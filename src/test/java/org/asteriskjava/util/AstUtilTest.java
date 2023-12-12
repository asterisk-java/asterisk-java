package org.asteriskjava.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AstUtilTest {
    @Test
    void testParseCallerIdName() {
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
    void testParseCallerIdNumber() {
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
    void testAJ120() {
        String s = "\"3496853210\" <3496853210>";
        assertEquals("3496853210", AstUtil.parseCallerId(s)[0]);
        assertEquals("3496853210", AstUtil.parseCallerId(s)[1]);
    }

    @Test
    void testIsNull() {
        assertTrue(AstUtil.isNull(null), "null must be null");
        assertTrue(AstUtil.isNull("unknown"), "unknown must be null");
        assertTrue(AstUtil.isNull("<unknown>"), "<unknown> must be null");
    }

    @Test
    void testIsNullForIpAddressInPeerEntryEvent() {
        assertTrue(AstUtil.isNull("-none-"), "\"-none-\" must be considered null");
    }

    @Test
    void testIsEqual() {
        assertTrue(AstUtil.isEqual(null, null));
        assertTrue(AstUtil.isEqual("", ""));
        assertFalse(AstUtil.isEqual(null, ""));
        assertFalse(AstUtil.isEqual("", null));
        assertFalse(AstUtil.isEqual("", "1"));

    }
}
