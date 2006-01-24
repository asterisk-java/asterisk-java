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
package org.asteriskjava.manager;

import java.io.IOException;
import java.util.Map;

/**
 * The AsteriskManager is built on top of the ManagerConnection and is an
 * attempt to simplify interaction with Asterisk by abstracting the interface.<br>
 * You will certainly have less freedom using AsteriskManager but it will make
 * life easier for easy things (like originating a call or getting a list of
 * open channels).<br>
 * AsteriskManager is still in an early state of development. So, when using
 * AsteriskManager be aware that it might change in the future.
 * 
 * @author srt
 * @version $Id: AsteriskManager.java,v 1.9 2005/07/27 23:38:14 srt Exp $
 */
public interface AsteriskManager
{
    /**
     * Generates an outgoing call.
     * 
     * @param originate conatins the details of the call to originate
     * @return a Call object representing the originated call
     * @throws TimeoutException if the originated call is not answered in time
     * @throws IOException if the action cannot be sent to the asterisk server
     */
    public Call originateCall(Originate originate) throws TimeoutException,
            IOException;

    /**
     * Returns a Map of active channels.<br>
     * The map contain the channel names as keys and objects of type
     * {@link org.asteriskjava.manager.Channel} as values.
     * 
     * @return a Map of active channels.
     */
    Map getChannels();

    /**
     * Returns a Map of all queues.<br>
     * The map contains the queue names as keys and objects of type
     * {@link Queue} as values.
     * 
     * @return a Map of queues.
     */
    Map getQueues();

    /**
     * Returns the version of the Asterisk server you are connected to.<br>
     * This typically looks like "Asterisk 1.0.9 built by root@host on a i686
     * running Linux".
     * 
     * @return the version of the Asterisk server you are connected to
     * @since 0.2
     */
    String getVersion();

    /**
     * Returns the CVS revision of a given source file of the Asterisk server
     * you are connected to.<br>
     * For example getVersion("app_meetme.c") may return {1, 102} for CVS
     * revision "1.102".<br>
     * Note that this feature is not available with Asterisk 1.0.x.<br>
     * You can use this feature if you need to write applications that behave
     * different depending on specific modules being available in a specific
     * version or not.
     * 
     * @param file the file for which to get the version like "app_meetme.c"
     * @return the CVS revision of the file, or <code>null</code> if that file
     *         is not part of the Asterisk instance you are connected to (maybe
     *         due to a module that provides it has not been loaded) or if you
     *         are connected to an Astersion 1.0.x
     * @since 0.2
     */
    int[] getVersion(String file);
}
