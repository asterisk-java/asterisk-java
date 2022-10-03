package org.asteriskjava.fastagi;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class CompositeMappingStrategyTest {
    private CompositeMappingStrategy strategy;

    @BeforeEach
    void setUp() {
        strategy = new CompositeMappingStrategy(new ResourceBundleMappingStrategy("test-mapping"),
                new ClassNameMappingStrategy());
    }

    @Test
    void testAJ37ResourceBundle() {
        AgiRequest request = new SimpleAgiRequest();
        AgiScript script = strategy.determineScript(request, null);

        assertNotNull(script, "no script determined");
        assertEquals(script.getClass(), HelloAgiScript.class, "incorrect script determined");
    }

    @Test
    void testAJ37ClassName() {
        AgiRequest request = new SimpleAgiRequest("org.asteriskjava.fastagi.HelloAgiScript");
        AgiScript script = strategy.determineScript(request, null);

        assertNotNull(script, "no script determined");
        assertEquals(script.getClass(), HelloAgiScript.class, "incorrect script determined");
    }
}
