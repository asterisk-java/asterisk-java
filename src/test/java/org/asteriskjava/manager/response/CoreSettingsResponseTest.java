package org.asteriskjava.manager.response;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CoreSettingsResponseTest {

    @Test
    public void testCoreRealtimeEnabled() {
        CoreSettingsResponse coreSettingsResponse = new CoreSettingsResponse();//object of core setting responce
        coreSettingsResponse.setCoreRealtimeEnabled(true);
        boolean result = coreSettingsResponse.isCoreRealtimeEnabled(); // storing the value in boolean
        assertTrue(result);
    }

    @Test
    public void testCoreCdrEnabled() {
        CoreSettingsResponse coreSettingsResponse = new CoreSettingsResponse();
        coreSettingsResponse.setCoreCdrEnabled(false);
        boolean result = coreSettingsResponse.isCoreCdrEnabled();
        assertFalse(result);
    }

    @Test
    public void testCoreHttpEnabled() {
        CoreSettingsResponse coreSettingsResponse = new CoreSettingsResponse();
        coreSettingsResponse.setCoreHttpEnabled(true);
        boolean result = coreSettingsResponse.isCoreHttpEnabled();
        assertTrue(result);
    }
}