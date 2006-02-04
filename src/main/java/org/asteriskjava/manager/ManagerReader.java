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
package org.asteriskjava.manager;

import org.asteriskjava.io.SocketConnectionFacade;

/**
 * The ManagerReader reads events and responses from the asterisk server, parses
 * them using EventBuilderImpl and ResponseBuilder and dispatches them to the
 * associated ManagerConnection.<br>
 * Do not use this interface in your code, it is intended to be used only by the
 * DefaultManagerConnection.
 * 
 * @see org.asteriskjava.manager.EventBuilder
 * @see org.asteriskjava.manager.ResponseBuilder
 * @see org.asteriskjava.manager.DefaultManagerConnection
 * @author srt
 * @version $Id: ManagerReader.java,v 1.13 2005/10/25 22:49:56 srt Exp $
 */

public interface ManagerReader extends Runnable
{

    /**
     * Sets the socket to use for reading from the asterisk server.
     * 
     * @param socket the socket to use for reading from the asterisk server.
     */
    void setSocket(final SocketConnectionFacade socket);

    /**
     * Registers a new event type with the underlying EventBuilderImpl.<br>
     * The eventClass must extend ManagerEvent.
     * 
     * @see EventBuilder
     * @see ManagerEvent
     * @param event class of the event to register.
     */
    void registerEventClass(Class event);

    void die();

}
