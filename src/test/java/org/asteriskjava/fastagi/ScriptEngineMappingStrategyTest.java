package org.asteriskjava.fastagi;

import static org.asteriskjava.fastagi.ScriptEngineMappingStrategy.getExtension;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

public class ScriptEngineMappingStrategyTest
{
    private ScriptEngineMappingStrategy scriptEngineMappingStrategy;

    @Before
    public void setUp()
    {
        this.scriptEngineMappingStrategy = new ScriptEngineMappingStrategy();
    }

    @Test
    public void testSearchFile() throws IOException
    {
        assertNull(scriptEngineMappingStrategy.searchFile(null, new String[]{"src", "test", "."}));
        assertNull(scriptEngineMappingStrategy.searchFile("pom.xml", null));
        assertNull(scriptEngineMappingStrategy.searchFile("pom.xml", new String[]{}));
        assertNull(scriptEngineMappingStrategy.searchFile("pom.xml", new String[]{"src", "test"}));
        assertEquals(new File("pom.xml").getCanonicalPath(),
                scriptEngineMappingStrategy.searchFile("pom.xml", new String[]{"bla", "src", "."}).getPath());
    }

    @Test
    public void testSearchFileOutsidePath()
    {
        // file should not be found for security reasons if it is not below the
        // path
        assertNull(scriptEngineMappingStrategy.searchFile("../pom.xml", new String[]{"src"}));
    }

    @Test
    public void testGetExtension()
    {
        assertEquals("txt", getExtension("hello.txt"));
        assertEquals("txt", getExtension("/some/path/hello.txt"));
        assertEquals("txt", getExtension("C:\\some\\path\\hello.txt"));
        assertEquals("txt", getExtension("C:\\some\\path\\hel.lo.txt"));
        assertEquals("txt", getExtension("C:\\some\\pa.th\\hel.lo.txt"));
        assertEquals("txt", getExtension(".txt"));

        assertEquals(null, getExtension(null));
        assertEquals(null, getExtension(""));

        assertEquals(null, getExtension("hello"));
        assertEquals(null, getExtension("/some/path/hello"));
        assertEquals(null, getExtension("/some/pa.th/hello"));
        assertEquals(null, getExtension("C:\\some\\path\\hello"));
        assertEquals(null, getExtension("C:\\some\\pa.th\\hello"));

        assertEquals(null, getExtension("/some/pa.th\\hello"));
        assertEquals(null, getExtension("C:\\some\\pa.th/hello"));
    }
}
