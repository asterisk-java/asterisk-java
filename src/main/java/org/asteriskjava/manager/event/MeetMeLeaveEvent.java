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
 * A MeetMeLeaveEvent is triggered if a channel leaves a MeetMe conference.<br>
 * It is implemented in <code>apps/app_meetme.c</code>
 * 
 * @author srt
 * @version $Id$
 */
public class MeetMeLeaveEvent extends MeetMeEvent
{
    /**
     * Serializable version identifier
     */
    private static final long serialVersionUID = 7692361610793036224L;

    private String channel;
    private String uniqueId;

    /**
     * @param source
     */
    public MeetMeLeaveEvent(Object source)
    {
        super(source);
    }
    
    /**
     * Returns the name of the channel that left the conference.
     */
    public String getChannel()
    {
        return channel;
    }

    /**
     * Sets the name of the channel that left the conference.
     */
    public void setChannel(String channel)
    {
        this.channel = channel;
    }

    /**
     * Returns the unique id of the channel that left the conference.
     */
    public String getUniqueId()
    {
        return uniqueId;
    }

    /**
     * Sets the unique id of the channel that left the conference.
     */
    public void setUniqueId(String uniqueId)
    {
        this.uniqueId = uniqueId;
    }
}
