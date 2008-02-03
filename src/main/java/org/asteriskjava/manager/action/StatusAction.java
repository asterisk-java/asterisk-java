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

import org.asteriskjava.manager.event.StatusCompleteEvent;

/**
 * The StatusAction requests the state of all active channels. Alternativly (as of Asterisk 1.6)
 * you can also pass a channel name to only retrive the status of one specific channel.<p>
 * For each active channel a StatusEvent is generated. After the state of all
 * channels has been reported a StatusCompleteEvent is generated.
 *
 * @author srt
 * @version $Id$
 * @see org.asteriskjava.manager.event.StatusEvent
 * @see org.asteriskjava.manager.event.StatusCompleteEvent
 */
public class StatusAction extends AbstractManagerAction implements EventGeneratingAction
{
    /**
     * Serializable version identifier.
     */
    static final long serialVersionUID = 0L;

    private String channel;

    /**
     * Creates a new StatusAction that retrieves the status of all channels.
     */
    public StatusAction()
    {

    }

    /**
     * Creates a new StatusAction that retrieves the status of the given channel.<p>
     * Available since Asterisk 1.6.
     *
     * @param channel name of the channel.
     * @since 1.0.0
     */
    public StatusAction(String channel)
    {
        this.channel = channel;
    }

    /**
     * Returns the name of this action, i.e. "Status".
     */
    @Override
    public String getAction()
    {
        return "Status";
    }

    public Class getActionCompleteEventClass()
    {
        return StatusCompleteEvent.class;
    }

    /**
     * Returns the name of the channel.
     *
     * @return the name of the channel or <code>null</code> for all channels.
     * @since 1.0.0
     */
    public String getChannel()
    {
        return channel;
    }

    /**
     * Sets the name of the channel.
     *
     * @param channel the name of the channel or <code>null</code> for all channels.
     * @since 1.0.0
     */
    public void setChannel(String channel)
    {
        this.channel = channel;
    }
}
