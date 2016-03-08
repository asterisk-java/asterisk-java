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
 * A HoldEvent is triggered when a channel is put on hold (or no longer on
 * hold).
 * <p>
 * It is implemented in <code>channels/chan_sip.c</code>.
 * <p>
 * Available since Asterisk 1.2 for SIP channels, as of Asterisk 1.6 this event
 * is also supported for IAX2 channels.
 * <p>
 * To receive HoldEvents for SIP channels you must set
 * <code>callevents = yes</code> in <code>sip.conf</code>.
 *
 * @author srt
 * @version $Id$
 * @since 0.2
 */
public class HoldEvent extends AbstractHoldEvent
{
    /**
     * Serializable version identifier.
     */
    private static final long serialVersionUID = 0L;

    private String musicClass;
    
    /**
     * Creates a new HoldEvent.
     *
     * @param source
     */
    public HoldEvent(Object source)
    {
        super(source);
        /*
         * Asterisk prior to 1.6 and after 11 uses Hold and Unhold events
         * instead of the status So we set the status to true in the Hold event
         * and to false in Unhold. For Asterisk 1.6-11 this is overridden by the
         * status property received with the hold event.
         */
        setStatus(true);
    }

    public String getMusicClass()
    {
        return musicClass;
    }

    public void setMusicClass(String musicClass)
    {
        this.musicClass = musicClass;
    }
    
}
