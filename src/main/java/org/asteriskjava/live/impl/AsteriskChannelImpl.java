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
package org.asteriskjava.live.impl;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.asteriskjava.live.AsteriskChannel;
import org.asteriskjava.live.ChannelState;
import org.asteriskjava.live.Extension;
import org.asteriskjava.live.HangupCause;
import org.asteriskjava.live.ManagerCommunicationException;
import org.asteriskjava.live.NoSuchChannelException;
import org.asteriskjava.manager.action.GetVarAction;
import org.asteriskjava.manager.action.HangupAction;
import org.asteriskjava.manager.action.RedirectAction;
import org.asteriskjava.manager.action.SetVarAction;
import org.asteriskjava.manager.response.ManagerError;
import org.asteriskjava.manager.response.ManagerResponse;

/**
 * Default implementation of the AsteriskChannel interface.
 * 
 * @author srt
 * @version $Id$
 */
public class AsteriskChannelImpl implements AsteriskChannel
{
    private static final String CAUSE_VARIABLE_NAME = "PRI_CAUSE";
    private final ManagerConnectionPool connectionPool;
    private final PropertyChangeSupport changes;

    /**
     * Unique id of this channel.
     */
    private final String id;

    /**
     * Date this channel has been created.
     */
    private final Date dateOfCreation;

    /**
     * Name of this channel.
     */
    private String name;

    /**
     * Caller ID Number of this channel.
     */
    private String callerIdNumber;

    /**
     * Caller ID Name of this channel.
     */
    private String callerIdName;

    /**
     * State of this channel.
     */
    private ChannelState state;

    /**
     * Account code used to bill this channel.
     */
    private String account;
    private final List<Extension> extensions;

    /**
     * If this channel is bridged to another channel, the linkedChannel contains
     * the channel this channel is bridged with.
     */
    private AsteriskChannel linkedChannel;

    /**
     * Indicates if this channel was linked to another channel at least once.
     */
    private boolean wasLinked;
    
    private HangupCause hangupCause;
    
    private String hangupCauseText;

    /**
     * Creates a new Channel.
     * 
     * @param name name of this channel, for example "SIP/1310-20da".
     * @param id unique id of this channel, for example "1099015093.165".
     */
    AsteriskChannelImpl(final ManagerConnectionPool connectionPool, final String name, final String id, final Date dateOfCreation)
    {
        this.connectionPool = connectionPool;
        this.name = name;
        this.id = id;
        this.dateOfCreation = dateOfCreation;
        this.extensions = new ArrayList<Extension>();
        this.changes = new PropertyChangeSupport(this);
    }

    public String getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    /**
     * Sets the name of this channel.
     * 
     * @param name the name of this channel.
     */
    void setName(final String name)
    {
        String oldName = this.name;

        this.name = name;
        firePropertyChange("name", oldName, name);
    }

    public String getCallerIdNumber()
    {
        return callerIdNumber;
    }

    /**
     * Sets the caller id number of this channel.
     * 
     * @param callerIdNumber the caller id number of this channel.
     */
    void setCallerIdNumber(final String callerIdNumber)
    {
        String oldCallerIdNumber = this.callerIdNumber;

        this.callerIdNumber = callerIdNumber;
        firePropertyChange("callerIdNumber", oldCallerIdNumber, callerIdNumber);
    }

    public String getCallerIdName()
    {
        return callerIdName;
    }

    /**
     * Sets the caller id of this channel.
     * 
     * @param callerIdName the caller id name of this channel.
     */
    void setCallerIdName(String callerIdName)
    {
        String oldCallerIdName = this.callerIdName;
        
        this.callerIdName = callerIdName;
        firePropertyChange("callerIdName", oldCallerIdName, callerIdName);
    }

    public ChannelState getState()
    {
        return state;
    }

    /**
     * Sets the state of this channel.
     * 
     * @param state the state of this channel.
     */
    void setState(ChannelState state)
    {
        ChannelState oldState = this.state;

        this.state = state;
        firePropertyChange("state", oldState, state);
    }

    public String getAccount()
    {
        return account;
    }

    /**
     * Sets the account code used to bill this channel.
     * 
     * @param account the account code used to bill this channel.
     */
    void setAccount(String account)
    {
        String oldAccount = this.account;

        this.account = account;
        changes.firePropertyChange("account", oldAccount, account);
    }

    public Extension getCurrentExtension()
    {
        Extension extension;

        synchronized (extensions)
        {
            if (extensions.isEmpty())
            {
                extension = null;
            }
            else
            {
                extension = extensions.get(extensions.size() - 1);
            }
        }

        return extension;
    }

    public Extension getFirstExtension()
    {
        Extension extension;

        synchronized (extensions)
        {
            if (extensions.isEmpty())
            {
                extension = null;
            }
            else
            {
                extension = extensions.get(0);
            }
        }

        return extension;
    }
    
    public List<Extension> getExtensions()
    {
        List<Extension> extensionsCopy;

        synchronized (extensions)
        {
            extensionsCopy = new ArrayList<Extension>(extensions);
        }

        return extensionsCopy;
    }

    /**
     * Adds a visted dialplan entry to the history.
     * 
     * @param extension the visted dialplan entry to add.
     * @since 0.2
     */
    void addExtension(Extension extension)
    {
        Extension oldCurrentExtension = getCurrentExtension();
        
        synchronized (extensions)
        {
            extensions.add(extension);
        }
        
        firePropertyChange("currentExtension", oldCurrentExtension, extension);
    }

    public Date getDateOfCreation()
    {
        return dateOfCreation;
    }

    public HangupCause getHangupCause()
    {
        return hangupCause;
    }

    void setHangupCause(HangupCause hangupCause)
    {
        HangupCause oldHangupCause = this.hangupCause;

        this.hangupCause = hangupCause;
        firePropertyChange("hangupCause", oldHangupCause, hangupCause);
    }

    public String getHangupCauseText()
    {
        return hangupCauseText;
    }

    void setHangupCauseText(String hangupCauseText)
    {
        String oldHangupCauseText = this.hangupCauseText;

        this.hangupCauseText = hangupCauseText;
        firePropertyChange("hangupCauseText", oldHangupCauseText, hangupCauseText);
    }

    public AsteriskChannel getLinkedChannel()
    {
        return linkedChannel;
    }

    /**
     * Sets the channel this channel is bridged with.
     * 
     * @param linkedChannel the channel this channel is bridged with.
     */
    void setLinkedChannel(AsteriskChannel linkedChannel)
    {
        AsteriskChannel oldLinkedChannel = this.linkedChannel;
        
        this.linkedChannel = linkedChannel;
        if (linkedChannel != null)
        {
            this.wasLinked = true;
        }
        firePropertyChange("linkedChannel", oldLinkedChannel, linkedChannel);
    }

    public boolean wasLinked()
    {
        return wasLinked;
    }

    // action methods

    public void hangup() throws ManagerCommunicationException, NoSuchChannelException
    {
        ManagerResponse response;

        response = connectionPool.sendAction(new HangupAction(name));
        if (response instanceof ManagerError)
        {
            throw new NoSuchChannelException("Channel '" + name + "' is not available: " + response.getMessage());
        }
    }

    public void hangup(HangupCause cause) throws ManagerCommunicationException, NoSuchChannelException
    {
        if (cause != null)
        {
            setVariable(CAUSE_VARIABLE_NAME, Integer.toString(cause.getCode()));
        }
        hangup();
    }
    
    public void redirect(String context, String exten, int priority) throws ManagerCommunicationException, NoSuchChannelException
    {
        ManagerResponse response;

        response = connectionPool.sendAction(new RedirectAction(name, context, exten, priority));
        if (response instanceof ManagerError)
        {
            throw new NoSuchChannelException("Channel '" + name + "' is not available: " + response.getMessage());
        }
    }
    
    public String getVariable(String variable) throws ManagerCommunicationException, NoSuchChannelException
    {
        ManagerResponse response;
        String value;

        response = connectionPool.sendAction(new GetVarAction(name, variable));
        if (response instanceof ManagerError)
        {
            throw new NoSuchChannelException("Channel '" + name + "' is not available: " + response.getMessage());
        }
        value = response.getAttribute("Value");
        if (value == null)
        {
            value = response.getAttribute(variable); // for Asterisk 1.0.x
        }
        return value;
    }
    
    public void setVariable(String variable, String value) throws ManagerCommunicationException, NoSuchChannelException
    {
        ManagerResponse response;

        response = connectionPool.sendAction(new SetVarAction(name, variable, value));
        if (response instanceof ManagerError)
        {
            throw new NoSuchChannelException("Channel '" + name + "' is not available: " + response.getMessage());
        }
    }

    // notification methods

    public void addPropertyChangeListener(PropertyChangeListener listener)
    {
        changes.addPropertyChangeListener(listener);
    }

    public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener)
    {
        changes.addPropertyChangeListener(propertyName, listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener)
    {
        changes.removePropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener)
    {
        changes.removePropertyChangeListener(propertyName, listener);
    }

    public String toString()
    {
        StringBuffer sb;
        AsteriskChannel linkedChannel;
        int systemHashcode;

        sb = new StringBuffer("AsteriskChannel[");

        synchronized (this)
        {
            sb.append("id='" + getId() + "',");
            sb.append("name='" + getName() + "',");
            sb.append("callerIdNumber='" + getCallerIdNumber() + "',");
            sb.append("callerIdName='" + getCallerIdName() + "',");
            sb.append("state='" + getState() + "',");
            sb.append("account='" + getAccount() + "',");
            sb.append("dateOfCreation=" + getDateOfCreation() + ",");
            linkedChannel = this.linkedChannel;
            systemHashcode = System.identityHashCode(this);
        }
        if (linkedChannel == null)
        {
            sb.append("linkedChannel=null,");
        }
        else
        {
            sb.append("linkedChannel=[");
            synchronized (linkedChannel)
            {
                sb.append(linkedChannel.getClass().getName() + "[");
                sb.append("id='" + linkedChannel.getId() + "',");
                sb.append("name='" + linkedChannel.getName() + "',");
                sb.append("systemHashcode="
                        + System.identityHashCode(linkedChannel));
                sb.append("]");
            }
            sb.append("],");
        }
        sb.append("systemHashcode=" + systemHashcode);
        sb.append("]");

        return sb.toString();
    }
    
    private void firePropertyChange(String propertyName, Object oldValue, Object newValue)
    {
        if (oldValue != null || newValue != null)
        {
            changes.firePropertyChange(propertyName, oldValue, newValue);
        }
    }
}
