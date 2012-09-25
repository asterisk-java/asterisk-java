package org.asteriskjava.manager.event;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class SkypeBuddyStatusEventTest
{
    private SkypeBuddyStatusEvent event;

    @Before
    public void setUp()
    {
        event = new SkypeBuddyStatusEvent(this);
        event.setBuddy("Skype/user@the.buddy");
    }

    @Test
    public void testGetUser() throws Exception
    {
        assertEquals("user", event.getUser());
    }

    @Test
    public void testGetBuddySkypename() throws Exception
    {
        assertEquals("the.buddy", event.getBuddySkypename());
    }
}
