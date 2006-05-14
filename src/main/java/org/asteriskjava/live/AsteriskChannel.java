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

import java.beans.PropertyChangeListener;
import java.util.Date;
import java.util.List;

/**
 * An Asterisk channel.
 * 
 * @author srt
 */
public interface AsteriskChannel
{
    /**
     * Returns the unique id of this channel, for example "1099015093.165".<br>
     * The unique id of an AsteriskChannel is immutable for the whole lifecycle
     * of the channel.
     * 
     * @return the unique id of this channel.
     */
    String getId();

    /**
     * Returns the name of this channel, for example "SIP/1310-20da".<br>
     * In contrast to the unique id the name of an AsteriskChannel can change
     * while the call is processed.
     * 
     * @return the name of this channel.
     */
    String getName();

    /**
     * Returns the caller id number of this channel.
     * 
     * @return the caller id number of this channel.
     */
    String getCallerIdNumber();

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
     * Returns the reason for hangup.
     * 
     * @return the reason for hangup or <code>null</code> if the
     *         channel has not yet been hung up or no hangup cause is available
     *         for this type of channel.
     * @since 0.3
     */
    HangupCause getHangupCause();

    /**
     * Returns a textual representation of the reason for hangup.
     * 
     * @return the textual representation of the reason for hangup or
     *         <code>null</code> if the channel has not yet been hung up or no
     *         hangup cause is available for this type of channel. If no hangup
     *         cause is available an empty String may be returned, too.
     * @since 0.3
     */
    String getHangupCauseText();

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
    boolean wasLinked();

    /**
     * Hangs up this channel.
     * 
     * @throws ManagerCommunicationException if the hangup action cannot be sent
     *             to Asterisk.
     * @throws NoSuchChannelException if this channel had already been hung up
     *             before the hangup was sent.
     * @since 0.3
     */
    void hangup() throws ManagerCommunicationException, NoSuchChannelException;

    /**
     * Hangs up this channel using a given cause code. The cause code is mainly
     * used for Zap PRI channels where it makes Asterisk send a PRI DISCONNECT 
     * message with the set CAUSE element to the switch. 
     * 
     * @param cause the cause code to send.
     * @throws ManagerCommunicationException if the hangup action cannot be sent
     *             to Asterisk.
     * @throws NoSuchChannelException if this channel had already been hung up
     *             before the hangup was sent.
     * @since 0.3
     */
    void hangup(HangupCause cause) throws ManagerCommunicationException, NoSuchChannelException;

    /**
     * Redirects this channel to a new extension.<br>
     * If this channel is linked to another channel, the linked channel is hung
     * up.
     * 
     * @param context the destination context.
     * @param exten the destination extension.
     * @param priority the destination priority.
     * @throws ManagerCommunicationException if the redirect action cannot be
     *             sent to Asterisk.
     * @throws NoSuchChannelException if this channel had been hung up before
     *             the redirect was sent.
     * @since 0.3
     */
    void redirect(String context, String exten, int priority) throws ManagerCommunicationException, NoSuchChannelException;

    /**
     * Redirects this channel and the channel this channel is linked to to a new extension.<br>
     * If this channel is not linked to another channel only this channel is redirected.
     * 
     * @param context the destination context.
     * @param exten the destination extension.
     * @param priority the destination priority.
     * @throws ManagerCommunicationException if the redirect action cannot be
     *             sent to Asterisk.
     * @throws NoSuchChannelException if this channel had been hung up before
     *             the redirect was sent.
     * @since 0.3
     */
    void redirectBothLegs(String context, String exten, int priority) throws ManagerCommunicationException, NoSuchChannelException;
    
    /**
     * Returns the value of the given channel variable.<br>
     * Currently Asterisk does not support the retrieval of built-in variables
     * like EXTEN or CALLERIDNUM but only custom variables set via Asterisk's
     * Set command or via {@link #setVariable(String, String)}.
     * 
     * @param variable the name of the channel variable to return.
     * @return the value of the channel variable or <code>null</code> if it is
     *         not set.
     * @throws ManagerCommunicationException if the get variable action cannot be
     *             sent to Asterisk.
     * @throws NoSuchChannelException if this channel had been hung up before
     *             the variable was requested.
     * @since 0.3
     */
    String getVariable(String variable) throws ManagerCommunicationException, NoSuchChannelException;

    /**
     * Sets the value of the given channel variable.
     * 
     * @param variable the name of the channel variable to set.
     * @param value the value of the channel variable to set.
     * @throws ManagerCommunicationException if the set variable action cannot be
     *             sent to Asterisk.
     * @throws NoSuchChannelException if this channel had been hung up before
     *             the variable was set.
     * @since 0.3
     */
    void setVariable(String variable, String value) throws ManagerCommunicationException, NoSuchChannelException;
    
    public void addPropertyChangeListener(PropertyChangeListener listener);

    public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener);

    public void removePropertyChangeListener(PropertyChangeListener listener);

    public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener);
}
