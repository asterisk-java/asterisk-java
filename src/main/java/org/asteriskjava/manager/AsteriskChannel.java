/*
 * Copyright 2004-2006 Stefan Reuter
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

import java.util.Date;
import java.util.List;

public interface AsteriskChannel
{
    /**
     * Returns the unique id of this channel, for example "1099015093.165".
     * 
     * @return the unique id of this channel.
     */
    String getId();

    /**
     * Returns the name of this channel, for example "SIP/1310-20da".
     * 
     * @return the name of this channel.
     */
    String getName();

    /**
     * Returns the caller id of this channel.
     * 
     * @return the caller id of this channel.
     */
    String getCallerId();

    /**
     * Returns the caller id name of this channel.
     * 
     * @return the caller id name of this channel.
     */
    String getCallerIdName();

    /**
     * Returns the state of this channel.
     * 
     * @return the state of this channel.
     */
    ChannelState getState();

    /**
     * Returns the account code used to bill this channel.
     * 
     * @return the account code used to bill this channel.
     */
    String getAccount();

    /**
     * Returns the last visited dialplan entry.
     * 
     * @return the last visited dialplan entry.
     * @since 0.2
     */
    Extension getCurrentExtension();

    /**
     * Returns the first visited dialplan entry.
     * 
     * @return the first visited dialplan entry.
     * @since 0.2
     */
    Extension getFirstExtension();

    /**
     * Returns the context of the current extension. This is a shortcut for
     * <code>getCurrentExtension().getContext()</code>.
     * 
     * @return the context of the current extension.
     */
    String getContext();

    /**
     * Returns the extension of the current extension. This is a shortcut for
     * <code>getCurrentExtension().getExtension()</code>.
     * 
     * @return the extension of the current extension.
     */
    String getExtension();

    /**
     * Returns the priority of the current extension. This is a shortcut for
     * <code>getCurrentExtension().getPriority()</code>.
     * 
     * @return the priority of the current extension.
     */
    Integer getPriority();

    /**
     * Returns a list of all visited dialplan entries.
     * 
     * @return a list of all visited dialplan entries.
     * @since 0.2
     */
    List<Extension> getExtensions();

    /**
     * Returns the date this channel has been created.
     * 
     * @return the date this channel has been created.
     */
    Date getDateOfCreation();

    /**
     * Returns the channel this channel is bridged with, if any.
     * 
     * @return the channel this channel is bridged with, or <code>null</code>
     *         if this channel is currently not bridged to another channel.
     */
    AsteriskChannel getLinkedChannel();

    /**
     * Indicates if this channel was linked to another channel at least once.
     * 
     * @return <code>true</code> if this channel was linked to another channel
     *         at least once, <code>false</code> otherwise.
     * @since 0.2
     */
    boolean getWasLinked();
    
    void hangup() throws ManagerCommunicationException, NoSuchChannelException;
    
    void redirect(String context, String exten, int priority) throws ManagerCommunicationException, NoSuchChannelException;
    
    String getVariable(String variable) throws ManagerCommunicationException, NoSuchChannelException;
}
