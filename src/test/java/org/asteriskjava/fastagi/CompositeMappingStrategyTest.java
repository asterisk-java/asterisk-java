package org.asteriskjava.fastagi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

public class CompositeMappingStrategyTest
{
    private CompositeMappingStrategy strategy;

    @Before
    public void setUp()
    {
        strategy = new CompositeMappingStrategy(new ResourceBundleMappingStrategy("test-mapping"),
                new ClassNameMappingStrategy());
    }

    @Test
    public void testAJ37ResourceBundle()
    {
        AgiRequest request = new SimpleAgiRequest();
        AgiScript script = strategy.determineScript(request, null);

        assertNotNull("no script determined", script);
        assertEquals("incorrect script determined", script.getClass(), HelloAgiScript.class);
    }

    @Test
    public void testAJ37ClassName()
    {
        AgiRequest request = new SimpleAgiRequest("org.asteriskjava.fastagi.HelloAgiScript");
        AgiScript script = strategy.determineScript(request, null);

        assertNotNull("no script determined", script);
        assertEquals("incorrect script determined", script.getClass(), HelloAgiScript.class);
    }
}
