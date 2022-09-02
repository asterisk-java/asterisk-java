package org.asteriskjava.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.CharBuffer;

import static org.junit.jupiter.api.Assertions.*;

class ConfigFileParserTest {
    ConfigFileReader configFileReader;

    @BeforeEach
    void setUp() {
        configFileReader = new ConfigFileReader();
    }

    @Test
    void testProcessLine() throws Exception {
        String s = " a ;-- comment --; = b ; a line comment";
        CharBuffer buffer = CharBuffer.allocate(s.length());

        buffer.put(s);
        buffer.flip();

        configFileReader.appendCategory(new Category("cat"));
        ConfigElement configElement = configFileReader.processLine("test.conf", 1, buffer);
        assertEquals(ConfigVariable.class, configElement.getClass(), "Incorrect type of configElement");

        ConfigVariable configVariable = (ConfigVariable) configElement;
        assertEquals("a", configVariable.getName(), "Incorrect variable name");
        assertEquals("b", configVariable.getValue(), "Incorrect variable value");
        assertEquals("a line comment", configElement.getComment(), "Incorrect comment");
    }

    @Test
    void testParseCategoryHeader() throws Exception {
        Category category;

        category = configFileReader.parseCategoryHeader("test.conf", 1, "[foo]");
        assertEquals("foo", category.getName(), "Incorrect category name");
        assertEquals(1, category.getLineNumber(), "Incorrect line number");
        assertEquals("test.conf", category.getFileName(), "Incorrect file name");

        category = configFileReader.parseCategoryHeader("test.conf", 1, "[foo](!)");
        assertEquals("foo", category.getName(), "Incorrect category name");
        assertTrue(category.isTemplate(), "Category not flagged as template");

        category = configFileReader.parseCategoryHeader("test.conf", 1, "[foo](+)");
        assertEquals("foo", category.getName(), "Incorrect category name");

        try {
            configFileReader.parseCategoryHeader("test.conf", 1, "[foo](a)");
            fail("Expected exception when requesting inheritance from a non-existing catagory");
        } catch (ConfigParseException e) {
            assertEquals("Inheritance requested, but category 'a' does not exist, line 1 of test.conf", e.getMessage());
            assertEquals(1, e.getLineNumber(), "Incorrect line number");
            assertEquals("test.conf", e.getFileName(), "Incorrect file name");
        }

        try {
            configFileReader.parseCategoryHeader("test.conf", 1, "[foo");
            fail("Expected exception when closing ']' is missing");
        } catch (ConfigParseException e) {
            assertEquals(e.getMessage(), "parse error: no closing ']', line 1 of test.conf");
        }

        try {
            configFileReader.parseCategoryHeader("test.conf", 1, "[foo](bar");
            fail("Expected exception when closing ')' is missing");
        } catch (ConfigParseException e) {
            assertEquals(e.getMessage(), "parse error: no closing ')', line 1 of test.conf");
        }
    }

    @Test
    void testParseDirective() throws ConfigParseException {
        ConfigDirective configDirective;

        configDirective = configFileReader.parseDirective("abc.conf", 20, "#include \"/etc/asterisk/inc.conf\"");
        assertEquals(IncludeDirective.class, configDirective.getClass(), "Incorrect type of configDirective");
        assertEquals("/etc/asterisk/inc.conf", ((IncludeDirective) configDirective).getIncludeFile(), "Incorrect include file");
        assertEquals(20, configDirective.getLineNumber(), "Incorrect line number");
        assertEquals("abc.conf", configDirective.getFileName(), "Incorrect file name");

        configDirective = configFileReader.parseDirective("abc.conf", 20, "#exec   </usr/local/test.sh>   ");
        assertEquals(ExecDirective.class, configDirective.getClass(), "Incorrect type of configDirective");
        assertEquals("/usr/local/test.sh", ((ExecDirective) configDirective).getExecFile(), "Incorrect exec file");
        assertEquals(20, configDirective.getLineNumber(), "Incorrect line number");
        assertEquals("abc.conf", configDirective.getFileName(), "Incorrect file name");

        try {
            configFileReader.parseDirective("abc.conf", 20, "#foo");
            fail("Expected exception when parsing a line with an unknown directive");
        } catch (UnknownDirectiveException e) {
            assertEquals("Unknown directive 'foo' at line 20 of abc.conf", e.getMessage());
        }

        try {
            configFileReader.parseDirective("/etc/asterisk/sip.conf", 805, "#include   ");
            fail("Expected exception when parsing a line with a directive but no parameter");
        } catch (MissingDirectiveParameterException e) {
            assertEquals("Directive '#include' needs an argument (filename) at line 805 of /etc/asterisk/sip.conf",
                    e.getMessage());
        }
    }

    @Test
    void testParseVariable() throws ConfigParseException {
        ConfigVariable variable;

        variable = configFileReader.parseVariable("extensions.conf", 20, "exten => s-NOANSWER,1,Hangup");
        assertEquals("exten", variable.getName(), "Incorrect name");
        assertEquals("s-NOANSWER,1,Hangup", variable.getValue(), "Incorrect value");
        assertEquals(20, variable.getLineNumber(), "Incorrect line number");
        assertEquals("extensions.conf", variable.getFileName(), "Incorrect file name");

        variable = configFileReader.parseVariable("extensions.conf", 20, "foo=");
        assertEquals("foo", variable.getName(), "Incorrect name");
        assertEquals("", variable.getValue(), "Incorrect value");

        try {
            configFileReader.parseVariable("extensions.conf", 20, "foo");
            fail("Expected exception when parsing a line without a '='");
        } catch (MissingEqualSignException e) {
            assertEquals("No '=' (equal sign) in line 20 of extensions.conf", e.getMessage());
        }
    }
}
