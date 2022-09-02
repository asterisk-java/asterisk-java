package org.asteriskjava.manager.event;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SkypeChatMessageEventTest {
    @Test
    void testGetDecodedMessage() {
        final SkypeChatMessageEvent event = new SkypeChatMessageEvent(this);
        event.setMessage("aMO2w7bDtj8=");
        assertEquals("h\u00F6\u00F6\u00F6?", event.getDecodedMessage(), "Inocrrectly decoded message");
    }
}
