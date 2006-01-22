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
package net.sf.asterisk.manager.event;

import java.io.Serializable;
import java.util.Date;
import java.util.EventObject;

/**
 * Abstract base class for all Events that can be received from the Asterisk
 * server.<br>
 * Events contain data pertaining to an event generated from within the Asterisk
 * core or an extension module.<br>
 * There is one conrete subclass of ManagerEvent per each supported Asterisk
 * Event.
 * 
 * @author srt
 * @version $Id: ManagerEvent.java,v 1.5 2005/05/21 23:06:04 srt Exp $
 */
public abstract class ManagerEvent extends EventObject implements Serializable
{
    /**
     * Serializable version identifier
     */
    static final long serialVersionUID = 4299374743315152040L;

    /**
     * AMI authorization class.
     */
    private String privilege;
    
    /**
     * The point in time this event has been received from the Asterisk
     * server.
     */
    private Date dateReceived;

    /**
     * @param source
     */
    public ManagerEvent(Object source)
    {
        super(source);

    }

    /**
     * Returns the point in time this event was received from the Asterisk
     * server.<br>
     * Pseudo events that are not directly received from the asterisk server
     * (for example ConnectEvent and DisconnectEvent) may return
     * <code>null</code>.
     */
    public Date getDateReceived()
    {
        return dateReceived;
    }

    /**
     * Sets the point in time this event was received from the asterisk server.
     */
    public void setDateReceived(Date dateReceived)
    {
        this.dateReceived = dateReceived;
    }

    /**
     * Returns the AMI authorization class of this event.<br>
     * This is one or more of system, call, log, verbose, command, agent or user.
     * Multiple privileges are separated by comma.<br>
     * Note: This property is not available from Asterisk 1.0 servers.
     * @since 0.2
     */
    public String getPrivilege()
    {
        return privilege;
    }

    /**
     * Sets the AMI authorization class of this event.
     * @since 0.2
     */
    public void setPrivilege(String privilege)
    {
        this.privilege = privilege;
    }

    public String toString()
    {
        StringBuffer sb;

        sb = new StringBuffer(getClass().getName() + ": ");
        sb.append("dateReceived=" + getDateReceived() + "; ");
        if (getPrivilege() != null)
        {
            sb.append("privilege=" + getPrivilege() + "; ");
        }
        // TODO print attributes
        sb.append("systemHashcode=" + System.identityHashCode(this));

        return sb.toString();
    }
}
