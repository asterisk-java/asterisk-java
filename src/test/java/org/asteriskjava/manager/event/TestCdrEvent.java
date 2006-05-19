package org.asteriskjava.manager.event;

import java.util.TimeZone;

import junit.framework.TestCase;

public class TestCdrEvent extends TestCase
{
    CdrEvent cdrEvent;
    TimeZone defaultTimeZone;
    
    @Override
    protected void setUp() throws Exception
    {
        cdrEvent = new CdrEvent(this);
        cdrEvent.setStartTime("2006-05-19 11:54:48");
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
        assertEquals(1137671688000L, cdrEvent.getStartTimeAsDate().getTime());
    }

    public void testGetStartTimeAsDateWithTimeZone()
    {
        TimeZone tz = TimeZone.getTimeZone("GMT+2");
        assertEquals(1137664488000L, cdrEvent.getStartTimeAsDate(tz).getTime());
    }
}
