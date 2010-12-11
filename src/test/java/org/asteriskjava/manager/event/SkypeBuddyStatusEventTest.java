package org.asteriskjava.manager.event;

import junit.framework.TestCase;

public class SkypeBuddyStatusEventTest extends TestCase
{
    private SkypeBuddyStatusEvent event;

    @Override
    public void setUp()
    {
        event = new SkypeBuddyStatusEvent(this);
        event.setBuddy("Skype/user@the.buddy");
    }

    public void testGetUser() throws Exception
    {
        assertEquals("user", event.getUser());
    }

    public void testGetBuddySkypename() throws Exception
    {
        assertEquals("the.buddy", event.getBuddySkypename());
    }
}
