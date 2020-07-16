package org.asteriskjava;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class AsteriskVersionTest
{

    @Test
    public void testNullIfNoMatch()
    {
        assertNull(AsteriskVersion.getDetermineVersionFromString(""));
    }
    
    @Test
    public void test13()
    {
        assertTrue(AsteriskVersion.getDetermineVersionFromString("Asterisk 13.1.0~dfsg-1.1ubuntu4.1")
                .equals(AsteriskVersion.ASTERISK_13));
    }

    @Test
    public void test14()
    {
        assertTrue(AsteriskVersion.getDetermineVersionFromString("Asterisk 14.1.0").equals(AsteriskVersion.ASTERISK_14));
    }

    @Test
    public void test15()
    {
        assertTrue(AsteriskVersion.getDetermineVersionFromString("Asterisk 15.1.0").equals(AsteriskVersion.ASTERISK_15));
    }

}
