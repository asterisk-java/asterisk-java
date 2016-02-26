/*
 *  Copyright 2004-2007 Stefan Reuter and others
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
 * A QueueCallerJoinEvent is triggered when a caller join a queue before
 * getting connected with an agent.
 * <p>
 * It is implemented in <code>apps/app_queue.c</code>
 * <p>
 * Available since Asterisk 1.4.
 *
 * @author Leonardo de Souza
 */
public class QueueCallerJoinEvent extends QueueEvent
{

    /**
     * Serializable version identifier
     */
    private static final long serialVersionUID = 812069706662063871L;

    private Integer position;

    /**
     * @param source
     */
    public QueueCallerJoinEvent(Object source)
    {
        super(source);
    }

    /**
     * @return the position of the caller at the time they abandoned the queue
     */
    public Integer getPosition()
    {
        return position;
    }

    /**
     * @param position the position of the caller at the time they abandoned the
     *            queue
     */
    public void setPosition(Integer position)
    {
        this.position = position;
    }
}
