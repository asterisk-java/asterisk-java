package org.asteriskjava.live;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HangupCauseTest {
    @Test
    void testGetByCode() {
        assertEquals(HangupCause.AST_CAUSE_NO_USER_RESPONSE, HangupCause.getByCode(18), "Valid enum for cause code 18");
    }
}
