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
 * A MeetMeJoinEvent is triggered if a channel joins a MeetMe conference.
 * <p>
 * Channel and unqiueId properties for this event are available since Asterisk
 * 1.0.
 * <p>
 * It is implemented in <code>apps/app_meetme.c</code>
 *
 * @author srt
 * @version $Id$
 */
public class MeetMeJoinEvent extends AbstractMeetMeEvent
{
    /**
     * Serializable version identifier.
     */
    private static final long serialVersionUID = 0L;

    /**
     * @param source
     */
    public MeetMeJoinEvent(Object source)
    {
        super(source);
    }
}
