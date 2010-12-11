package org.asteriskjava.manager.event;

import junit.framework.TestCase;

public class SkypeChatMessageEventTest extends TestCase
{
    public void testGetDecodedMessage()
    {
        final SkypeChatMessageEvent event = new SkypeChatMessageEvent(this);
        event.setMessage("aMO2w7bDtj8=");
        assertEquals("Inocrrectly decoded message", "h\u00F6\u00F6\u00F6?", event.getDecodedMessage());
    }
}
