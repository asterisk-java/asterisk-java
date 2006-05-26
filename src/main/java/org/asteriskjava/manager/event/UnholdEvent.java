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
 * An UnholdEvent is triggered by the SIP channel driver when a channel is no
 * longer put on hold.<p>
 * It is implemented in <code>channels/chan_sip.c</code>.<p>
 * Available since Asterisk 1.2
 * 
 * @see org.asteriskjava.manager.event.HoldEvent
 * @author srt
 * @version $Id$
 * @since 0.2
 */
public class UnholdEvent extends ManagerEvent
{
    /**
     * Serializable version identifier
     */
    static final long serialVersionUID = 5906599407896179295L;

    /**
     * The name of the channel.
     */
    private String channel;

    /**
     * The unique id of the channel.
     */
    private String uniqueId;

    /**
     * Creates a new UnholdEvent.
     * 
     * @param source
     */
    public UnholdEvent(Object source)
    {
        super(source);
    }

    /**
     * Returns the name of the channel.
     */
    public String getChannel()
    {
        return channel;
    }

    /**
     * Sets the name of the channel.
     */
    public void setChannel(String channel)
    {
        this.channel = channel;
    }

    /**
     * Returns the unique id of the channel.
     */
    public String getUniqueId()
    {
        return uniqueId;
    }

    /**
     * Sets the unique id of the channel.
     */
    public void setUniqueId(String uniqueId)
    {
        this.uniqueId = uniqueId;
    }
}
