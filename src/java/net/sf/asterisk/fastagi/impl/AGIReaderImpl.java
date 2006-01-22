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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.sf.asterisk.fastagi.AGIException;
import net.sf.asterisk.fastagi.AGIHangupException;
import net.sf.asterisk.fastagi.AGINetworkException;
import net.sf.asterisk.fastagi.AGIReader;
import net.sf.asterisk.fastagi.AGIRequest;
import net.sf.asterisk.fastagi.reply.AGIReply;
import net.sf.asterisk.fastagi.reply.impl.AGIReplyImpl;
import net.sf.asterisk.io.SocketConnectionFacade;

/**
 * Default implementation of the AGIReader implementation.
 * 
 * @author srt
 * @version $Id: AGIReaderImpl.java,v 1.3 2005/09/27 21:07:26 srt Exp $
 */
public class AGIReaderImpl implements AGIReader
{
    private SocketConnectionFacade socket;

    public AGIReaderImpl(SocketConnectionFacade socket)
    {
        this.socket = socket;
    }

    public AGIRequest readRequest() throws AGIException
    {
        AGIRequestImpl request;
        String line;
        List lines;

        lines = new ArrayList();

        try
        {
            while ((line = socket.readLine()) != null)
            {
                if (line.length() == 0)
                {
                    break;
                }

                lines.add(line);
            }
        }
        catch (IOException e)
        {
            throw new AGINetworkException(
                    "Unable to read request from Asterisk: " + e.getMessage(),
                    e);
        }

        request = new AGIRequestImpl(lines);
        request.setLocalAddress(socket.getLocalAddress());
        request.setLocalPort(socket.getLocalPort());
        request.setRemoteAddress(socket.getRemoteAddress());
        request.setRemotePort(socket.getRemotePort());

        return request;
    }

    public AGIReply readReply() throws AGIException
    {
        AGIReply reply;
        List lines;
        String line;

        lines = new ArrayList();

        try
        {
            line = socket.readLine();
        }
        catch (IOException e)
        {
            throw new AGINetworkException(
                    "Unable to read reply from Asterisk: " + e.getMessage(), e);
        }

        if (line == null)
        {
            throw new AGIHangupException();
        }

        lines.add(line);

        // read synopsis and usage if statuscode is 520
        if (line.startsWith(Integer
                .toString(AGIReply.SC_INVALID_COMMAND_SYNTAX)))
        {
            try
            {
                while ((line = socket.readLine()) != null)
                {
                    lines.add(line);
                    if (line.startsWith(Integer
                            .toString(AGIReply.SC_INVALID_COMMAND_SYNTAX)))
                    {
                        break;
                    }
                }
            }
            catch (IOException e)
            {
                throw new AGINetworkException(
                        "Unable to read reply from Asterisk: " + e.getMessage(),
                        e);
            }
        }

        reply = new AGIReplyImpl(lines);

        return reply;
    }
}
