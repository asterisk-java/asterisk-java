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

import org.asteriskjava.fastagi.AgiHangupException;
import org.asteriskjava.fastagi.AgiReader;
import org.asteriskjava.fastagi.AgiRequest;
import org.asteriskjava.fastagi.reply.AgiReply;
import org.asteriskjava.util.SocketConnectionFacade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.InetAddress;

import static org.easymock.EasyMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class AgiReaderImplTest {
    private AgiReader agiReader;
    private SocketConnectionFacade socket;

    @BeforeEach
    void setUp() {
        this.socket = createMock(SocketConnectionFacade.class);
        this.agiReader = new FastAgiReader(socket);
    }

    @Test
    void testReadRequest() throws Exception {
        AgiRequest request;

        expect(socket.readLine()).andReturn("agi_network: yes");
        expect(socket.readLine()).andReturn("agi_network_script: myscript.agi");
        expect(socket.readLine()).andReturn("agi_request: agi://host/myscript.agi");
        expect(socket.readLine()).andReturn("agi_channel: SIP/1234-d715");
        expect(socket.readLine()).andReturn("");

        byte[] ipLocal = new byte[4];
        ipLocal[0] = Integer.valueOf(192).byteValue();
        ipLocal[1] = Integer.valueOf(168).byteValue();
        ipLocal[2] = Integer.valueOf(0).byteValue();
        ipLocal[3] = Integer.valueOf(1).byteValue();
        expect(socket.getLocalAddress()).andReturn(InetAddress.getByAddress(ipLocal));
        expect(socket.getLocalPort()).andReturn(1234);

        byte[] ipRemote = new byte[4];
        ipRemote[0] = Integer.valueOf(192).byteValue();
        ipRemote[1] = Integer.valueOf(168).byteValue();
        ipRemote[2] = Integer.valueOf(0).byteValue();
        ipRemote[3] = Integer.valueOf(2).byteValue();
        expect(socket.getRemoteAddress()).andReturn(InetAddress.getByAddress(ipRemote));
        expect(socket.getRemotePort()).andReturn(1235);

        replay(socket);

        request = agiReader.readRequest();

        assertEquals("myscript.agi", request.getScript(), "incorrect script");
        assertEquals("agi://host/myscript.agi", request.getRequestURL(), "incorrect requestURL");
        assertEquals("SIP/1234-d715", request.getChannel(), "incorrect channel");
        assertEquals(ipLocal[0], request.getLocalAddress().getAddress()[0], "incorrect local address");
        assertEquals(ipLocal[1], request.getLocalAddress().getAddress()[1], "incorrect local address");
        assertEquals(ipLocal[2], request.getLocalAddress().getAddress()[2], "incorrect local address");
        assertEquals(ipLocal[3], request.getLocalAddress().getAddress()[3], "incorrect local address");
        assertEquals(1234, request.getLocalPort(), "incorrect local port");
        assertEquals(ipRemote[0], request.getRemoteAddress().getAddress()[0], "incorrect remote address");
        assertEquals(ipRemote[1], request.getRemoteAddress().getAddress()[1], "incorrect remote address");
        assertEquals(ipRemote[2], request.getRemoteAddress().getAddress()[2], "incorrect remote address");
        assertEquals(ipRemote[3], request.getRemoteAddress().getAddress()[3], "incorrect remote address");
        assertEquals(1235, request.getRemotePort(), "incorrect remote port");

        verify(socket);
    }

    @Test
    void testReadReply() throws Exception {
        AgiReply reply;

        expect(socket.readLine()).andReturn("200 result=49 endpos=2240");

        replay(socket);

        reply = agiReader.readReply();

        assertEquals(AgiReply.SC_SUCCESS, reply.getStatus(), "Incorrect status");
        assertEquals(49, reply.getResultCode(), "Incorrect result");

        verify(socket);
    }

    @Test
    void testReadReplyInvalidOrUnknownCommand() throws Exception {
        AgiReply reply;

        expect(socket.readLine()).andReturn("510 Invalid or unknown command");

        replay(socket);

        reply = agiReader.readReply();

        assertEquals(AgiReply.SC_INVALID_OR_UNKNOWN_COMMAND, reply.getStatus(), "Incorrect status");

        verify(socket);
    }

    @Test
    void testReadReplyInvalidCommandSyntax() throws Exception {
        AgiReply reply;

        expect(socket.readLine()).andReturn("520-Invalid command syntax.  Proper usage follows:");
        expect(socket.readLine()).andReturn(" Usage: DATABASE DEL <family> <key>");
        expect(socket.readLine()).andReturn("        Deletes an entry in the Asterisk database for a");
        expect(socket.readLine()).andReturn(" given family and key.");
        expect(socket.readLine()).andReturn(" Returns 1 if succesful, 0 otherwise");
        expect(socket.readLine()).andReturn("520 End of proper usage.");

        replay(socket);

        reply = agiReader.readReply();

        assertEquals(AgiReply.SC_INVALID_COMMAND_SYNTAX, reply.getStatus(), "Incorrect status");
        assertEquals("DATABASE DEL <family> <key>", reply.getSynopsis(), "Incorrect synopsis");

        verify(socket);
    }

    @Test
    void testReadReplyWhenHungUp() throws Exception {
        expect(socket.readLine()).andReturn(null);

        replay(socket);

        try {
            agiReader.readReply();
            fail("Must throw AgiHangupException");
        } catch (AgiHangupException e) {
        }

        verify(socket);
    }
}
