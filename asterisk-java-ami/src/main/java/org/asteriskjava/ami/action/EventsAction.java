/*
 * Copyright 2004-2023 Asterisk Java contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.asteriskjava.ami.action;

import org.asteriskjava.ami.action.annotation.ExpectedResponse;
import org.asteriskjava.ami.action.response.EventsActionResponse;

import java.io.Serial;
import java.util.EnumSet;

/**
 * Control Event Flow.
 * <p>
 * Enable/Disable sending of events to this manager client.
 * <p>
 * Supported Asterisk versions:
 * <ul>
 *     <li>18 - <a href="https://docs.asterisk.org/Asterisk_18_Documentation/API_Documentation/AMI_Actions/Events/">Events</a></li>
 *     <li>20 - <a href="https://docs.asterisk.org/Asterisk_20_Documentation/API_Documentation/AMI_Actions/Events/">Events</a></li>
 * </ul>
 *
 * @author Stefan Reuter
 * @since 1.0.0
 */
@ExpectedResponse(EventsActionResponse.class)
public class EventsAction extends AbstractManagerAction {
    @Serial
    private static final long serialVersionUID = -8042435402644984875L;

    private EnumSet<EventMask> eventMask;

    @Override
    public String getAction() {
        return "Events";
    }

    public EnumSet<EventMask> getEventMask() {
        return eventMask;
    }

    public void setEventMask(EnumSet<EventMask> eventMask) {
        this.eventMask = eventMask;
    }
}
