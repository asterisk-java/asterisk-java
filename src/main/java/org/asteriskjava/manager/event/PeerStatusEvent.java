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
package org.asteriskjava.manager.event;

/**
 * A PeerStatusEvent is triggered when a SIP or IAX client attempts to registrer at this asterisk
 * server.<br>
 * This event is implemented in <code>channels/chan_iax2.c</code> and
 * <code>channels/chan_sip.c</code>
 * 
 * @author srt
 * @version $Id: PeerStatusEvent.java,v 1.3 2005/08/28 10:37:55 srt Exp $
 */
public class PeerStatusEvent extends ManagerEvent
{
    /**
     * Serializable version identifier
     */
    private static final long serialVersionUID = 8384939771592846892L;
    private String peer;
    private String peerStatus;
    private String cause;
    private Integer time;

    /**
     * @param source
     */
    public PeerStatusEvent(Object source)
    {
        super(source);
    }

    /**
     * Returns the name of the peer that registered. The peer's name starts with "IAX2/" if it is an
     * IAX client or "SIP/" if it is a SIP client. It is followed by the username that is used for
     * registration.
     */
    public String getPeer()
    {
        return peer;
    }

    /**
     * Sets the name of the peer that registered.
     */
    public void setPeer(String peer)
    {
        this.peer = peer;
    }

    /**
     * Returns the registration state.<br>
     * This may be one of
     * <ul>
     * <li>Registered</li>
     * <li>Unregistered</li>
     * <li>Reachable</li>
     * <li>Lagged</li>
     * <li>Unreachable</li>
     * <li>Rejected (IAX only)</li>
     * </ul>
     */
    public String getPeerStatus()
    {
        return peerStatus;
    }

    /**
     * Sets the registration state.
     */
    public void setPeerStatus(String peerStatus)
    {
        this.peerStatus = peerStatus;
    }

    /**
     * Returns the cause of a rejection or unregistration.<br>
     * For IAX peers this is set only if the status equals "Rejected".<br>
     * For SIP peers this is set if the status equals "Unregistered" and the peer was unregistered
     * due to an expiration. In that case the cause is set to "Expired".
     */
    public String getCause()
    {
        return cause;
    }

    /**
     * Sets the cause of the rejection or unregistration.
     */
    public void setCause(String cause)
    {
        this.cause = cause;
    }

    /**
     * Returns the ping time of the client if status equals "Reachable" or "Lagged"; if the status
     * equals "Unreachable" it returns how long the last response took (in ms) for IAX peers or -1
     * for SIP peers.
     */
    public Integer getTime()
    {
        return time;
    }

    public void setTime(Integer time)
    {
        this.time = time;
    }
}
