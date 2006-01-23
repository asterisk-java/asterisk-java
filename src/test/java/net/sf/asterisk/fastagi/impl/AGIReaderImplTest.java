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
package net.sf.asterisk.fastagi.impl;

import java.net.InetAddress;

import junit.framework.TestCase;
import net.sf.asterisk.fastagi.AGIHangupException;
import net.sf.asterisk.fastagi.AGIReader;
import net.sf.asterisk.fastagi.AGIRequest;
import net.sf.asterisk.fastagi.reply.AGIReply;
import net.sf.asterisk.io.SocketConnectionFacade;

import org.easymock.MockControl;

public class AGIReaderImplTest extends TestCase
{
    private AGIReader agiReader;
    private MockControl socketMC;
    private SocketConnectionFacade socket;

    protected void setUp() throws Exception
    {
        super.setUp();
        this.socketMC = MockControl.createControl(SocketConnectionFacade.class);
        this.socket = (SocketConnectionFacade) socketMC.getMock();
        this.agiReader = new AGIReaderImpl(socket);
    }

    public void testReadRequest() throws Exception
    {
        AGIRequest request;

        socket.readLine();
        socketMC.setReturnValue("agi_network: yes");
        socket.readLine();
        socketMC.setReturnValue("agi_network_script: myscript.agi");
        socket.readLine();
        socketMC.setReturnValue("agi_request: agi://host/myscript.agi");
        socket.readLine();
        socketMC.setReturnValue("agi_channel: SIP/1234-d715");
        socket.readLine();
        socketMC.setReturnValue("");

        byte[] ipLocal = new byte[4];
        ipLocal[0] = new Integer(192).byteValue();
        ipLocal[1] = new Integer(168).byteValue();
        ipLocal[2] = new Integer(0).byteValue();
        ipLocal[3] = new Integer(1).byteValue();
        socket.getLocalAddress();
        socketMC.setReturnValue(InetAddress.getByAddress(ipLocal));
        socket.getLocalPort();
        socketMC.setReturnValue(1234);

        byte[] ipRemote = new byte[4];
        ipRemote[0] = new Integer(192).byteValue();
        ipRemote[1] = new Integer(168).byteValue();
        ipRemote[2] = new Integer(0).byteValue();
        ipRemote[3] = new Integer(2).byteValue();
        socket.getRemoteAddress();
        socketMC.setReturnValue(InetAddress.getByAddress(ipRemote));
        socket.getRemotePort();
        socketMC.setReturnValue(1235);

        socketMC.replay();

        request = agiReader.readRequest();

        assertEquals("incorrect script", "myscript.agi", request.getScript());
        assertEquals("incorrect requestURL", "agi://host/myscript.agi", request
                .getRequestURL());
        assertEquals("incorrect channel", "SIP/1234-d715", request.getChannel());
        assertEquals("incorrect local address", ipLocal[0], request.getLocalAddress().getAddress()[0]);
        assertEquals("incorrect local address", ipLocal[1], request.getLocalAddress().getAddress()[1]);
        assertEquals("incorrect local address", ipLocal[2], request.getLocalAddress().getAddress()[2]);
        assertEquals("incorrect local address", ipLocal[3], request.getLocalAddress().getAddress()[3]);
        assertEquals("incorrect local port", 1234, request.getLocalPort());
        assertEquals("incorrect remote address", ipRemote[0], request.getRemoteAddress().getAddress()[0]);
        assertEquals("incorrect remote address", ipRemote[1], request.getRemoteAddress().getAddress()[1]);
        assertEquals("incorrect remote address", ipRemote[2], request.getRemoteAddress().getAddress()[2]);
        assertEquals("incorrect remote address", ipRemote[3], request.getRemoteAddress().getAddress()[3]);
        assertEquals("incorrect remote port", 1235, request.getRemotePort());

        socketMC.verify();
    }

    public void testReadReply() throws Exception
    {
        AGIReply reply;

        socket.readLine();
        socketMC.setReturnValue("200 result=49 endpos=2240");

        socketMC.replay();

        reply = agiReader.readReply();

        assertEquals("Incorrect status", AGIReply.SC_SUCCESS, reply.getStatus());
        assertEquals("Incorrect result", 49, reply.getResultCode());

        socketMC.verify();
    }

    public void testReadReplyInvalidOrUnknownCommand() throws Exception
    {
        AGIReply reply;

        socket.readLine();
        socketMC.setReturnValue("510 Invalid or unknown command");

        socketMC.replay();

        reply = agiReader.readReply();

        assertEquals("Incorrect status",
                AGIReply.SC_INVALID_OR_UNKNOWN_COMMAND, reply.getStatus());

        socketMC.verify();
    }

    public void testReadReplyInvalidCommandSyntax() throws Exception
    {
        AGIReply reply;

        socket.readLine();
        socketMC
                .setReturnValue("520-Invalid command syntax.  Proper usage follows:");
        socket.readLine();
        socketMC.setReturnValue(" Usage: DATABASE DEL <family> <key>");
        socket.readLine();
        socketMC
                .setReturnValue("        Deletes an entry in the Asterisk database for a");
        socket.readLine();
        socketMC.setReturnValue(" given family and key.");
        socket.readLine();
        socketMC.setReturnValue(" Returns 1 if succesful, 0 otherwise");
        socket.readLine();
        socketMC.setReturnValue("520 End of proper usage.");

        socketMC.replay();

        reply = agiReader.readReply();

        assertEquals("Incorrect status", AGIReply.SC_INVALID_COMMAND_SYNTAX,
                reply.getStatus());
        assertEquals("Incorrect synopsis", "DATABASE DEL <family> <key>", reply
                .getSynopsis());

        socketMC.verify();
    }

    public void testReadReplyWhenHungUp() throws Exception
    {
        socket.readLine();
        socketMC.setReturnValue(null);

        socketMC.replay();

        try
        {
            agiReader.readReply();
            fail("Must throw AGIHangupException");
        }
        catch (AGIHangupException e)
        {
        }

        socketMC.verify();
    }

}
