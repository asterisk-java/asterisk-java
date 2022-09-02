package org.asteriskjava.util;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.TimeZone;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DateUtilTest {
    TimeZone defaultTimeZone;
    final String dateString = "2006-05-19 11:54:48";

    @BeforeEach
    void setUp() {
        defaultTimeZone = TimeZone.getDefault();
        TimeZone.setDefault(TimeZone.getTimeZone("GMT"));
    }

    @AfterEach
    void tearDown() {
        TimeZone.setDefault(defaultTimeZone);
    }

    @Test
    void testGetStartTimeAsDate() {
        final Date result = DateUtil.parseDateTime(dateString);
        assertEquals(1148039688000L, result.getTime());
        assertEquals("Fri May 19 11:54:48 GMT 2006", result.toString());
    }

    @Test
    void testGetStartTimeAsDateWithTimeZone() {
        final TimeZone tz = TimeZone.getTimeZone("GMT+2");
        final Date result = DateUtil.parseDateTime(dateString, tz);
        assertEquals(1148032488000L, result.getTime());
        assertEquals("Fri May 19 09:54:48 GMT 2006", result.toString());
    }
}
