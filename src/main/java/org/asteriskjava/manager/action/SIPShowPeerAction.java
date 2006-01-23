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
package org.asteriskjava.manager.action;

import org.asteriskjava.manager.event.PeerlistCompleteEvent;

/**
 * Retrieves a the details about a given SIP peer.<br>
 * For a PeerEntryEvent is sent by Asterisk containing the details of the peer
 * followed by a PeerlistCompleteEvent.<br>
 * Available since Asterisk 1.2
 * 
 * @see org.asteriskjava.manager.event.PeerEntryEvent
 * @see org.asteriskjava.manager.event.PeerlistCompleteEvent
 * @author srt
 * @version $Id: SIPShowPeerAction.java,v 1.3 2005/08/27 10:18:05 srt Exp $
 * @since 0.2
 */
public class SIPShowPeerAction extends AbstractManagerAction
        implements
            EventGeneratingAction
{
    /**
     * Serial version identifier
     */
    private static final long serialVersionUID = 921037572305993779L;
    private String peer;

    /**
     * Creates a new empty SIPShowPeerAction.
     */
    public SIPShowPeerAction()
    {

    }

    /**
     * Creates a new SIPShowPeerAction that requests the details about the given
     * SIP peer.
     * 
     * @param peer the name of the SIP peer to retrieve details for.
     * @since 0.2
     */
    public SIPShowPeerAction(String peer)
    {
        this.peer = peer;
    }

    public String getAction()
    {
        return "SIPShowPeer";
    }

    /**
     * Returns the name of the peer to retrieve.<br>
     * This parameter is mandatory.
     * 
     * @return the name of the peer to retrieve.
     */
    public String getPeer()
    {
        return peer;
    }

    /**
     * Sets the name of the peer to retrieve.<br>
     * This parameter is mandatory.
     * 
     * @param peer the name of the peer to retrieve.
     */
    public void setPeer(String peer)
    {
        this.peer = peer;
    }

    public Class getActionCompleteEventClass()
    {
        return PeerlistCompleteEvent.class;
    }
}
