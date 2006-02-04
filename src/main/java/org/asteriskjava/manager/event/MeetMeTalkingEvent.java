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
 * A MeetMeTalkingEvent is triggered when a user starts talking in a meet me
 * conference.<br>
 * It is implemented in <code>apps/app_meetme.c</code><br>
 * Available since Asterisk 1.2
 * 
 * @author srt
 * @version $Id: MeetMeTalkingEvent.java,v 1.1 2005/08/27 03:15:32 srt Exp $
 * @since 0.2
 */
public class MeetMeTalkingEvent extends MeetMeEvent
{
    /**
     * Serializable version identifier
     */
    private static final long serialVersionUID = -8554403451985143184L;

    /**
     * @param source
     */
    public MeetMeTalkingEvent(Object source)
    {
        super(source);
    }
}
