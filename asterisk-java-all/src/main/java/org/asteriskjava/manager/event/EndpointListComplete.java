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
 * An EndpointListComplete event is triggered after the details of all end points have been
 * reported in response to a PJSIPShowEndpoints event.
 * <p>
 * Available since Asterisk 12
 *
 * @author srt
 * @version $Id$
 * @see org.asteriskjava.manager.event.PJSipShowEndpoints
 * @since 12
 */
public class EndpointListComplete extends ResponseEvent {
    /**
     * Serial version identifier.
     */
    private static final long serialVersionUID = -1177773673509373296L;
    private Integer listItems;
    private String eventList;

    /**
     * Creates a new instance.
     *
     * @param source
     */
    public EndpointListComplete(Object source) {
        super(source);
    }

    /**
     * Returns the number of Endpoints that have been reported.
     *
     * @return the number of Endpoints that have been reported.
     */
    public Integer getListItems() {
        return listItems;
    }

    /**
     * Sets the number of Endpoints that have been reported.
     *
     * @param listItems the number of PeerEvents that have been reported.
     */
    public void setListItems(Integer listItems) {
        this.listItems = listItems;
    }

    /**
     * Returns always "Complete".
     * <p>
     * Available since Asterisk 1.6.
     *
     * @return always returns "Complete" confirming that all PeerEntry events have
     * been sent.
     * @since 1.0.0
     */
    public String getEventList() {
        return eventList;
    }

    public void setEventList(String eventList) {
        this.eventList = eventList;
    }
}
