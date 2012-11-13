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
package org.asteriskjava.manager.action;

import org.asteriskjava.manager.event.PeerlistCompleteEvent;
import org.asteriskjava.manager.event.ResponseEvent;

/**
 * Retrieves a list of all defined SIP peers.<p>
 * For each peer that is found a PeerEntryEvent is sent by Asterisk containing
 * the details. When all peers have been reported a PeerlistCompleteEvent is
 * sent.<p>
 * Available since Asterisk 1.2
 * 
 * Permission required: write=system
 *
 * @author srt
 * @version $Id$
 * @see org.asteriskjava.manager.event.PeerEntryEvent
 * @see org.asteriskjava.manager.event.PeerlistCompleteEvent
 * @since 0.2
 */
public class SipPeersAction extends AbstractManagerAction implements EventGeneratingAction
{
    /**
     * Serial version identifier.
     */
    private static final long serialVersionUID = 921037572305993779L;

    /**
     * Creates a new SipPeersAction.
     */
    public SipPeersAction()
    {

    }

    @Override
    public String getAction()
    {
        return "SIPPeers";
    }

    public Class<? extends ResponseEvent> getActionCompleteEventClass()
    {
        return PeerlistCompleteEvent.class;
    }
}
