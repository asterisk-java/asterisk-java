/*
 * Copyright  2004-2005 Stefan Reuter
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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author srt
 * @version $Id: Channel.java,v 1.13 2005/09/01 19:13:20 srt Exp $
 */
public class Channel implements Serializable
{
    /**
     * Serializable version identifier
     */
    private static final long serialVersionUID = -6919877370396385380L;

    private AsteriskServer asteriskServer;

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
    private ChannelStateEnum state;

    /**
     * Account code used to bill this channel.
     */
    private String account;
    private final List extensions;

    /**
     * Date this channel has been created.
     */
    private Date dateOfCreation;

    /**
     * If this channel is bridged to another channel, the linkedChannel contains
     * the channel this channel is bridged with.
     */
    private Channel linkedChannel;

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
    public Channel(final String name, final String id)
    {
        this(name, id, null);
    }

    /**
     * Creates a new Channel on the given server.
     * 
     * @param name name of this channel, for example "SIP/1310-20da".
     * @param id unique id of this channel, for example "1099015093.165".
     * @param server the Asterisk server this channel exists on.
     */
    public Channel(final String name, final String id,
            final AsteriskServer server)
    {
        this.name = name;
        this.id = id;
        this.asteriskServer = server;
        this.extensions = new ArrayList();
    }

    /**
     * Returns the Asterisk server.
     * 
     * @return the Asterisk server.
     */
    public final AsteriskServer getAsteriskServer()
    {
        return asteriskServer;
    }

    /**
     * Sets the Asterisk server.
     * 
     * @param asteriskServer the Asterisk server to set.
     */
    public final void setAsteriskServer(final AsteriskServer asteriskServer)
    {
        this.asteriskServer = asteriskServer;
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
    public final ChannelStateEnum getState()
    {
        return state;
    }

    /**
     * Sets the state of this channel.
     * 
     * @param state the state of this channel.
     */
    public final void setState(final ChannelStateEnum state)
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
    public List getExtensions()
    {
        List extensionsCopy;

        synchronized (extensions)
        {
            extensionsCopy = new ArrayList(extensions);
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
    public final Channel getLinkedChannel()
    {
        return linkedChannel;
    }

    /**
     * Sets the channel this channel is bridged with.
     * 
     * @param linkedChannel the channel this channel is bridged with.
     */
    public final void setLinkedChannel(final Channel linkedChannel)
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

    public String toString()
    {
        StringBuffer sb;
        Channel linkedChannel;
        int systemHashcode;

        sb = new StringBuffer(getClass().getName() + ": ");

        synchronized (this)
        {
            sb.append("id='" + getId() + "'; ");
            sb.append("name='" + getName() + "'; ");
            sb.append("callerId='" + getCallerId() + "'; ");
            sb.append("state='" + getState() + "'; ");
            sb.append("account='" + getAccount() + "'; ");
            sb.append("dateOfCreation=" + getDateOfCreation() + "; ");
            linkedChannel = this.linkedChannel;
            systemHashcode = System.identityHashCode(this);
        }
        if (linkedChannel == null)
        {
            sb.append("linkedChannel=null; ");
        }
        else
        {
            sb.append("linkedChannel=[");
            synchronized (linkedChannel)
            {
                sb.append(linkedChannel.getClass().getName() + ": ");
                sb.append("id='" + linkedChannel.getId() + "'; ");
                sb.append("name='" + linkedChannel.getName() + "'; ");
                sb.append("systemHashcode="
                        + System.identityHashCode(linkedChannel));
            }
            sb.append("]; ");
        }
        sb.append("systemHashcode=" + systemHashcode);

        return sb.toString();
    }
}
