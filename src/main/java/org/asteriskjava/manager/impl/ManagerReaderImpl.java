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
package org.asteriskjava.manager.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.asteriskjava.io.SocketConnectionFacade;
import org.asteriskjava.manager.AsteriskServer;
import org.asteriskjava.manager.Dispatcher;
import org.asteriskjava.manager.EventBuilder;
import org.asteriskjava.manager.ManagerReader;
import org.asteriskjava.manager.ResponseBuilder;
import org.asteriskjava.manager.event.ConnectEvent;
import org.asteriskjava.manager.event.DisconnectEvent;
import org.asteriskjava.manager.event.ManagerEvent;
import org.asteriskjava.manager.response.CommandResponse;
import org.asteriskjava.manager.response.ManagerResponse;
import org.asteriskjava.util.DateUtil;
import org.asteriskjava.util.Log;
import org.asteriskjava.util.LogFactory;



/**
 * Default implementation of the ManagerReader interface.
 * 
 * @author srt
 * @version $Id: ManagerReaderImpl.java,v 1.9 2005/09/13 19:59:58 srt Exp $
 */
public class ManagerReaderImpl implements ManagerReader
{
    /**
     * Instance logger.
     */
    private final Log logger = LogFactory.getLog(getClass());

    /**
     * The event builder utility to convert a map of attributes reveived from asterisk to instances
     * of registered event classes.
     */
    private final EventBuilder eventBuilder;

    /**
     * The response builder utility to convert a map of attributes reveived from asterisk to
     * instances of well known response classes.
     */
    private final ResponseBuilder responseBuilder;

    /**
     * The dispatcher to use for dispatching events and responses.
     */
    private final Dispatcher dispatcher;

    /**
     * The asterisk server we are reading from.
     */
    private final AsteriskServer asteriskServer;

    /**
     * The socket to use for reading from the asterisk server.
     */
    private SocketConnectionFacade socket;

    /**
     * If set to <code>true</code>, terminates and closes the reader.
     */
    private boolean die = false;

    /**
     * Creates a new ManagerReaderImpl.
     * 
     * @param dispatcher the dispatcher to use for dispatching events and responses.
     */
    public ManagerReaderImpl(final Dispatcher dispatcher, AsteriskServer asteriskServer)
    {
        this.dispatcher = dispatcher;
        this.asteriskServer = asteriskServer;

        this.eventBuilder = new EventBuilderImpl();
        this.responseBuilder = new ResponseBuilderImpl();
    }

    /**
     * Sets the socket to use for reading from the asterisk server.
     * 
     * @param socket the socket to use for reading from the asterisk server.
     */
    public void setSocket(final SocketConnectionFacade socket)
    {
        this.socket = socket;
    }

    public void registerEventClass(Class eventClass)
    {
        eventBuilder.registerEventClass(eventClass);
    }

    /**
     * Reads line by line from the asterisk server, sets the protocol identifier as soon as it is
     * received and dispatches the received events and responses via the associated dispatcher.
     * 
     * @see DefaultManagerConnection#dispatchEvent(ManagerEvent)
     * @see DefaultManagerConnection#dispatchResponse(ManagerResponse)
     * @see DefaultManagerConnection#setProtocolIdentifier(String)
     */
    public void run()
    {
        final Map<String, String> buffer = new HashMap<String, String>();
        final List<String> commandResult = new ArrayList<String>();
        String line;
        boolean processingCommandResult = false;

        if (socket == null)
        {
            throw new IllegalStateException("Unable to run: socket is null.");
        }

        this.die = false;

        try
        {
            while ((line = socket.readLine()) != null && !this.die)
            {
                // dirty hack for handling the CommandAction. Needs fix when manager protocol is
                // enhanced.
                if (processingCommandResult)
                {
                    // in case of an error Asterisk sends a Usage: and an END COMMAND
                    // that is prepended by a space :(
                    if ("--END COMMAND--".equals(line) || " --END COMMAND--".equals(line))
                    {
                        CommandResponse commandResponse = new CommandResponse();

                        for (int crIdx = 0; crIdx < commandResult.size(); crIdx++)
                        {
                            String[] crNVPair = ((String) commandResult.get(crIdx)).split(" *: *", 2);

                            if (crNVPair[0].equalsIgnoreCase("ActionID"))
                            {
                                // Remove the command response nvpair from the 
                                // command result array and decrement index so we
                                // don't skip the "new" current line
                                commandResult.remove(crIdx--);

                                // Register the action id with the command result
                                commandResponse.setActionId(crNVPair[1]);
                            }
                            else if (crNVPair[0].equalsIgnoreCase("Privilege"))
                            {
                                // Remove the command response nvpair from the 
                                // command result array and decrement index so we
                                // don't skip the "new" current line
                                commandResult.remove(crIdx--);                            	
                            }
                            else
                            {
                                // Didn't find a name:value pattern, so we're now in the 
                                // command results.  Stop processing the nv pairs.
                                break;
                            }
                        }
                        commandResponse.setResponse("Follows");
                        commandResponse.setDateReceived(DateUtil.getDate());
                        commandResponse.setResult(commandResult);
                        Map<String, String> attributes = new HashMap<String, String>();
                        attributes.put("actionid", commandResponse.getActionId());
                        attributes.put("response", commandResponse.getResponse());
                        commandResponse.setAttributes(attributes);
                        dispatcher.dispatchResponse(commandResponse);
                        processingCommandResult = false;
                    }
                    else
                    {
                        commandResult.add(line);
                    }
                    continue;
                }

                // Reponse: Follows indicates that the output starting on the next line until
                // --END COMMAND-- must be treated as raw output of a command executed by a
                // CommandAction.
                if ("Response: Follows".equalsIgnoreCase(line))
                {
                    processingCommandResult = true;
                    commandResult.clear();
                    continue;
                }

                // maybe we will find a better way to identify the protocol identifier but for now
                // this works quite well.
                if (line.startsWith("Asterisk Call Manager/") ||
                        line.startsWith("Asterisk Manager Proxy/"))
                {
                    ConnectEvent connectEvent = new ConnectEvent(asteriskServer);
                    connectEvent.setProtocolIdentifier(line);
                    connectEvent.setDateReceived(DateUtil.getDate());
                    dispatcher.dispatchEvent(connectEvent);
                    continue;
                }

                // an empty line indicates a normal response's or event's end so we build
                // the corresponding value object and dispatch it through the ManagerConnection.
                if (line.length() == 0)
                {
                    if (buffer.containsKey("response"))
                    {
                        ManagerResponse response = buildResponse(buffer);
                        logger.debug("attempting to build response");
                        if (response != null)
                        {
                            dispatcher.dispatchResponse(response);
                        } 
                    }
                    else if (buffer.containsKey("event"))
                    {
                        logger.debug("attempting to build event: " + buffer.get("event"));
                        ManagerEvent event = buildEvent(asteriskServer, buffer);
                        if (event != null)
                        {
                            dispatcher.dispatchEvent(event);
                        }
                        else
                        {
                            logger.debug("buildEvent returned null");
                        }
                    }
                    else
                    {
                        if (buffer.size() > 0)
                        {
                            logger.debug("buffer contains neither response nor event");
                        }
                    }

                    buffer.clear();
                }
                else
                {
                    int delimiterIndex;

                    delimiterIndex = line.indexOf(":");
                    if (delimiterIndex > 0 && line.length() > delimiterIndex + 2)
                    {
                        String name;
                        String value;

                        name = line.substring(0, delimiterIndex).toLowerCase();
                        value = line.substring(delimiterIndex + 2);

                        buffer.put(name, value);
                        logger.debug("Got name [" + name + "], value: [" + value + "]");
                    }
                }
            }
            logger.debug("Reached end of stream, terminating reader.");
        }
        catch (IOException e)
        {
            logger.info("IOException while reading from asterisk server, terminating reader thread: " + e.getMessage());
        }
        finally
        {
            // cleans resources and reconnects if needed
            DisconnectEvent disconnectEvent = new DisconnectEvent(asteriskServer);
            disconnectEvent.setDateReceived(DateUtil.getDate());
            dispatcher.dispatchEvent(disconnectEvent);
        }
    }

    public void die()
    {
        this.die = true;
    }

    private ManagerResponse buildResponse(Map<String, String> buffer)
    {
        ManagerResponse response;

        response = responseBuilder.buildResponse(buffer);

        if (response != null)
        {
            response.setDateReceived(DateUtil.getDate());
        }

        return response;
    }

    private ManagerEvent buildEvent(Object source, Map<String, String> buffer)
    {
        ManagerEvent event;

        event = eventBuilder.buildEvent(source, buffer);

        if (event != null)
        {
            event.setDateReceived(DateUtil.getDate());
        }

        return event;
    }
}
