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

import org.asteriskjava.manager.AsteriskMapping;

/**
 * A MeetMeTalkRequestEvent is triggered when a muted user requests talking in
 * a MeetMe conference.
 *
 * @author srt
 * @version $Id$
 * @since 1.0.0
 */
@AsteriskMapping("MeetMeTalkRequest")
public class MeetMeTalkingRequestEvent extends AbstractMeetMeEvent {
    /**
     * Serializable version identifier.
     */
    private static final long serialVersionUID = 1L;

    private Boolean status;
    private Integer duration;

    /**
     * Constructs a MeetMeTalkingRequestEvent
     *
     * @param source The object on which the Event initially occurred.
     * @throws IllegalArgumentException if source is null.
     */
    public MeetMeTalkingRequestEvent(Object source) {
        super(source);
    }

    // see http://bugs.digium.com/view.php?id=9418

    /**
     * The length of time (in seconds) that the MeetMe user has been in the
     * conference at the time of this event
     *
     * @return the number of seconds this user has been in the conference.
     */
    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    /**
     * Returns whether the user has started or stopped requesting talking
     *
     * @return {@code true} if the user has started requesting talking,
     * {@code false} if the user has stopped requesting talking.
     */
    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
