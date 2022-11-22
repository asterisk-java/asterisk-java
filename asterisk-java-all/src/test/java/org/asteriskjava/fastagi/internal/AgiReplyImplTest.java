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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class AgiReplyImplTest {
    private List<String> lines;

    @BeforeEach
    void setUp() {
        this.lines = new ArrayList<>();
    }

    @Test
    void testBuildReply() {
        AgiReplyImpl reply;

        lines.add("200 result=49");

        reply = new AgiReplyImpl(lines);

        assertEquals(AgiReplyImpl.SC_SUCCESS, reply.getStatus(), "Incorrect status");
        assertEquals(49, reply.getResultCode(), "Incorrect result");
        assertEquals('1', reply.getResultCodeAsChar(), "Incorrect result as character");
        assertEquals("49", reply.getAttribute("result"), "Incorrect result when get via getAttribute()");
    }

    @Test
    void testBuildReplyWithAdditionalAttribute() {
        AgiReplyImpl reply;

        lines.add("200 result=49 endpos=2240");

        reply = new AgiReplyImpl(lines);

        assertEquals(AgiReplyImpl.SC_SUCCESS, reply.getStatus(), "Incorrect status");
        assertEquals(49, reply.getResultCode(), "Incorrect result");
        assertEquals('1', reply.getResultCodeAsChar(), "Incorrect result as character");
        assertEquals("49", reply.getAttribute("result"), "Incorrect result when get via getAttribute()");
        assertEquals("2240", reply.getAttribute("endpos"), "Incorrect endpos attribute");
    }

    @Test
    void testBuildReplyWithMultipleAdditionalAttribute() {
        AgiReplyImpl reply;

        lines.add("200 result=49 startpos=1234 endpos=2240");

        reply = new AgiReplyImpl(lines);

        assertEquals(AgiReplyImpl.SC_SUCCESS, reply.getStatus(), "Incorrect status");
        assertEquals(49, reply.getResultCode(), "Incorrect result");
        assertEquals('1', reply.getResultCodeAsChar(), "Incorrect result as character");
        assertEquals("49", reply.getAttribute("result"), "Incorrect result when get via getAttribute()");
        assertEquals("1234", reply.getAttribute("startpos"), "Incorrect startpos attribute");
        assertEquals("2240", reply.getAttribute("endpos"), "Incorrect endpos attribute");
    }

    @Test
    void testBuildReplyWithQuotedAttribute() {
        AgiReplyImpl reply;

        lines.add("200 result=1 (speech) endpos=0 results=1 score0=969 text0=\"123456789\" grammar0=digits");

        reply = new AgiReplyImpl(lines);

        assertEquals(AgiReplyImpl.SC_SUCCESS, reply.getStatus(), "Incorrect status");
        assertEquals(1, reply.getResultCode(), "Incorrect result");
        assertEquals("1", reply.getAttribute("result"), "Incorrect result when get via getAttribute()");
        assertEquals("0", reply.getAttribute("endpos"), "Incorrect endpos attribute");
        assertEquals("speech", reply.getExtra(), "Incorrect extra");
        assertEquals("123456789", reply.getAttribute("text0"), "Incorrect text0 attribute");
    }

    @Test
    void testBuildReplyWithQuotedAttribute2() {
        AgiReplyImpl reply;

        lines.add("200 result=1 (speech) endpos=0 results=1 score0=969 text0=\"hi \\\"joe!\\\"\" grammar0=digits");

        reply = new AgiReplyImpl(lines);

        assertEquals(AgiReplyImpl.SC_SUCCESS, reply.getStatus(), "Incorrect status");
        assertEquals(1, reply.getResultCode(), "Incorrect result");
        assertEquals("1", reply.getAttribute("result"), "Incorrect result when get via getAttribute()");
        assertEquals("0", reply.getAttribute("endpos"), "Incorrect endpos attribute");
        assertEquals("speech", reply.getExtra(), "Incorrect extra");
        assertEquals("hi \"joe!\"", reply.getAttribute("text0"), "Incorrect text0 attribute");
    }

    void testBla() {
        System.out.println(005);
    }

    @Test
    void testBuildReplyWithParenthesis() {
        AgiReplyImpl reply;

        lines.add("200 result=1 ((hello)(world))");

        reply = new AgiReplyImpl(lines);

        assertEquals(AgiReplyImpl.SC_SUCCESS, reply.getStatus(), "Incorrect status");
        assertEquals(1, reply.getResultCode(), "Incorrect result");
        assertEquals("(hello)(world)", reply.getExtra(), "Incorrect parenthesis");
    }

    @Test
    void testBuildReplyWithAdditionalAttributeAndParenthesis() {
        AgiReplyImpl reply;

        lines.add("200 result=1 ((hello)(world)) endpos=2240");

        reply = new AgiReplyImpl(lines);

        assertEquals(AgiReplyImpl.SC_SUCCESS, reply.getStatus(), "Incorrect status");
        assertEquals(1, reply.getResultCode(), "Incorrect result");
        assertEquals("(hello)(world)", reply.getExtra(), "Incorrect parenthesis");
        assertEquals("2240", reply.getAttribute("endpos"), "Incorrect endpos attribute");
    }

    @Test
    void testBuildReplyInvalidOrUnknownCommand() {
        AgiReplyImpl reply;

        lines.add("510 Invalid or unknown command");

        reply = new AgiReplyImpl(lines);

        assertEquals(AgiReplyImpl.SC_INVALID_OR_UNKNOWN_COMMAND, reply.getStatus(), "Incorrect status");
    }

    @Test
    void testBuildReplyInvalidCommandSyntax() {
        AgiReplyImpl reply;

        lines.add("520-Invalid command syntax.  Proper usage follows:");
        lines.add(" Usage: DATABASE DEL <family> <key>");
        lines.add("        Deletes an entry in the Asterisk database for a");
        lines.add(" given family and key.");
        lines.add(" Returns 1 if succesful, 0 otherwise");
        lines.add("520 End of proper usage.");

        reply = new AgiReplyImpl(lines);

        assertEquals(AgiReplyImpl.SC_INVALID_COMMAND_SYNTAX, reply.getStatus(), "Incorrect status");
        assertEquals("DATABASE DEL <family> <key>", reply.getSynopsis(), "Incorrect synopsis");
        assertEquals("Deletes an entry in the Asterisk database for a given family and key. Returns 1 if succesful, 0 otherwise", reply.getUsage(), "Incorrect usage");
    }

    @Test
    void testBuildReplyInvalidCommandSyntaxWithOnlyUsage() {
        AgiReplyImpl reply;

        lines.add("520-Invalid command syntax.  Proper usage follows:");
        lines.add(" Usage: DATABASE DEL <family> <key>");
        lines.add("        Deletes an entry in the Asterisk database for a");
        lines.add(" given family and key.");
        lines.add(" Returns 1 if succesful, 0 otherwise");
        lines.add("520 End of proper usage.");

        reply = new AgiReplyImpl(lines);

        assertEquals(AgiReplyImpl.SC_INVALID_COMMAND_SYNTAX, reply.getStatus(), "Incorrect status");
        // due to the lazy initialization in use this getUsage() could fail if
        // we don't call it before getSynopsis()
        assertEquals("Deletes an entry in the Asterisk database for a given family and key. Returns 1 if succesful, 0 otherwise", reply.getUsage(), "Incorrect usage");
        assertEquals("DATABASE DEL <family> <key>", reply.getSynopsis(), "Incorrect synopsis");
    }

    @Test
    void testBuildReplyWithLeadingSpace() {
        AgiReplyImpl reply;

        lines.add("200 result= (timeout)");

        reply = new AgiReplyImpl(lines);

        assertEquals(AgiReplyImpl.SC_SUCCESS, reply.getStatus(), "Incorrect status");
        assertEquals("timeout", reply.getExtra(), "Incorrect extra");
    }

    @Test
    void testBuildReplyWithEmptyResultAndTimeout() {
        AgiReplyImpl reply;

        lines.add("200 result= (timeout)");

        reply = new AgiReplyImpl(lines);

        assertFalse(reply.getResult().equals("timeout"), "Incorrect result");
        assertEquals("", reply.getResult(), "Incorrect result");
        assertEquals("timeout", reply.getExtra(), "Incorrect extra");

    }
}
