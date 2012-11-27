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
 * A HangupEvent is triggered when a channel is hung up.<p>
 * It is implemented in <code>channel.c</code>
 */
public class SoftHangupRequestEvent extends AbstractChannelEvent
{
    /**
     * Serializable version identifier.
     */
    static final long serialVersionUID = 0L;

    private Integer cause;

    public SoftHangupRequestEvent(Object source)
    {
        super(source);
    }

    /**
     * Returns the cause of the hangup.
     *
     * @return the hangup cause.
     * @see org.asteriskjava.live.HangupCause
     */
    public Integer getCause()
    {
        return cause;
    }

    /**
     * Sets the cause of the hangup.
     *
     * @param cause the hangup cause.
     */
    public void setCause(Integer cause)
    {
        this.cause = cause;
    }
}
