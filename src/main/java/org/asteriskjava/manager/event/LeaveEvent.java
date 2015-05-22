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
 * A LeaveEvent is triggered when a channel leaves a queue.<p>
 * It is implemented in <code>apps/app_queue.c</code>
 * 
 * @author srt
 */
public class LeaveEvent extends QueueEvent
{
    /**
     * Serializable version identifier
     */
    static final long serialVersionUID = -7450401017732634240L;

    private Integer position;

    /**
     * @param source
     */
    public LeaveEvent(Object source)
    {
        super(source);
    }

    /**
     * @return the position of the caller at the time they leave the queue
     */
    public Integer getPosition()
    {
        return position;
    }

    public void setPosition(Integer position)
    {
        this.position = position;
    }
}
