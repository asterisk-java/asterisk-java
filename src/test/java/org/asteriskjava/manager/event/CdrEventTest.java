package org.asteriskjava.manager.event;

import static org.junit.Assert.assertEquals;

import java.util.TimeZone;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CdrEventTest
{
    CdrEvent cdrEvent;
    TimeZone defaultTimeZone;

    @Before
    public void setUp()
    {
        cdrEvent = new CdrEvent(this);
        cdrEvent.setStartTime("2006-05-19 11:54:48");
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
        assertEquals(1148039688000L, cdrEvent.getStartTimeAsDate().getTime());
    }

    @Test
    public void testGetStartTimeAsDateWithTimeZone()
    {
        TimeZone tz = TimeZone.getTimeZone("GMT+2");
        assertEquals(1148032488000L, cdrEvent.getStartTimeAsDate(tz).getTime());
    }

    @Test
    public void testBug()
    {
        TimeZone.setDefault(TimeZone.getTimeZone("Europe/Monaco"));

        cdrEvent.setStartTime("2006-05-29 13:17:21");
        assertEquals("Mon May 29 13:17:21 CEST 2006", cdrEvent.getStartTimeAsDate().toString());
    }
}
