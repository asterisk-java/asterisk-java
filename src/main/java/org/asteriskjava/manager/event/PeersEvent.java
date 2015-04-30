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

import java.util.List;

/**
 * A PeersEvent is triggered in response to a {@link org.asteriskjava.manager.action.SipPeersAction},
 * {@link org.asteriskjava.manager.action.SipShowPeerAction} or {@link org.asteriskjava.manager.action.IaxPeerListAction}
 * and contains information about a SIP or IAX peers.<p>
 * It is implemented in <code>channels/chan_sip.c</code> and <code>channels/chan_iax.c</code>
 *
 * @author ddpaul
 * @version $Id$
 * @since 0.2
 */
public class PeersEvent extends ResponseEvent
{
    /**
     * Serial version identifier.
     */
    private static final long serialVersionUID = 0L;

    /**
     * This field holds actual information about SIP/IAX peers
     */
    private List<PeerEntryEvent> childEvents;

    /**
     * Creates a new instance.
     *
     * @param source
     */
    public PeersEvent( Object source )
    {
        super(source);
    }

    public List<PeerEntryEvent> getChildEvents()
    {
        return childEvents;
    }

    public void setChildEvents( List<PeerEntryEvent> childEvents )
    {
        this.childEvents = childEvents;
    }
}
