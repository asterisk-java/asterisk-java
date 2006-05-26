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
package org.asteriskjava.live.internal;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.asteriskjava.live.AsteriskChannel;
import org.asteriskjava.live.CallerId;
import org.asteriskjava.live.ChannelState;
import org.asteriskjava.live.Extension;
import org.asteriskjava.live.HangupCause;
import org.asteriskjava.live.ManagerCommunicationException;
import org.asteriskjava.live.NoSuchChannelException;
import org.asteriskjava.manager.action.GetVarAction;
import org.asteriskjava.manager.action.HangupAction;
import org.asteriskjava.manager.action.PlayDtmfAction;
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
class AsteriskChannelImpl extends AbstractLiveObject implements AsteriskChannel
{
    private static final String CAUSE_VARIABLE_NAME = "PRI_CAUSE";

    /**
     * Unique id of this channel.
     */
    private final String id;

    /**
     * Date this channel has been created.
     */
    private final Date dateOfCreation;
    
    /**
     * Date this channel has left the Asterisk server.
     */
    private Date dateOfRemoval;

    /**
     * Name of this channel.
     */
    private String name;

    /**
     * Caller*ID of this channel.
     */
    private CallerId callerId;

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
     * MeetMe room user associated with this channel if any, <code>null</code> otherwise.
     */
    private MeetMeUserImpl meetMeUserImpl;

    /**
     * Creates a new Channel.
     * 
     * @param name name of this channel, for example "SIP/1310-20da".
     * @param id unique id of this channel, for example "1099015093.165".
     */
    AsteriskChannelImpl(final AsteriskServerImpl server, final String name, final String id, final Date dateOfCreation)
    {
        super(server);
        this.name = name;
        this.id = id;
        this.dateOfCreation = dateOfCreation;
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
    void setName(final String name)
    {
        String oldName = this.name;

        this.name = name;
        firePropertyChange(PROPERTY_NAME, oldName, name);
    }

    public CallerId getCallerId()
    {
        return callerId;
    }

    /**
     * Sets the caller id number of this channel.
     * 
     * @param callerIdNumber the caller id number of this channel.
     */
    void setCallerId(final CallerId callerId)
    {
        CallerId oldCallerId = this.callerId;

        this.callerId = callerId;
        firePropertyChange(PROPERTY_CALLER_ID, oldCallerId, callerId);
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
        firePropertyChange(PROPERTY_STATE, oldState, state);
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
        firePropertyChange(PROPERTY_ACCOUNT, oldAccount, account);
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
        
        firePropertyChange(PROPERTY_CURRENT_EXTENSION, oldCurrentExtension, extension);
    }

    public Date getDateOfCreation()
    {
        return dateOfCreation;
    }

    public Date getDateOfRemoval()
    {
        return dateOfRemoval;
    }

    public HangupCause getHangupCause()
    {
        return hangupCause;
    }

    public String getHangupCauseText()
    {
        return hangupCauseText;
    }

    /**
     * Sets dateOfRemoval, hangupCause and hangupCauseText and changes state to
     * {@link ChannelState#HUNGUP}. Fires a PropertyChangeEvent for state.
     * 
     * @param dateOfRemoval date the channel was hung up
     * @param hangupCause cause for hangup
     * @param hangupCauseText textual representation of hangup cause
     */
    void hungup(Date dateOfRemoval, HangupCause hangupCause, String hangupCauseText)
    {
        this.dateOfRemoval = dateOfRemoval;
        this.hangupCause = hangupCause;
        this.hangupCauseText = hangupCauseText;
        // update state and fire PropertyChangeEvent
        setState(ChannelState.HUNGUP);
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
        firePropertyChange(PROPERTY_LINKED_CHANNEL, oldLinkedChannel, linkedChannel);
    }

    public boolean wasLinked()
    {
        return wasLinked;
    }

    MeetMeUserImpl getMeetMeUserImpl()
    {
        return meetMeUserImpl;
    }

    void setMeetMeUserImpl(MeetMeUserImpl meetMeRoomUserImpl)
    {
        this.meetMeUserImpl = meetMeRoomUserImpl;
    }

    // action methods

    public void hangup() throws ManagerCommunicationException, NoSuchChannelException
    {
        ManagerResponse response;

        response = server.sendAction(new HangupAction(name));
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

        response = server.sendAction(new RedirectAction(name, context, exten, priority));
        if (response instanceof ManagerError)
        {
            throw new NoSuchChannelException("Channel '" + name + "' is not available: " + response.getMessage());
        }
    }

    public void redirectBothLegs(String context, String exten, int priority) throws ManagerCommunicationException, NoSuchChannelException
    {
        ManagerResponse response;

        if (linkedChannel != null)
        {
            response = server.sendAction(new RedirectAction(name, linkedChannel.getName(), context, exten, priority, context, exten, priority));
        }
        else
        {
            response = server.sendAction(new RedirectAction(name, context, exten, priority));
        }

        if (response instanceof ManagerError)
        {
            throw new NoSuchChannelException("Channel '" + name + "' is not available: " + response.getMessage());
        }
    }

    public String getVariable(String variable) throws ManagerCommunicationException, NoSuchChannelException
    {
        ManagerResponse response;
        String value;

        response = server.sendAction(new GetVarAction(name, variable));
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

        response = server.sendAction(new SetVarAction(name, variable, value));
        if (response instanceof ManagerError)
        {
            throw new NoSuchChannelException("Channel '" + name + "' is not available: " + response.getMessage());
        }
    }

    public void playDtmf(String digit) throws ManagerCommunicationException, NoSuchChannelException, IllegalArgumentException
    {
        ManagerResponse response;

        if (digit == null)
        {
            throw new IllegalArgumentException("DTMF digit to send must not be null");
        }

        response = server.sendAction(new PlayDtmfAction(name, digit));
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

        sb = new StringBuffer("AsteriskChannel[");

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
