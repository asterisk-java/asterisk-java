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
package org.asteriskjava.util.internal;

import org.asteriskjava.util.SocketConnectionFacade;

import javax.net.SocketFactory;
import javax.net.ssl.SSLSocketFactory;
import java.io.*;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.regex.Pattern;


/**
 * Default implementation of the SocketConnectionFacade interface using java.io.
 *
 * @author srt
 * @version $Id$
 */
public class SocketConnectionFacadeImpl implements SocketConnectionFacade
{
    public static final Pattern CRNL_PATTERN = Pattern.compile("\r\n");
    public static final Pattern NL_PATTERN = Pattern.compile("\n");
    private Socket socket;
    private Scanner scanner;
    private BufferedWriter writer;
    private Trace trace;

    /**
     * Creates a new instance for use with the Manager API that uses CRNL ("\r\n") as line delimiter.
     *
     * @param host        the foreign host to connect to.
     * @param port        the foreign port to connect to.
     * @param ssl         <code>true</code> to use SSL, <code>false</code> otherwise.
     * @param timeout     0 incidcates default
     * @param readTimeout see {@link Socket#setSoTimeout(int)}
     * @throws IOException if the connection cannot be established.
     */
    public SocketConnectionFacadeImpl(String host, int port, boolean ssl, int timeout, int readTimeout) throws IOException
    {
        this(host, port, ssl, timeout, readTimeout, CRNL_PATTERN);
    }

    /**
     * Creates a new instance for use with the Manager API that uses the given line delimiter.
     *
     * @param host        the foreign host to connect to.
     * @param port        the foreign port to connect to.
     * @param ssl         <code>true</code> to use SSL, <code>false</code> otherwise.
     * @param timeout     0 incidcates default
     * @param readTimeout see {@link Socket#setSoTimeout(int)}
     * @param lineDelimiter a {@link Pattern} for matching the line delimiter for the socket
     * @throws IOException if the connection cannot be established.
     */
    public SocketConnectionFacadeImpl(String host, int port, boolean ssl, int timeout, int readTimeout, Pattern lineDelimiter) throws IOException
    {
        Socket socket;

        if (ssl)
        {
            socket = SSLSocketFactory.getDefault().createSocket();
        }
        else
        {
            socket = SocketFactory.getDefault().createSocket();
        }
        socket.setSoTimeout(readTimeout);
        socket.connect(new InetSocketAddress(host, port), timeout);

        initialize(socket, lineDelimiter);
        if (System.getProperty(Trace.TRACE_PROPERTY, "false").equalsIgnoreCase("true"))
        {
            trace = new FileTrace(socket);
        }
    }

    /**
     * Creates a new instance for use with FastAGI that uses NL ("\n") as line delimiter.
     *
     * @param socket the underlying socket.
     * @throws IOException if the connection cannot be initialized.
     */
    SocketConnectionFacadeImpl(Socket socket) throws IOException {
	    socket.setSoTimeout(MAX_SOCKET_READ_TIMEOUT_MILLIS);
	    initialize(socket, NL_PATTERN);
    }

	/** 70 mi = 70 * 60 * 1000 */
	private static final int MAX_SOCKET_READ_TIMEOUT_MILLIS = 4200000;


    private void initialize(Socket socket, Pattern pattern) throws IOException
    {
        this.socket = socket;

        InputStream inputStream = socket.getInputStream();
        OutputStream outputStream = socket.getOutputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        this.scanner = new Scanner(reader);
        this.scanner.useDelimiter(pattern);
        this.writer = new BufferedWriter(new OutputStreamWriter(outputStream));
    }

    public String readLine() throws IOException
    {
        String line;
        try
        {
            line = scanner.next();
        }
        catch (IllegalStateException e)
        {
            if (scanner.ioException() != null)
            {
                throw scanner.ioException();
            }
            else
            {
                // throw new IOException("No more lines available", e); // JDK6
                throw new IOException("No more lines available: " + e.getMessage());
            }
        }
        catch (NoSuchElementException e)
        {
            if (scanner.ioException() != null)
            {
                throw scanner.ioException();
            }
            else
            {
                // throw new IOException("No more lines available", e); // JDK6
                throw new IOException("No more lines available: " + e.getMessage());
            }
        }

        if (trace != null)
        {
            trace.received(line);
        }
        return line;
    }

    public void write(String s) throws IOException
    {
        writer.write(s);
        if (trace != null)
        {
            trace.sent(s);
        }
    }

    public void flush() throws IOException
    {
        writer.flush();
    }

    public void close() throws IOException
    {
        socket.close();
        scanner.close();
        // close the trace only if it was activated (the object is not null)
        if (trace != null){
        	trace.close();
        }
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
