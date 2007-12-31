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
 * Abstract base class providing common properties for HangupEvent, NewChannelEvent and
 * NewStateEvent.
 * 
 * @author srt
 * @version $Id$
 */
public abstract class AbstractChannelStateEvent extends AbstractChannelEvent
{
    /**
     * Serializable version identifier
     */
    static final long serialVersionUID = 5906599407896179295L;

    /**
     * The state of the channel.
     */
    private String state;

    /**
     * @param source
     */
    protected AbstractChannelStateEvent(Object source)
    {
        super(source);
    }

    /**
     * Returns the (new) state of the channel.<p>
     * The following states are used:<p>
     * <ul>
     * <li>Down</li>
     * <li>OffHook</li>
     * <li>Dialing</li>
     * <li>Ring</li>
     * <li>Ringing</li>
     * <li>Up</li>
     * <li>Busy</li>
     * <ul>
     */
    public String getState()
    {
        return state;
    }

    /**
     * Sets the (new) state of the channel.
     */
    public void setState(String state)
    {
        this.state = state;
    }
}
