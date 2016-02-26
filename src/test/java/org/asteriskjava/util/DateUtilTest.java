package org.asteriskjava.util;

import static org.junit.Assert.assertEquals;

import java.util.Date;
import java.util.TimeZone;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class DateUtilTest
{
    TimeZone defaultTimeZone;
    final String dateString = "2006-05-19 11:54:48";

    @Before
    public void setUp()
    {
        defaultTimeZone = TimeZone.getDefault();
        TimeZone.setDefault(TimeZone.getTimeZone("GMT"));
    }

    @After
    public void tearDown()
    {
        TimeZone.setDefault(defaultTimeZone);
    }

    @Test
    public void testGetStartTimeAsDate()
    {
        final Date result = DateUtil.parseDateTime(dateString);
        assertEquals(1148039688000L, result.getTime());
        assertEquals("Fri May 19 11:54:48 GMT 2006", result.toString());
    }

    @Test
    public void testGetStartTimeAsDateWithTimeZone()
    {
        final TimeZone tz = TimeZone.getTimeZone("GMT+2");
        final Date result = DateUtil.parseDateTime(dateString, tz);
        assertEquals(1148032488000L, result.getTime());
        assertEquals("Fri May 19 09:54:48 GMT 2006", result.toString());
    }
}
