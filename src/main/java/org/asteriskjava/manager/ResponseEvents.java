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
package org.asteriskjava.manager;

import org.asteriskjava.ami.action.api.response.ManagerActionResponse;
import org.asteriskjava.ami.action.api.response.event.ResponseEvent;
import org.asteriskjava.ami.event.api.ManagerEvent;

import java.util.Collection;


/**
 * Contains the result of executing an
 * {@link org.asteriskjava.manager.action.EventGeneratingAction}, that is the
 * {@link ManagerActionResponse} and any received
 * {@link ManagerEvent}s.
 *
 * @author srt
 * @version $Id$
 * @see org.asteriskjava.manager.action.EventGeneratingAction
 * @since 0.2
 */
public interface ResponseEvents {
    /**
     * Returns the response received.
     *
     * @return the response received.
     */
    ManagerActionResponse getResponse();

    /**
     * Returns a Collection of ManagerEvents that have been received including
     * the last one that indicates completion.
     *
     * @return a Collection of ManagerEvents received.
     */
    Collection<ResponseEvent> getEvents();
}
