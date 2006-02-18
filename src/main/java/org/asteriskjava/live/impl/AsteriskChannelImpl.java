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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.asteriskjava.live.AsteriskChannel;
import org.asteriskjava.live.ChannelState;
import org.asteriskjava.live.Extension;
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
 * @version $Id: Channel.java,v 1.13 2005/09/01 19:13:20 srt Exp $
 */
public class AsteriskChannelImpl implements AsteriskChannel
{
    private final ManagerConnectionPool connectionPool;

    /**
     * Unique id of this channel.
     */
    private final String id;

    /**
     * Name of this channel.
     */
    private String name;

    /**
     * Caller ID of this channel.
     */
    private String callerId;

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
     * Date this channel has been created.
     */
    private Date dateOfCreation;

    /**
     * If this channel is bridged to another channel, the linkedChannel contains
     * the channel this channel is bridged with.
     */
    private AsteriskChannel linkedChannel;

    /**
     * Indicates if this channel was linked to another channel at least once.
     */
    private boolean wasLinked;

    /**
     * Creates a new Channel.
     * 
     * @param name name of this channel, for example "SIP/1310-20da".
     * @param id unique id of this channel, for example "1099015093.165".
     */
    public AsteriskChannelImpl(final ManagerConnectionPool connectionPool, final String name, final String id)
    {
        this.connectionPool = connectionPool;
        this.name = name;
        this.id = id;
        this.extensions = new ArrayList<Extension>();
    }

    /**
     * Returns the unique id of this channel, for example "1099015093.165".
     * 
     * @return the unique id of this channel.
     */
    public final String getId()
    {
        return id;
    }

    /**
     * Returns the name of this channel, for example "SIP/1310-20da".
     * 
     * @return the name of this channel.
     */
    public final String getName()
    {
        return name;
    }

    /**
     * Sets the name of this channel.
     * 
     * @param name the name of this channel.
     */
    public final void setName(final String name)
    {
        this.name = name;
    }

    /**
     * Returns the caller id of this channel.
     * 
     * @return the caller id of this channel.
     */
    public final String getCallerId()
    {
        return callerId;
    }

    /**
     * Sets the caller id of this channel.
     * 
     * @param callerId the caller id of this channel.
     */
    public final void setCallerId(final String callerId)
    {
        this.callerId = callerId;
    }

    /**
     * Returns the caller id name of this channel.
     * 
     * @return the caller id name of this channel.
     */
    public final String getCallerIdName()
    {
        return callerIdName;
    }

    /**
     * Sets the caller id of this channel.
     * 
     * @param callerIdName the caller id name of this channel.
     */
    public final void setCallerIdName(final String callerIdName)
    {
        this.callerIdName = callerIdName;
    }

    /**
     * Returns the state of this channel.
     * 
     * @return the state of this channel.
     */
    public final ChannelState getState()
    {
        return state;
    }

    /**
     * Sets the state of this channel.
     * 
     * @param state the state of this channel.
     */
    public final void setState(final ChannelState state)
    {
        this.state = state;
    }

    /**
     * Returns the account code used to bill this channel.
     * 
     * @return the account code used to bill this channel.
     */
    public final String getAccount()
    {
        return account;
    }

    /**
     * Sets the account code used to bill this channel.
     * 
     * @param account the account code used to bill this channel.
     */
    public final void setAccount(final String account)
    {
        this.account = account;
    }

    /**
     * Returns the last visited dialplan entry.
     * 
     * @return the last visited dialplan entry.
     * @since 0.2
     */
    public final Extension getCurrentExtension()
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
                extension = (Extension) extensions.get(extensions.size() - 1);
            }
        }

        return extension;
    }

    /**
     * Returns the first visited dialplan entry.
     * 
     * @return the first visited dialplan entry.
     * @since 0.2
     */
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
                extension = (Extension) extensions.get(0);
            }
        }

        return extension;
    }

    /**
     * Returns the context of the current extension. This is a shortcut for
     * <code>getCurrentExtension().getContext()</code>.
     * 
     * @return the context of the current extension.
     */
    public String getContext()
    {
        Extension currentExtension;
        
        currentExtension = getCurrentExtension();
        return currentExtension == null ? null : currentExtension.getContext();
    }

    /**
     * Returns the extension of the current extension. This is a shortcut for
     * <code>getCurrentExtension().getExtension()</code>.
     * 
     * @return the extension of the current extension.
     */
    public String getExtension()
    {
        Extension currentExtension;
        
        currentExtension = getCurrentExtension();
        return currentExtension == null ? null : currentExtension.getExtension();
    }

    /**
     * Returns the priority of the current extension. This is a shortcut for
     * <code>getCurrentExtension().getPriority()</code>.
     * 
     * @return the priority of the current extension.
     */
    public Integer getPriority()
    {
        Extension currentExtension;
        
        currentExtension = getCurrentExtension();
        return currentExtension == null ? null : currentExtension.getPriority();
    }
    
    /**
     * Returns a list of all visited dialplan entries.
     * 
     * @return a list of all visited dialplan entries.
     * @since 0.2
     */
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
    public void addExtension(Extension extension)
    {
        synchronized (extensions)
        {
            extensions.add(extension);
        }
    }

    /**
     * Returns the date this channel has been created.
     * 
     * @return the date this channel has been created.
     */
    public final Date getDateOfCreation()
    {
        return dateOfCreation;
    }

    /**
     * Sets the date this channel has been created.
     * 
     * @param dateOfCreation the date this channel has been created.
     */
    public final void setDateOfCreation(final Date dateOfCreation)
    {
        this.dateOfCreation = dateOfCreation;
    }

    /**
     * Returns the channel this channel is bridged with, if any.
     * 
     * @return the channel this channel is bridged with, or <code>null</code>
     *         if this channel is currently not bridged to another channel.
     */
    public final AsteriskChannel getLinkedChannel()
    {
        return linkedChannel;
    }

    /**
     * Sets the channel this channel is bridged with.
     * 
     * @param linkedChannel the channel this channel is bridged with.
     */
    public final void setLinkedChannel(final AsteriskChannel linkedChannel)
    {
        this.linkedChannel = linkedChannel;
        if (linkedChannel != null)
        {
            this.wasLinked = true;
        }
    }

    /**
     * Indicates if this channel was linked to another channel at least once.
     * 
     * @return <code>true</code> if this channel was linked to another channel
     *         at least once, <code>false</code> otherwise.
     * @since 0.2
     */
    public final boolean getWasLinked()
    {
        return wasLinked;
    }
    
    public void hangup() throws ManagerCommunicationException, NoSuchChannelException
    {
        ManagerResponse response;

        response = connectionPool.sendAction(new HangupAction(name));
        if (response instanceof ManagerError)
        {
            throw new NoSuchChannelException("Channel '" + name + "' is not available: " + response.getMessage());
        }
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

    public String toString()
    {
        StringBuffer sb;
        AsteriskChannel linkedChannel;
        int systemHashcode;

        sb = new StringBuffer(getClass().getName() + "[");

        synchronized (this)
        {
            sb.append("id='" + getId() + "',");
            sb.append("name='" + getName() + "',");
            sb.append("callerId='" + getCallerId() + "',");
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
}
