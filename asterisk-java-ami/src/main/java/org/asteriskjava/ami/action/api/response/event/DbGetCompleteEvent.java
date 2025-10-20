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
package org.asteriskjava.ami.action.api.response.event;

import org.asteriskjava.ami.action.api.response.EventList;

/**
 * @author Piotr Olaszewski
 * @since 4.0.0
 */
public class DbGetCompleteEvent extends ResponseEvent {
    private EventList eventList;
    private int listItems;

    public DbGetCompleteEvent() {
        super(null);
    }

    public DbGetCompleteEvent(Object object) {
        super(null);
    }

    public EventList getEventList() {
        return eventList;
    }

    public void setEventList(EventList eventList) {
        this.eventList = eventList;
    }

    public int getListItems() {
        return listItems;
    }

    public void setListItems(int listItems) {
        this.listItems = listItems;
    }
}
