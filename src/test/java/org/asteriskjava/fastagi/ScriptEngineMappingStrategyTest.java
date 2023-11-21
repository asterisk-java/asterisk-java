package org.asteriskjava.fastagi;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.asteriskjava.fastagi.ScriptEngineMappingStrategy.getExtension;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class ScriptEngineMappingStrategyTest {
    private ScriptEngineMappingStrategy scriptEngineMappingStrategy;

    @BeforeEach
    void setUp() {
        this.scriptEngineMappingStrategy = new ScriptEngineMappingStrategy();
    }

    @Test
    void testSearchFile() throws IOException {
        assertNull(scriptEngineMappingStrategy.searchFile(null, new String[]{"src", "test", "."}));
        assertNull(scriptEngineMappingStrategy.searchFile("pom.xml", null));
        assertNull(scriptEngineMappingStrategy.searchFile("pom.xml", new String[]{}));
        assertNull(scriptEngineMappingStrategy.searchFile("pom.xml", new String[]{"src", "test"}));
//        assertEquals(new File("pom.xml").getCanonicalPath(), scriptEngineMappingStrategy.searchFile("pom.xml", new String[]{"bla", "src", "."}).getPath());
    }

    @Test
    void testSearchFileOutsidePath() {
        // file should not be found for security reasons if it is not below the
        // path
        assertNull(scriptEngineMappingStrategy.searchFile("../pom.xml", new String[]{"src"}));
    }

    @Test
    void testGetExtension() {
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
