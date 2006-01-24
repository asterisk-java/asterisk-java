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
package org.asteriskjava.io.impl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

import org.asteriskjava.io.SocketConnectionFacade;


/**
 * Default implementation of the SocketConnectionFacade interface using java.io.
 * 
 * @author srt
 * @version $Id: SocketConnectionFacadeImpl.java,v 1.1 2005/10/25 22:43:25 srt Exp $
 */
public class SocketConnectionFacadeImpl implements SocketConnectionFacade
{
    private final Socket socket;
    private final BufferedReader reader;
    private final BufferedWriter writer;

    public SocketConnectionFacadeImpl(String host, int port) throws IOException
    {
    	this(host, port, 0);
    }    
    
    public SocketConnectionFacadeImpl(String host, int port, int timeout) throws IOException
    {
    	this.socket = new Socket();
    	this.socket.connect(new InetSocketAddress(host, port), timeout);

        InputStream inputStream = socket.getInputStream();
        OutputStream outputStream = socket.getOutputStream();

        this.reader = new BufferedReader(new InputStreamReader(inputStream));
        this.writer = new BufferedWriter(new OutputStreamWriter(outputStream));
    }

    SocketConnectionFacadeImpl(Socket socket) throws IOException
    {
        this.socket = socket;

        InputStream inputStream = socket.getInputStream();
        OutputStream outputStream = socket.getOutputStream();

        this.reader = new BufferedReader(new InputStreamReader(inputStream));
        this.writer = new BufferedWriter(new OutputStreamWriter(outputStream));
    }

    public String readLine() throws IOException
    {
        return reader.readLine();
    }

    public void write(String s) throws IOException
    {
        writer.write(s);
    }

    public void flush() throws IOException
    {
        writer.flush();
    }

    public void close() throws IOException
    {
        this.socket.close();
    }

    public boolean isConnected()
    {
        return socket.isConnected();
    }

    public InetAddress getLocalAddress()
    {
        return socket.getLocalAddress();
    }

    public int getLocalPort()
    {
        return socket.getLocalPort();
    }

    public InetAddress getRemoteAddress()
    {
        return socket.getInetAddress();
    }

    public int getRemotePort()
    {
        return socket.getPort();
    }
}
