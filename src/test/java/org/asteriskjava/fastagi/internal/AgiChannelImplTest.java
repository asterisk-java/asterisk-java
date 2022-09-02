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

import org.asteriskjava.fastagi.*;
import org.asteriskjava.fastagi.command.NoopCommand;
import org.asteriskjava.fastagi.reply.AgiReply;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.easymock.EasyMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class AgiChannelImplTest {
    private AgiWriter agiWriter;
    private AgiReader agiReader;
    private AgiChannel agiChannel;

    @BeforeEach
    void setUp() {
        this.agiWriter = createMock(AgiWriter.class);
        this.agiReader = createMock(AgiReader.class);
        this.agiChannel = new AgiChannelImpl(null, agiWriter, agiReader);
    }

    @Test
    void testSendCommand() throws Exception {
        SimpleAgiReply reply;
        NoopCommand command;

        reply = new SimpleAgiReply();
        reply.setStatus(AgiReply.SC_SUCCESS);
        reply.setResult("0");

        command = new NoopCommand();

        agiWriter.sendCommand(command);
        expect(agiReader.readReply()).andReturn(reply);

        replay(agiWriter);
        replay(agiReader);

        assertEquals(reply, agiChannel.sendCommand(command));

        verify(agiWriter);
        verify(agiReader);
    }

    @Test
    void testSendCommandWithInvalidOrUnknownCommandResponse() throws Exception {
        SimpleAgiReply reply;
        NoopCommand command;

        reply = new SimpleAgiReply();
        reply.setStatus(AgiReply.SC_INVALID_OR_UNKNOWN_COMMAND);
        reply.setResult("0");

        command = new NoopCommand();

        agiWriter.sendCommand(command);
        expect(agiReader.readReply()).andReturn(reply);

        replay(agiWriter);
        replay(agiReader);

        try {
            agiChannel.sendCommand(command);
            fail("must throw InvalidOrUnknownCommandException");
        } catch (InvalidOrUnknownCommandException e) {
            assertEquals("Invalid or unknown command: NOOP", e.getMessage(), "Incorrect message");
        }

        verify(agiWriter);
        verify(agiReader);
    }

    @Test
    void testSendCommandWithInvalidCommandSyntaxResponse() throws Exception {
        SimpleAgiReply reply;
        NoopCommand command;

        reply = new SimpleAgiReply();
        reply.setStatus(AgiReply.SC_INVALID_COMMAND_SYNTAX);
        reply.setSynopsis("NOOP Synopsis");
        reply.setUsage("NOOP Usage");
        reply.setResult("0");

        command = new NoopCommand();

        agiWriter.sendCommand(command);
        expect(agiReader.readReply()).andReturn(reply);

        replay(agiWriter);
        replay(agiReader);

        try {
            agiChannel.sendCommand(command);
            fail("must throw InvalidCommandSyntaxException");
        } catch (InvalidCommandSyntaxException e) {
            assertEquals("Invalid command syntax: NOOP Synopsis", e.getMessage(), "Incorrect message");
            assertEquals("NOOP Synopsis", e.getSynopsis(), "Incorrect sysnopsis");
            assertEquals("NOOP Usage", e.getUsage(), "Incorrect usage");
        }

        verify(agiWriter);
        verify(agiReader);
    }

    public static class SimpleAgiReply implements AgiReply {
        private static final long serialVersionUID = 1L;
        private int status;
        private String result;
        private String synopsis;
        private String usage;

        public String getFirstLine() {
            throw new UnsupportedOperationException();
        }

        public void setUsage(String usage) {
            this.usage = usage;
        }

        public void setSynopsis(String synopsis) {
            this.synopsis = synopsis;
        }

        public void setResult(String result) {
            this.result = result;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public List<String> getLines() {
            throw new UnsupportedOperationException();
        }

        public int getResultCode() {
            throw new UnsupportedOperationException();
        }

        public char getResultCodeAsChar() {
            throw new UnsupportedOperationException();
        }

        public String getResult() {
            return result;
        }

        public int getStatus() {
            return status;
        }

        public String getAttribute(String name) {
            throw new UnsupportedOperationException();
        }

        public String getExtra() {
            throw new UnsupportedOperationException();
        }

        public String getSynopsis() {
            return synopsis;
        }

        public String getUsage() {
            return usage;
        }
    }
}
