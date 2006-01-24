/*
 *  Copyright  2004-2006 Stefan Reuter
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
package org.asteriskjava.fastagi.impl;

import java.util.List;

import junit.framework.TestCase;

import org.asteriskjava.fastagi.AGIChannel;
import org.asteriskjava.fastagi.AGIReader;
import org.asteriskjava.fastagi.AGIWriter;
import org.asteriskjava.fastagi.InvalidCommandSyntaxException;
import org.asteriskjava.fastagi.InvalidOrUnknownCommandException;
import org.asteriskjava.fastagi.command.NoopCommand;
import org.asteriskjava.fastagi.impl.AGIChannelImpl;
import org.asteriskjava.fastagi.reply.AGIReply;
import org.easymock.MockControl;

public class AGIChannelImplTest extends TestCase
{
    private MockControl agiWriterMC;
    private AGIWriter agiWriter;
    private MockControl agiReaderMC;
    private AGIReader agiReader;
    private AGIChannel agiChannel;

    protected void setUp() throws Exception
    {
        super.setUp();

        this.agiWriterMC = MockControl.createControl(AGIWriter.class);
        this.agiWriter = (AGIWriter) agiWriterMC.getMock();
        this.agiReaderMC = MockControl.createControl(AGIReader.class);
        this.agiReader = (AGIReader) agiReaderMC.getMock();
        this.agiChannel = new AGIChannelImpl(agiWriter, agiReader);
    }

    public void testSendCommand() throws Exception
    {
        SimpleAGIReply reply;
        NoopCommand command;

        reply = new SimpleAGIReply();
        reply.setStatus(AGIReply.SC_SUCCESS);
        reply.setResult("0");

        command = new NoopCommand();

        agiWriter.sendCommand(command);
        agiReader.readReply();
        agiReaderMC.setReturnValue(reply);

        agiWriterMC.replay();
        agiReaderMC.replay();

        assertEquals(reply, agiChannel.sendCommand(command));

        agiWriterMC.verify();
        agiReaderMC.verify();
    }

    public void testSendCommandWithInvalidOrUnknownCommandResponse()
            throws Exception
    {
        SimpleAGIReply reply;
        NoopCommand command;

        reply = new SimpleAGIReply();
        reply.setStatus(AGIReply.SC_INVALID_OR_UNKNOWN_COMMAND);
        reply.setResult("0");

        command = new NoopCommand();

        agiWriter.sendCommand(command);
        agiReader.readReply();
        agiReaderMC.setReturnValue(reply);

        agiWriterMC.replay();
        agiReaderMC.replay();

        try
        {
            agiChannel.sendCommand(command);
            fail("must throw InvalidOrUnknownCommandException");
        }
        catch (InvalidOrUnknownCommandException e)
        {
            assertEquals("Incorrect message",
                    "Invalid or unknown command: NOOP", e.getMessage());
        }

        agiWriterMC.verify();
        agiReaderMC.verify();
    }

    public void testSendCommandWithInvalidCommandSyntaxResponse()
            throws Exception
    {
        SimpleAGIReply reply;
        NoopCommand command;

        reply = new SimpleAGIReply();
        reply.setStatus(AGIReply.SC_INVALID_COMMAND_SYNTAX);
        reply.setSynopsis("NOOP Synopsis");
        reply.setUsage("NOOP Usage");
        reply.setResult("0");

        command = new NoopCommand();

        agiWriter.sendCommand(command);
        agiReader.readReply();
        agiReaderMC.setReturnValue(reply);

        agiWriterMC.replay();
        agiReaderMC.replay();

        try
        {
            agiChannel.sendCommand(command);
            fail("must throw InvalidCommandSyntaxException");
        }
        catch (InvalidCommandSyntaxException e)
        {
            assertEquals("Incorrect message",
                    "Invalid command syntax: NOOP Synopsis", e.getMessage());
            assertEquals("Incorrect sysnopsis", "NOOP Synopsis", e
                    .getSynopsis());
            assertEquals("Incorrect usage", "NOOP Usage", e.getUsage());
        }

        agiWriterMC.verify();
        agiReaderMC.verify();
    }

    public class SimpleAGIReply implements AGIReply
    {
        private int status;
        private String result;
        private String synopsis;
        private String usage;
        
        public String getFirstLine()
        {
            throw new UnsupportedOperationException();
        }

        public void setUsage(String usage)
        {
            this.usage = usage;
        }

        public void setSynopsis(String synopsis)
        {
            this.synopsis = synopsis;
        }

        public void setResult(String result)
        {
            this.result = result;
        }

        public void setStatus(int status)
        {
            this.status = status;
        }

        public List getLines()
        {
            throw new UnsupportedOperationException();
        }

        public int getResultCode()
        {
            throw new UnsupportedOperationException();
        }

        public char getResultCodeAsChar()
        {
            throw new UnsupportedOperationException();
        }

        public String getResult()
        {
            return result;
        }

        public int getStatus()
        {
            return status;
        }

        public String getAttribute(String name)
        {
            throw new UnsupportedOperationException();
        }

        public String getExtra()
        {
            throw new UnsupportedOperationException();
        }

        public String getSynopsis()
        {
            return synopsis;
        }

        public String getUsage()
        {
            return usage;
        }
    }
}
