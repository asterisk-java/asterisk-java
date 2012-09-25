package org.asteriskjava.live;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class HangupCauseTest
{
    @Test
    public void testGetByCode()
    {
        assertEquals("Valid enum for cause code 18", HangupCause.AST_CAUSE_NO_USER_RESPONSE, HangupCause.getByCode(18));
    }
}
