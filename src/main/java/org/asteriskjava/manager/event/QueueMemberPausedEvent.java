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
 * A QueueMemberPausedEvent is triggered when a queue member is paused or
 * unpaused.<br>
 * It is implemented in <code>apps/app_queue.c</code>.<br>
 * Available since Asterisk 1.2
 * 
 * @author srt
 * @version $Id$
 * @since 0.2
 */
public class QueueMemberPausedEvent extends AbstractQueueMemberEvent
{
    /**
     * Serial version identifier.
     */
    private static final long serialVersionUID = 2108033737226142194L;

    private Boolean paused;

    public QueueMemberPausedEvent(Object source)
    {
        super(source);
    }

    /**
     * Returns if this queue member is paused (not accepting calls).<br>
     * 
     * @return <code>Boolean.TRUE</code> if this member has been paused or
     *         <code>Boolean.FALSE</code> if not.
     */
    public Boolean getPaused()
    {
        return paused;
    }

    /**
     * Sets if this member is paused.
     * 
     * @param paused <code>Boolean.TRUE</code> if this member has been paused
     *            or <code>Boolean.FALSE</code> if not.
     */
    public void setPaused(Boolean paused)
    {
        this.paused = paused;
    }
}
