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
 * @version $Id$
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
    public void setName(final String name)
    {
        this.name = name;
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
    public void setCallerIdNumber(final String callerIdNumber)
    {
        this.callerIdNumber = callerIdNumber;
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
    public void setCallerIdName(String callerIdName)
    {
        this.callerIdName = callerIdName;
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
    public void setState(ChannelState state)
    {
        this.state = state;
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
    public void setAccount(String account)
    {
        this.account = account;
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
    public void addExtension(Extension extension)
    {
        synchronized (extensions)
        {
            extensions.add(extension);
        }
    }

    public Date getDateOfCreation()
    {
        return dateOfCreation;
    }

    /**
     * Sets the date this channel has been created.
     * 
     * @param dateOfCreation the date this channel has been created.
     */
    public void setDateOfCreation(Date dateOfCreation)
    {
        this.dateOfCreation = dateOfCreation;
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
    public void setLinkedChannel(AsteriskChannel linkedChannel)
    {
        this.linkedChannel = linkedChannel;
        if (linkedChannel != null)
        {
            this.wasLinked = true;
        }
    }

    public boolean getWasLinked()
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
}
