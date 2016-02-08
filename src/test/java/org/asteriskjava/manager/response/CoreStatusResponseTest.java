package org.asteriskjava.manager.response;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.TimeZone;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CoreStatusResponseTest
{
    private TimeZone tz = TimeZone.getTimeZone("Europe/Berlin");
    private CoreStatusResponse response;
    private TimeZone defaultTimeZone;

    @Before
    public void setUp()
    {
        this.response = new CoreStatusResponse();
        defaultTimeZone = TimeZone.getDefault();
        TimeZone.setDefault(tz);
    }

    @After
    public void tearDown()
    {
        TimeZone.setDefault(defaultTimeZone);
    }

    @Test
    public void testGetCoreStartupTimeAsDate()
    {
        assertNotNull("TimeZone not found", tz);
        response.setCoreStartupDate("2009-05-27");
        response.setCoreStartupTime("02:49:15");

        assertEquals("Wed May 27 02:49:15 CEST 2009", response.getCoreStartupDateTimeAsDate(tz).toString());
    }

    @Test
    public void testGetCoreStartupTimeAsDateIfDateIsNull()
    {
        assertNotNull("TimeZone not found", tz);
        response.setCoreStartupDate(null); // before Asterisk 1.6.2
        response.setCoreStartupTime("02:49:15");

        assertNull(response.getCoreStartupDateTimeAsDate(tz));
    }
}
