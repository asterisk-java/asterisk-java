package org.asteriskjava.manager.event;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SkypeBuddyStatusEventTest {
    private SkypeBuddyStatusEvent event;

    @BeforeEach
    void setUp() {
        event = new SkypeBuddyStatusEvent(this);
        event.setBuddy("Skype/user@the.buddy");
    }

    @Test
    void testGetUser() {
        assertEquals("user", event.getUser());
    }

    @Test
    void testGetBuddySkypename() {
        assertEquals("the.buddy", event.getBuddySkypename());
    }
}
