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
 * A StatusCompleteEvent is triggered after the state of all channels has been
 * reported in response to a StatusAction.
 * 
 * @see org.asteriskjava.manager.action.StatusAction
 * @see org.asteriskjava.manager.event.StatusEvent
 * @author srt
 * @version $Id$
 */
public class StatusCompleteEvent extends ResponseEvent
{
    /**
     * Serial version identifier.
     */
    private static final long serialVersionUID = 0L;
    private Integer items;

    private Integer listItems;
    private String eventList;

    public StatusCompleteEvent(Object source)
    {
        super(source);
    }

    /**
     * Returns the number of channels reported.
     * <p>
     * Available since Asterisk 1.6.
     *
     * @return the number of channels reported.
     * @since 1.0.0
     */
    public Integer getItems()
    {
        return items;
    }

    /**
     * Sets the number of channels reported.
     * <p>
     * Available since Asterisk 1.6.
     *
     * @param items the number of channels reported.
     * @since 1.0.0
     */
    public void setItems(Integer items)
    {
        this.items = items;
    }

    public Integer getListItems()
    {
        return listItems;
    }

    public void setListitems(Integer listItems)
    {
        this.listItems = listItems;
    }

    public String getEventList()
    {
        return eventList;
    }

    public void setEventList(String eventList)
    {
        this.eventList = eventList;
    }

    public void setListItems(Integer listItems)
    {
        this.listItems = listItems;
    }
 
}
