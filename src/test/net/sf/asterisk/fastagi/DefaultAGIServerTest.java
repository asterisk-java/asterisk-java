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
package net.sf.asterisk.fastagi;

import java.io.IOException;

import org.easymock.MockControl;

import net.sf.asterisk.io.ServerSocketFacade;
import net.sf.asterisk.io.SocketConnectionFacade;
import junit.framework.TestCase;

public class DefaultAGIServerTest extends TestCase
{
    private DefaultAGIServer server;
    private MockedServerSocketFacade serverSocket;
    private MockControl socketMC;
    private SocketConnectionFacade socket;

    protected void setUp() throws Exception
    {
        super.setUp();

        serverSocket = new MockedServerSocketFacade();
        server = new MockedDefaultAGIServer();
    }

    protected void tearDown() throws Exception
    {
        super.tearDown();
        server.shutdown();
    }

    public void testDummy()
    {
        
    }
    
    public void XtestStartup() throws Exception
    {
        socketMC = MockControl.createControl(SocketConnectionFacade.class);
        socket = (SocketConnectionFacade) socketMC.getMock();

        socket.readLine();
        socketMC.setReturnValue(null);
        socket.getLocalAddress();
        socketMC.setReturnValue(null);
        socket.getLocalPort();
        socketMC.setReturnValue(1);
        socket.getRemoteAddress();
        socketMC.setReturnValue(null);
        socket.getRemotePort();
        socketMC.setReturnValue(2);
        socket.write("VERBOSE \"No script configured for null\" 1\n");
        socket.flush();
        socket.readLine();
        socketMC.setReturnValue(null);
        socket.close();
        socketMC.replay();

        try
        {
            server.startup();
        }
        catch (IOException e)
        {
            
        }
        Thread.sleep(500);

        assertEquals("serverSocket.accept() not called 2 times", 2,
                serverSocket.acceptCalls);
        assertEquals("serverSocket.close() not called", 1,
                serverSocket.closeCalls);
        
        socketMC.verify();
    }

    class MockedDefaultAGIServer extends DefaultAGIServer
    {
        protected ServerSocketFacade createServerSocket()
        {
            return serverSocket;
        }
    }

    class MockedServerSocketFacade implements ServerSocketFacade
    {
        public int acceptCalls = 0;
        public int closeCalls = 0;

        public SocketConnectionFacade accept() throws IOException
        {
            acceptCalls++;
            try
            {
                Thread.sleep(100);
            }
            catch (InterruptedException e)
            {
            }

            if (acceptCalls == 1)
            {
                return socket;
            }
            else
            {
                throw new IOException("Provoked IOException");
            }
        }

        public void close() throws IOException
        {
            closeCalls++;
        }
    }
}
