package org.asteriskjava;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AsteriskVersionTest {
    @Test
    void testNullIfNoMatch() {
        assertNull(AsteriskVersion.getDetermineVersionFromString(""));
    }

    @Test
    void test13() {
        assertTrue(AsteriskVersion.getDetermineVersionFromString("Asterisk 13.1.0~dfsg-1.1ubuntu4.1")
                .equals(AsteriskVersion.ASTERISK_13));
    }

    @Test
    void test14() {
        assertTrue(AsteriskVersion.getDetermineVersionFromString("Asterisk 14.1.0").equals(AsteriskVersion.ASTERISK_14));
    }

    @Test
    void test15() {
        assertTrue(AsteriskVersion.getDetermineVersionFromString("Asterisk 15.1.0").equals(AsteriskVersion.ASTERISK_15));
    }

    @Test
    void test18() {
        assertTrue(AsteriskVersion.getDetermineVersionFromString("Asterisk 18.1.0").equals(AsteriskVersion.ASTERISK_18));
    }
    @Test
    void testCertified18() {
        assertTrue(AsteriskVersion.getDetermineVersionFromString("Asterisk certified/18.9-cert2").equals(AsteriskVersion.ASTERISK_18));
    }
}
