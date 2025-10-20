package org.asteriskjava.manager.response;

import org.asteriskjava.ami.action.api.response.CoreStatusActionResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.TimeZone;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class CoreStatusActionResponseTest {
    private TimeZone tz = TimeZone.getTimeZone("Europe/Berlin");
    private CoreStatusActionResponse response;
    private TimeZone defaultTimeZone;

    @BeforeEach
    void setUp() {
        this.response = new CoreStatusActionResponse();
        defaultTimeZone = TimeZone.getDefault();
        TimeZone.setDefault(tz);
    }

    @AfterEach
    void tearDown() {
        TimeZone.setDefault(defaultTimeZone);
    }

    @Test
    void testGetCoreStartupTimeAsDate() {
        assertNotNull(tz, "TimeZone not found");
//        response.setCoreStartupDate("2009-05-27");
//        response.setCoreStartupTime("02:49:15");

//        assertEquals("Wed May 27 02:49:15 CEST 2009", response.getCoreStartupDateTimeAsDate(tz).toString());
    }

    @Test
    void testGetCoreStartupTimeAsDateIfDateIsNull() {
        assertNotNull(tz, "TimeZone not found");
        response.setCoreStartupDate(null); // before Asterisk 1.6.2
//        response.setCoreStartupTime("02:49:15");

//        assertNull(response.getCoreStartupDateTimeAsDate(tz));
    }
}
