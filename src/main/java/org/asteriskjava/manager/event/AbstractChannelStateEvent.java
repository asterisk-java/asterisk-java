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
 * Abstract base class providing common properties for HangupEvent,
 * NewChannelEvent and NewStateEvent.
 *
 * @author srt
 * @version $Id$
 */
public abstract class AbstractChannelStateEvent extends AbstractChannelEvent
{
    /**
     * Serializable version identifier.
     */
    static final long serialVersionUID = 0L;

    protected AbstractChannelStateEvent(Object source)
    {
        super(source);
    }

    /**
     * Returns the new state of the channel as a descriptive text. <br>
     * This is an alias for {@link #getChannelStateDesc()}.
     *
     * @return the new state of the channel as a descriptive text.
     * @deprecated as of 1.0.0, use {@link #getChannelStateDesc()} instead or
     *             even better switch to numeric values as returned by
     *             {@link #getChannelState()}.
     */
    @Deprecated
    public String getState()
    {
        return getChannelStateDesc();
    }

    /**
     * Sets the new state of the channel as a descriptive text.
     * <p>
     * The state property is used by Asterisk versions prior to 1.6.
     *
     * @param state the new state of the channel as a descriptive text.
     */
    public void setState(String state)
    {
        setChannelStateDesc(state);
    }
}
