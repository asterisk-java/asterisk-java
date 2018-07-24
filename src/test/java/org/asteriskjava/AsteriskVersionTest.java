package org.asteriskjava;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class AsteriskVersionTest
{

    @Test
    public void test1_4()
    {
        // we just default to 1.4 - to further understand why, see
        // ManagerConnectionImpl.determineVersion
        assertTrue(AsteriskVersion.getDetermineVersionFromString("").equals(AsteriskVersion.ASTERISK_1_4));
    }

    @Test
    public void test1_6()
    {
        assertTrue(AsteriskVersion.getDetermineVersionFromString("Asterisk 1.6.0").equals(AsteriskVersion.ASTERISK_1_6));
    }

    @Test
    public void test1_8()
    {
        assertTrue(AsteriskVersion.getDetermineVersionFromString("Asterisk 1.8.0").equals(AsteriskVersion.ASTERISK_1_8));
    }

    @Test
    public void test10()
    {
        assertTrue(AsteriskVersion.getDetermineVersionFromString("Asterisk 10.1.0").equals(AsteriskVersion.ASTERISK_10));
    }

    @Test
    public void test11()
    {
        assertTrue(AsteriskVersion.getDetermineVersionFromString("Asterisk 11.1.0").equals(AsteriskVersion.ASTERISK_11));
    }

    @Test
    public void test12()
    {
        assertTrue(AsteriskVersion.getDetermineVersionFromString("Asterisk 12.1.0").equals(AsteriskVersion.ASTERISK_12));
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
