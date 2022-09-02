/*
 *  Copyright 2004-2006 Stefan Reuter
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */
package org.asteriskjava.fastagi.internal;

import org.asteriskjava.AsteriskVersion;
import org.asteriskjava.fastagi.AgiRequest;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AgiRequestImplTest {
    @SuppressWarnings("deprecation")
    @Test
    void testBuildRequest() {
        List<String> lines;
        AgiRequest request;

        lines = new ArrayList<String>();

        lines.add("agi_network: yes");
        lines.add("agi_network_script: myscript.agi");
        lines.add("agi_request: agi://host/myscript.agi");
        lines.add("agi_channel: SIP/1234-d715");
        lines.add("agi_language: en");
        lines.add("agi_type: SIP");
        lines.add("agi_uniqueid: 1110023416.6");
        lines.add("agi_callerid: John Doe<1234>");
        lines.add("agi_dnid: 8870");
        lines.add("agi_rdnis: 9876");
        lines.add("agi_context: local");
        lines.add("agi_extension: 8870");
        lines.add("agi_priority: 1");
        lines.add("agi_enhanced: 0.0");
        lines.add("agi_accountcode: ");
        lines.add("agi_version: 13.1.0~dfsg-1.1ubuntu4.1");

        request = new AgiRequestImpl(lines);

        assertEquals("myscript.agi", request.getScript(), "incorrect script");
        assertEquals("agi://host/myscript.agi", request.getRequestURL(), "incorrect requestURL");
        assertEquals("SIP/1234-d715", request.getChannel(), "incorrect channel");
        assertEquals("SIP/1234-d715", request.getChannel(), "incorrect uniqueId");
        assertEquals("SIP", request.getType(), "incorrect type");
        assertEquals("1110023416.6", request.getUniqueId(), "incorrect uniqueId");
        assertEquals("en", request.getLanguage(), "incorrect language");
        assertEquals("1234", request.getCallerId(), "incorrect callerId");
        assertEquals("John Doe", request.getCallerIdName(), "incorrect callerIdName");
        assertEquals("8870", request.getDnid(), "incorrect dnid");
        assertEquals("9876", request.getRdnis(), "incorrect rdnis");
        assertEquals("local", request.getContext(), "incorrect context");
        assertEquals("8870", request.getExtension(), "incorrect extension");
        assertEquals(new Integer(1), request.getPriority(), "incorrect priority");
        assertEquals(Boolean.FALSE, request.getEnhanced(), "incorrect enhanced");
        assertNull(request.getAccountCode(), "incorrect accountCode must not be set");
        assertEquals(AsteriskVersion.ASTERISK_13, request.getAsteriskVersion(), "incorret Asterisk Version Number");
    }

    @Test
    void testBuildRequestWithAccountCode() {
        List<String> lines;
        AgiRequest request;

        lines = new ArrayList<String>();

        lines.add("agi_network: yes");
        lines.add("agi_network_script: myscript.agi");
        lines.add("agi_accountcode: 12345");

        request = new AgiRequestImpl(lines);

        assertEquals("12345", request.getAccountCode(), "incorrect accountCode");
    }

    @SuppressWarnings("deprecation")
    @Test
    void testBuildRequestWithoutCallerIdName() {
        List<String> lines;
        AgiRequest request;

        lines = new ArrayList<String>();

        lines.add("agi_callerid: 1234");

        request = new AgiRequestImpl(lines);

        assertEquals("1234", request.getCallerId(), "incorrect callerId");
        assertEquals("1234", request.getCallerIdNumber(), "incorrect callerIdNumber");
        // assertNull("callerIdName must not be set",
        // request.getCallerIdName());
    }

    @SuppressWarnings("deprecation")
    @Test
    void testBuildRequestWithoutCallerIdNameButBracket() {
        List<String> lines;
        AgiRequest request;

        lines = new ArrayList<String>();

        lines.add("agi_callerid: <1234>");

        request = new AgiRequestImpl(lines);

        assertEquals("1234", request.getCallerId(), "incorrect callerId");
        assertEquals("1234", request.getCallerIdNumber(), "incorrect callerIdNumber");
        assertNull(request.getCallerIdName(), "callerIdName must not be set");
    }

    @SuppressWarnings("deprecation")
    @Test
    void testBuildRequestWithoutCallerIdNameButBracketAndQuotesAndSpace() {
        List<String> lines;
        AgiRequest request;

        lines = new ArrayList<String>();

        lines.add("agi_callerid: \"\" <1234>");

        request = new AgiRequestImpl(lines);

        assertEquals("1234", request.getCallerId(), "incorrect callerId");
        assertEquals("1234", request.getCallerIdNumber(), "incorrect callerIdNumber");
        assertNull(request.getCallerIdName(), "callerIdName must not be set");
    }

    @SuppressWarnings("deprecation")
    @Test
    void testBuildRequestWithQuotedCallerIdName() {
        List<String> lines;
        AgiRequest request;

        lines = new ArrayList<String>();

        lines.add("agi_callerid: \"John Doe\"<1234>");

        request = new AgiRequestImpl(lines);

        assertEquals("1234", request.getCallerId(), "incorrect callerId");
        assertEquals("1234", request.getCallerIdNumber(), "incorrect callerIdNumber");
        assertEquals("John Doe", request.getCallerIdName(), "incorrect callerIdName");
    }

    @SuppressWarnings("deprecation")
    @Test
    void testBuildRequestWithQuotedCallerIdNameAndSpace() {
        List<String> lines;
        AgiRequest request;

        lines = new ArrayList<String>();

        lines.add("agi_callerid: \"John Doe\" <1234>");

        request = new AgiRequestImpl(lines);

        assertEquals("1234", request.getCallerId(), "incorrect callerId");
        assertEquals("1234", request.getCallerIdNumber(), "incorrect callerIdNumber");
        assertEquals("John Doe", request.getCallerIdName(), "incorrect callerIdName");
    }

    @SuppressWarnings("deprecation")
    @Test
    void testBuildRequestWithoutCallerId() {
        List<String> lines;
        AgiRequest request;

        lines = new ArrayList<String>();

        lines.add("agi_callerid: ");

        request = new AgiRequestImpl(lines);

        assertNull(request.getCallerId(), "callerId must not be set");
        assertNull(request.getCallerIdNumber(), "callerIdNumber must not be set");
        assertNull(request.getCallerIdName(), "callerIdName must not be set");
    }

    /*
     * Asterisk 1.2 now uses agi_callerid and agi_calleridname so we don't need
     * to process it ourselves.
     */
    @SuppressWarnings("deprecation")
    @Test
    void testBuildRequestCallerIdAsterisk12() {
        List<String> lines;
        AgiRequest request;

        lines = new ArrayList<String>();

        lines.add("agi_callerid: 1234");
        lines.add("agi_calleridname: John Doe");

        request = new AgiRequestImpl(lines);

        assertEquals("1234", request.getCallerId(), "incorrect callerId");
        assertEquals("1234", request.getCallerIdNumber(), "incorrect callerIdNumber");
        assertEquals("John Doe", request.getCallerIdName(), "incorrect callerIdName");
    }

    @SuppressWarnings("deprecation")
    @Test
    void testBuildRequestCallerIdAsterisk12WithUnknownCallerId() {
        List<String> lines;
        AgiRequest request;

        lines = new ArrayList<String>();

        lines.add("agi_callerid: unknown");
        lines.add("agi_calleridname: John Doe");

        request = new AgiRequestImpl(lines);

        assertNull(request.getCallerId(), "callerId must not be set if \"unknown\"");
        assertNull(request.getCallerIdNumber(), "callerIdNumber must not be set if \"unknown\"");
        assertEquals("John Doe", request.getCallerIdName(), "incorrect callerIdName");
    }

    @SuppressWarnings("deprecation")
    @Test
    void testBuildRequestCallerIdAsterisk12WithUnknownCallerIdName() {
        List<String> lines;
        AgiRequest request;

        lines = new ArrayList<String>();

        lines.add("agi_callerid: 1234");
        lines.add("agi_calleridname: unknown");

        request = new AgiRequestImpl(lines);

        assertEquals("1234", request.getCallerId(), "incorrect callerId");
        assertEquals("1234", request.getCallerIdNumber(), "incorrect callerIdNumber");
        assertNull(request.getCallerIdName(), "callerIdName must not be set if \"unknown\"");
    }

    @Test
    void testBuildRequestCallerIdWithUnknownDnid() {
        List<String> lines;
        AgiRequest request;

        lines = new ArrayList<String>();

        lines.add("agi_dnid: unknown");

        request = new AgiRequestImpl(lines);

        assertNull(request.getDnid(), "dnid must not be set if \"unknown\"");
    }

    @Test
    void testBuildRequestCallerIdWithUnknownRdnis() {
        List<String> lines;
        AgiRequest request;

        lines = new ArrayList<String>();

        lines.add("agi_rdnis: unknown");

        request = new AgiRequestImpl(lines);

        assertNull(request.getRdnis(), "rdnis must not be set if \"unknown\"");
    }

    @Test
    void testBuildRequestWithNullEnvironment() {
        try {
            new AgiRequestImpl(null);
            fail("No IllegalArgumentException thrown.");
        } catch (IllegalArgumentException e) {
        }
    }

    @Test
    void testBuildRequestWithUnusualInput() {
        List<String> lines;
        AgiRequest request;

        lines = new ArrayList<String>();

        lines.add("var without agi prefix: a value");
        lines.add("agi_without_colon another value");
        lines.add("agi_without_space_after_colon:");
        lines.add("agi_channel: SIP/1234-a892");

        request = new AgiRequestImpl(lines);

        assertEquals("SIP/1234-a892", request.getChannel(), "incorrect channel");
    }

    @Test
    void testBuildRequestWithoutParameters() {
        List<String> lines;
        AgiRequest request;

        lines = new ArrayList<String>();

        lines.add("agi_network_script: myscript.agi");
        lines.add("agi_request: agi://host/myscript.agi");

        request = new AgiRequestImpl(lines);

        assertEquals("myscript.agi", request.getScript(), "incorrect script");
        assertEquals("agi://host/myscript.agi", request.getRequestURL(), "incorrect requestURL");
        assertEquals(null, request.getParameter("param1"), "incorrect value for unset parameter 'param1'");
        assertNotNull(request.getParameterValues("param1"), "getParameterValues() must not return null");
        assertEquals(0, request.getParameterValues("param1").length, "incorrect size of values for unset parameter 'param1'");
        assertNotNull(request.getParameterMap(), "getParameterMap() must not return null");
        assertEquals(0, request.getParameterMap().size(), "incorrect size of getParameterMap()");
    }

    @Test
    void testBuildRequestWithSingleValueParameters() {
        List<String> lines;
        AgiRequest request;

        lines = new ArrayList<String>();

        lines.add("agi_network_script: myscript.agi?param1=value1&param2=value2");
        lines.add("agi_request: agi://host/myscript.agi?param1=value1&param2=value2");

        request = new AgiRequestImpl(lines);

        assertEquals("myscript.agi", request.getScript(), "incorrect script");
        assertEquals("agi://host/myscript.agi?param1=value1&param2=value2", request.getRequestURL(), "incorrect requestURL");
        assertEquals("value1", request.getParameter("param1"), "incorrect value for parameter 'param1'");
        assertEquals("value2", request.getParameter("param2"), "incorrect value for parameter 'param2'");
        assertEquals(null, request.getParameter("param3"), "incorrect value for unset parameter 'param3'");
        assertEquals(2, request.getParameterMap().size(), "incorrect size of getParameterMap()");
        assertEquals("value1", request.getParameterMap().get("param1")[0], "incorrect value for parameter 'param1' when obtained from map");
    }

    @Test
    void testBuildRequestWithMultiValueParameter() {
        List<String> lines;
        AgiRequest request;

        lines = new ArrayList<String>();

        lines.add("agi_network_script: myscript.agi?param1=value1&param1=value2");
        lines.add("agi_request: agi://host/myscript.agi?param1=value1&param1=value2");

        request = new AgiRequestImpl(lines);

        assertEquals("myscript.agi", request.getScript(), "incorrect script");
        assertEquals("agi://host/myscript.agi?param1=value1&param1=value2", request.getRequestURL(), "incorrect requestURL");
        assertEquals(2, request.getParameterValues("param1").length, "incorrect number of values for parameter 'param1'");
        assertEquals("value1", request.getParameterValues("param1")[0], "incorrect value[0] for parameter 'param1'");
        assertEquals("value2", request.getParameterValues("param1")[1], "incorrect value[1] for parameter 'param1'");
    }

    @Test
    void testBuildRequestWithEmptyValueParameter() {
        List<String> lines;
        AgiRequest request;

        lines = new ArrayList<String>();

        lines.add("agi_network_script: myscript.agi?param1");
        lines.add("agi_request: agi://host/myscript.agi?param1");

        request = new AgiRequestImpl(lines);

        assertEquals("myscript.agi", request.getScript(), "incorrect script");
        assertEquals("agi://host/myscript.agi?param1", request.getRequestURL(), "incorrect requestURL");
        assertEquals("", request.getParameter("param1"), "incorrect value for parameter 'param1'");
        assertEquals(1, request.getParameterValues("param1").length, "incorrect number of values for parameter 'param1'");
        assertEquals("", request.getParameterValues("param1")[0], "incorrect value[0] for parameter 'param1'");
    }

    @Test
    void testBuildRequestWithUrlEncodedParameter() {
        List<String> lines;
        AgiRequest request;

        lines = new ArrayList<String>();

        lines.add("agi_network_script: myscript.agi?param1=my%20value");
        lines.add("agi_request: agi://host/myscript.agi?param1=my%20value");

        request = new AgiRequestImpl(lines);

        assertEquals("myscript.agi", request.getScript(), "incorrect script");
        assertEquals("agi://host/myscript.agi?param1=my%20value", request.getRequestURL(), "incorrect requestURL");
        assertEquals("my value", request.getParameter("param1"), "incorrect value for parameter 'param1'");
    }

    @Test
    void testGetParameter() {
        List<String> lines;
        AgiRequest request;

        lines = new ArrayList<String>();

        lines.add("agi_network_script: myscript.agi?param1=my%20value");
        lines.add("agi_request: agi://host/myscript.agi?param1=my%20value");

        request = new AgiRequestImpl(lines);

        assertEquals("agi://host/myscript.agi?param1=my%20value", request.getRequestURL(), "incorrect requestURL");
        assertEquals("my value", request.getParameter("param1"), "incorrect value for parameter 'param1'");
    }

    @Test
    void testGetArguments() {
        List<String> lines;
        AgiRequest request;

        lines = new ArrayList<String>();

        lines.add("agi_network_script: myscript.agi");
        lines.add("agi_request: agi://host/myscript.agi");
        lines.add("agi_arg_1: value1");
        lines.add("agi_arg_2: value2");

        request = new AgiRequestImpl(lines);

        assertEquals("agi://host/myscript.agi", request.getRequestURL(), "incorrect requestURL");
        assertEquals(2, request.getArguments().length, "invalid number of arguments");
        assertEquals("value1", request.getArguments()[0], "incorrect value for first argument");
        assertEquals("value2", request.getArguments()[1], "incorrect value for second argument");
    }

    @Test
    void testGetArgumentsWithEmptyArgument() {
        List<String> lines;
        AgiRequest request;

        lines = new ArrayList<String>();

        lines.add("agi_network_script: myscript.agi");
        lines.add("agi_request: agi://host/myscript.agi");
        lines.add("agi_arg_1: value1");
        lines.add("agi_arg_2: ");
        lines.add("agi_arg_3: value3");

        request = new AgiRequestImpl(lines);

        assertEquals("agi://host/myscript.agi", request.getRequestURL(), "incorrect requestURL");
        assertEquals(3, request.getArguments().length, "invalid number of arguments");
        assertEquals("value1", request.getArguments()[0], "incorrect value for first argument");
        assertEquals(null, request.getArguments()[1], "incorrect value for second argument");
        assertEquals("value3", request.getArguments()[2], "incorrect value for third argument");
    }

    @Test
    void testGetArgumentsWithNoArgumentsPassed() {
        List<String> lines;
        AgiRequest request;

        lines = new ArrayList<String>();

        lines.add("agi_network_script: myscript.agi");
        lines.add("agi_request: agi://host/myscript.agi");

        request = new AgiRequestImpl(lines);

        assertEquals("agi://host/myscript.agi", request.getRequestURL(), "incorrect requestURL");
        assertNotNull(request.getArguments(), "getArguments() must never return null");
        assertEquals(0, request.getArguments().length, "invalid number of arguments");
    }
}
