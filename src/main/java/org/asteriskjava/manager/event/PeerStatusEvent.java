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
package org.asteriskjava.manager.event;

/**
 * A PeerStatusEvent is triggered when a SIP or IAX client attempts to registrer at this asterisk
 * server.<p>
 * This event is implemented in <code>channels/chan_iax2.c</code> and
 * <code>channels/chan_sip.c</code>
 * 
 * @author srt
 * @version $Id$
 */
public class PeerStatusEvent extends ManagerEvent
{
    /**
     * Serializable version identifier.
     */
    private static final long serialVersionUID = 8384939771592846892L;
    
    public static final String STATUS_REGISTERED = "Registered";
    public static final String STATUS_UNREGISTERED = "Unregistered";
    public static final String STATUS_REACHABLE = "Reachable";
    public static final String STATUS_LAGGED = "Lagged";
    public static final String STATUS_UNREACHABLE = "Unreachable";
    public static final String STATUS_REJECTED = "Rejected";

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
     * Returns the name of the peer that registered.<p>
     * The peer name includes the channel type prefix. So if you receive a PeerStatusEvent for a
     * SIP peer defined as "john" in <code>sip.conf</code> this method returns "SIP/john".
     * <p>
     * Peer names for IAX clients start with "IAX2/", peer names for SIP clients start with "SIP/".
     *
     * @return the peer's name including the channel type.
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
     * Returns the registration state.<p>
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
     * Returns the cause of a rejection or unregistration.<p>
     * For IAX peers this is set only if the status equals "Rejected".<p>
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
