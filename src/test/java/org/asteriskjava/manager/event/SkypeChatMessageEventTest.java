package org.asteriskjava.manager.event;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class SkypeChatMessageEventTest
{
    @Test
    public void testGetDecodedMessage()
    {
        final SkypeChatMessageEvent event = new SkypeChatMessageEvent(this);
        event.setMessage("aMO2w7bDtj8=");
        assertEquals("Inocrrectly decoded message", "h\u00F6\u00F6\u00F6?", event.getDecodedMessage());
    }
}
