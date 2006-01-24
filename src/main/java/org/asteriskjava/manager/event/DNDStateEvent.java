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
 * A DNDStateEvent is triggered by the Zap channel driver when a channel enters
 * or leaves DND (do not disturb) state.<br>
 * It is implemented in <code>channels/chan_zap.c</code>.<br>
 * Available since Asterisk 1.2
 * 
 * @author srt
 * @version $Id: DNDStateEvent.java,v 1.3 2005/08/28 12:20:53 srt Exp $
 * @since 0.2
 */
public class DNDStateEvent extends ManagerEvent
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
     * The DND state of the channel.
     */
    private String state;

    /**
     * Creates a new DNDStateEvent.
     * 
     * @param source
     */
    public DNDStateEvent(Object source)
    {
        super(source);
    }

    /**
     * Returns the name of the channel. The channel name is of the form
     * "Zap/&lt;channel number&gt;".
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
     * Returns DND state of the channel.
     * 
     * @return "enabled" if do not disturb is on, "disabled" if it is off.
     */
    public String getState()
    {
        return state;
    }

    /**
     * Sets the DND state of the channel.
     */
    public void setState(String state)
    {
        this.state = state;
    }
}
