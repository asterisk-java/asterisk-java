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
package org.asteriskjava.live;

import java.util.Date;
import java.util.List;

import org.asteriskjava.manager.ManagerCommunicationException;
import org.asteriskjava.manager.NoSuchChannelException;

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

    /**
     * Hangs up this channel.
     * 
     * @throws ManagerCommunicationException if the hangup action cannot be sent to Asterisk.
     * @throws NoSuchChannelException if this channel had already been hung up before the hangup was sent.
     */
    void hangup() throws ManagerCommunicationException, NoSuchChannelException;

    /**
     * Redirects this channel to a new extension.<br>
     * If this channel is linked to another channel, the linked channel is hung
     * up.
     * 
     * @param context the destination context.
     * @param exten the destination extension.
     * @param priority the destination priority.
     * @throws ManagerCommunicationException if the redirect action cannot be sent to Asterisk.
     * @throws NoSuchChannelException if this channel had been hung up before the redirect was sent.
     */
    void redirect(String context, String exten, int priority) throws ManagerCommunicationException, NoSuchChannelException;

    /**
     * Returns the value of the given channel variable.<br>
     * Currently Asterisk does not support the retrieval of built-in variables like
     * EXTEN or CALLERIDNUM but only custom variables set via Asterisk's Set command
     * or via {@link #setVariable(String, String)}.
     * 
     * @param variable the name of the channel variable to return.
     * @return the value of the channel variable or <code>null</code> if it is not set.
     * @throws ManagerCommunicationException if the redirect action cannot be sent to Asterisk.
     * @throws NoSuchChannelException if this channel had been hung up before the variable was request.
     */
    String getVariable(String variable) throws ManagerCommunicationException, NoSuchChannelException;

    void setVariable(String variable, String value) throws ManagerCommunicationException, NoSuchChannelException;
}
