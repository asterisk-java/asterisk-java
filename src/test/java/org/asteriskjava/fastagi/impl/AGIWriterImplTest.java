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

import junit.framework.TestCase;

import org.asteriskjava.fastagi.AGIWriter;
import org.asteriskjava.fastagi.command.StreamFileCommand;
import org.asteriskjava.fastagi.impl.AGIWriterImpl;
import org.asteriskjava.io.SocketConnectionFacade;
import org.easymock.MockControl;

public class AGIWriterImplTest extends TestCase
{
    private AGIWriter agiWriter;
    private MockControl socketMC;
    private SocketConnectionFacade socket;

    protected void setUp() throws Exception
    {
        super.setUp();
        this.socketMC = MockControl.createControl(SocketConnectionFacade.class);
        this.socket = (SocketConnectionFacade) socketMC.getMock();
        this.agiWriter = new AGIWriterImpl(socket);
    }

    public void testSendCommand() throws Exception
    {
        StreamFileCommand command;
        
        command = new StreamFileCommand("welcome");
        
        socket.write("STREAM FILE \"welcome\" \"\"\n");
        socket.flush();
        
        socketMC.replay();
        
        agiWriter.sendCommand(command);
        
        socketMC.verify();
    }
}
