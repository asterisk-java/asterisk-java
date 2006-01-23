/*
 * Copyright  2004-2005 Stefan Reuter
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
package net.sf.asterisk.fastagi.reply.impl;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;
import net.sf.asterisk.fastagi.reply.impl.AGIReplyImpl;

public class AGIReplyImplTest extends TestCase
{
    private List lines;

    protected void setUp()
    {
        this.lines = new ArrayList();
    }

    public void testBuildReply()
    {
        AGIReplyImpl reply;

        lines.add("200 result=49");

        reply = new AGIReplyImpl(lines);

        assertEquals("Incorrect status", AGIReplyImpl.SC_SUCCESS, reply.getStatus());
        assertEquals("Incorrect result", 49, reply.getResultCode());
        assertEquals("Incorrect result as character", '1', reply
                .getResultCodeAsChar());
        assertEquals("Incorrect result when get via getAttribute()", "49",
                reply.getAttribute("result"));
    }

    public void testBuildReplyWithAdditionalAttribute()
    {
        AGIReplyImpl reply;

        lines.add("200 result=49 endpos=2240");

        reply = new AGIReplyImpl(lines);

        assertEquals("Incorrect status", AGIReplyImpl.SC_SUCCESS, reply.getStatus());
        assertEquals("Incorrect result", 49, reply.getResultCode());
        assertEquals("Incorrect result as character", '1', reply
                .getResultCodeAsChar());
        assertEquals("Incorrect result when get via getAttribute()", "49",
                reply.getAttribute("result"));
        assertEquals("Incorrect endpos attribute", "2240", reply
                .getAttribute("endpos"));
    }

    public void testBuildReplyWithMultipleAdditionalAttribute()
    {
        AGIReplyImpl reply;

        lines.add("200 result=49 startpos=1234 endpos=2240");

        reply = new AGIReplyImpl(lines);

        assertEquals("Incorrect status", AGIReplyImpl.SC_SUCCESS, reply.getStatus());
        assertEquals("Incorrect result", 49, reply.getResultCode());
        assertEquals("Incorrect result as character", '1', reply
                .getResultCodeAsChar());
        assertEquals("Incorrect result when get via getAttribute()", "49",
                reply.getAttribute("result"));
        assertEquals("Incorrect startpos attribute", "1234", reply
                .getAttribute("startpos"));
        assertEquals("Incorrect endpos attribute", "2240", reply
                .getAttribute("endpos"));
    }

    public void testBuildReplyWithParenthesis()
    {
        AGIReplyImpl reply;

        lines.add("200 result=1 ((hello)(world))");

        reply = new AGIReplyImpl(lines);

        assertEquals("Incorrect status", AGIReplyImpl.SC_SUCCESS, reply.getStatus());
        assertEquals("Incorrect result", 1, reply.getResultCode());
        assertEquals("Incorrect parenthesis", "(hello)(world)", reply
                .getExtra());
    }

    public void testBuildReplyWithAdditionalAttributeAndParenthesis()
    {
        AGIReplyImpl reply;

        lines.add("200 result=1 ((hello)(world)) endpos=2240");

        reply = new AGIReplyImpl(lines);

        assertEquals("Incorrect status", AGIReplyImpl.SC_SUCCESS, reply.getStatus());
        assertEquals("Incorrect result", 1, reply.getResultCode());
        assertEquals("Incorrect parenthesis", "(hello)(world)", reply
                .getExtra());
        assertEquals("Incorrect endpos attribute", "2240", reply
                .getAttribute("endpos"));
    }

    public void testBuildReplyInvalidOrUnknownCommand()
    {
        AGIReplyImpl reply;

        lines.add("510 Invalid or unknown command");

        reply = new AGIReplyImpl(lines);

        assertEquals("Incorrect status",
                AGIReplyImpl.SC_INVALID_OR_UNKNOWN_COMMAND, reply.getStatus());
    }

    public void testBuildReplyInvalidCommandSyntax()
    {
        AGIReplyImpl reply;

        lines.add("520-Invalid command syntax.  Proper usage follows:");
        lines.add(" Usage: DATABASE DEL <family> <key>");
        lines.add("        Deletes an entry in the Asterisk database for a");
        lines.add(" given family and key.");
        lines.add(" Returns 1 if succesful, 0 otherwise");
        lines.add("520 End of proper usage.");

        reply = new AGIReplyImpl(lines);

        assertEquals("Incorrect status", AGIReplyImpl.SC_INVALID_COMMAND_SYNTAX,
                reply.getStatus());
        assertEquals("Incorrect synopsis", "DATABASE DEL <family> <key>", reply
                .getSynopsis());
        assertEquals(
                "Incorrect usage",
                "Deletes an entry in the Asterisk database for a given family and key. Returns 1 if succesful, 0 otherwise",
                reply.getUsage());
    }

    public void testBuildReplyWithLeadingSpace()
    {
        AGIReplyImpl reply;

        lines.add("200 result= (timeout)");

        reply = new AGIReplyImpl(lines);

        assertEquals("Incorrect status", AGIReplyImpl.SC_SUCCESS, reply.getStatus());
        assertEquals("Incorrect extra", "timeout", reply.getExtra());
    }
}
