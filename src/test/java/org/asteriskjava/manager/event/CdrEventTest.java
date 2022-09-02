package org.asteriskjava.manager.event;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.TimeZone;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CdrEventTest {
    CdrEvent cdrEvent;
    TimeZone defaultTimeZone;

    @BeforeEach
    void setUp() {
        cdrEvent = new CdrEvent(this);
        cdrEvent.setStartTime("2006-05-19 11:54:48");
        defaultTimeZone = TimeZone.getDefault();
        TimeZone.setDefault(TimeZone.getTimeZone("GMT"));
    }

    @AfterEach
    void tearDown() {
        TimeZone.setDefault(defaultTimeZone);
    }

    @Test
    void testGetStartTimeAsDate() {
        assertEquals(1148039688000L, cdrEvent.getStartTimeAsDate().getTime());
    }

    @Test
    void testGetStartTimeAsDateWithTimeZone() {
        TimeZone tz = TimeZone.getTimeZone("GMT+2");
        assertEquals(1148032488000L, cdrEvent.getStartTimeAsDate(tz).getTime());
    }

    @Test
    void testBug() {
        TimeZone.setDefault(TimeZone.getTimeZone("Europe/Monaco"));

        cdrEvent.setStartTime("2006-05-29 13:17:21");
        assertEquals("Mon May 29 13:17:21 CEST 2006", cdrEvent.getStartTimeAsDate().toString());
    }
}
