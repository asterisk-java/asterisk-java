package org.asteriskjava.util;

import java.util.TimeZone;

import junit.framework.TestCase;

public class DateUtilTest extends TestCase
{
    TimeZone defaultTimeZone;
    final String dateString = "2006-05-19 11:54:48";

    @Override
    protected void setUp() throws Exception
    {
        defaultTimeZone = TimeZone.getDefault();
        TimeZone.setDefault(TimeZone.getTimeZone("GMT"));
    }

    @Override
    protected void tearDown() throws Exception
    {
        TimeZone.setDefault(defaultTimeZone);
    }

    public void testGetStartTimeAsDate()
    {
        assertEquals(1148039688000L, DateUtil.parseDateTime(dateString).getTime());
    }

    public void testGetStartTimeAsDateWithTimeZone()
    {
        TimeZone tz = TimeZone.getTimeZone("GMT+2");
        assertEquals(1148032488000L, DateUtil.parseDateTime(dateString, tz).getTime());
    }
}
