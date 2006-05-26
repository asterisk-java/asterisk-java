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
package org.asteriskjava.live;

import java.util.Collection;
import java.util.Map;

/**
 * The AsteriskServer is built on top of the {@link org.asteriskjava.manager.ManagerConnection}
 * and is an attempt to simplify interaction with Asterisk by abstracting the interface.
 * <p>
 * You will certainly have less freedom using AsteriskServer but it will make
 * life easier for easy things (like originating a call or getting a list of
 * open channels).
 * <p>
 * AsteriskServer is still in an early state of development. So, when using
 * AsteriskServer be aware that it might change in the future.
 * 
 * @author srt
 * @version $Id$
 */
public interface AsteriskServer
{
    /**
     * Generates an outgoing channel to a dialplan entry (extension, context, priority).
     * 
     * @param channel channel name to call, for example "SIP/1310.
     * @param context context to connect to
     * @param exten extension to connect to
     * @param priority priority to connect to
     * @param timeout how long to wait for the channel to be answered before its considered to have failed (in ms)
     * @return the generated channel
     * @throws ManagerCommunicationException if the originate action cannot be sent to Asterisk
     */
    AsteriskChannel originateToExtension(String channel, String context, String exten, int priority, long timeout) 
        throws ManagerCommunicationException;

    /**
     * Generates an outgoing channel to a dialplan entry (extension, context, priority)
     * and sets an optional map of channel variables.
     * 
     * @param channel channel name to call, for example "SIP/1310.
     * @param context context to connect to
     * @param exten extension to connect to
     * @param priority priority to connect to
     * @param timeout how long to wait for the channel to be answered before its considered to have failed (in ms)
     * @param variables channel variables to set, may be <code>null</code>.
     * @return the generated channel
     * @throws ManagerCommunicationException if the originate action cannot be sent to Asterisk
     */
    AsteriskChannel originateToExtension(String channel, String context, String exten, int priority, long timeout, Map<String, String> variables) 
        throws ManagerCommunicationException;

    /**
     * Generates an outgoing channel to an application.
     * 
     * @param channel channel name to call, for example "SIP/1310.
     * @param application application to connect to, for example "MeetMe"
     * @param data data to pass to the application, for example "1000|d", may be <code>null</code>.
     * @param timeout how long to wait for the channel to be answered before its considered to have failed (in ms)
     * @return the generated channel
     * @throws ManagerCommunicationException if the originate action cannot be sent to Asterisk
     */
    AsteriskChannel originateToApplication(String channel, String application, String data, long timeout) 
        throws ManagerCommunicationException;

    /**
     * Generates an outgoing channel to an application and sets an optional
     * map of channel variables.
     * 
     * @param channel channel name to call, for example "SIP/1310.
     * @param application application to connect to, for example "MeetMe"
     * @param data data to pass to the application, for example "1000|d", may be <code>null</code>.
     * @param timeout how long to wait for the channel to be answered before its considered to have failed (in ms)
     * @param variables channel variables to set, may be <code>null</code>.
     * @return the generated channel
     * @throws ManagerCommunicationException if the originate action cannot be sent to Asterisk
     */
    AsteriskChannel originateToApplication(String channel, String application, String data, long timeout, Map<String, String> variables) 
        throws ManagerCommunicationException;

    /**
     * Returns the active channels of the Asterisk server.
     * 
     * @return a Collection of active channels.
     */
    Collection<AsteriskChannel> getChannels();

    /**
     * Returns a channel by its name.
     * 
     * @param name name of the channel to return
     * @return the channel with the given name
     */
    AsteriskChannel getChannelByName(String name);

    /**
     * Returns a channel by its unique id.
     * 
     * @param id the unique id of the channel to return
     * @return the channel with the given unique id
     */
    AsteriskChannel getChannelById(String id);

    /**
     * Returns the acitve MeetMe rooms on the Asterisk server.
     *  
     * @return a Collection of MeetMeRooms
     */
    Collection<MeetMeRoom> getMeetMeRooms();

    /**
     * Returns the queues served by the Asterisk server.
     * 
     * @return a Collection of queues.
     */
    Collection<AsteriskQueue> getQueues();

    /**
     * Returns the version of the Asterisk server you are connected to.<p>
     * This typically looks like "Asterisk 1.0.9 built by root@host on a i686
     * running Linux".
     * 
     * @return the version of the Asterisk server you are connected to
     * @throws ManagerCommunicationException if the version cannot be retrieved from Asterisk
     * @since 0.2
     */
    String getVersion() throws ManagerCommunicationException;

    /**
     * Returns the CVS revision of a given source file of the Asterisk server
     * you are connected to.
     * <p>
     * For example getVersion("app_meetme.c") may return {1, 102} for CVS
     * revision "1.102".
     * <p>
     * Note that this feature is not available with Asterisk 1.0.x.<p>
     * You can use this feature if you need to write applications that behave
     * different depending on specific modules being available in a specific
     * version or not.
     * 
     * @param file the file for which to get the version like "app_meetme.c"
     * @return the CVS revision of the file, or <code>null</code> if that file
     *         is not part of the Asterisk instance you are connected to (maybe
     *         due to a module that provides it has not been loaded) or if you
     *         are connected to an Astersion 1.0.x
     * @throws ManagerCommunicationException if the version cannot be retrieved from Asterisk
     * @since 0.2
     */
    int[] getVersion(String file) throws ManagerCommunicationException;
}
