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
 * A DahdiShowChannelsCompleteEvent is triggered after the state of all Dahdi channels has been reported
 * in response to a DahdiShowChannelsAction.
 * 
 * @see org.asteriskjava.manager.action.DahdiShowChannelsAction
 * @see org.asteriskjava.manager.event.DahdiShowChannelsEvent
 * 
 * @author srt
 * @version $Id$
 */
public class DahdiShowChannelsCompleteEvent extends ResponseEvent
{
    /**
     * Serial version identifier
     */
    private static final long serialVersionUID = 6323249250335885462L;

    private Integer listitems;
    private String eventlist;
    private Integer items;

    public Integer getItems() {
        return items;
    }

    public void setItems(Integer items) {
        this.items = items;
    }
    
    /**
     * @param source
     */
    public DahdiShowChannelsCompleteEvent(Object source)
    {
        super(source);
    }

      /**
     * Returns if the status of the eventlist (should be Complete).<p>
     *
     * @return the status of the list.
     * @since 1.0.0
     */
    public String getEventlist()
    {
        return eventlist;
    }

    public void setEventlist(String eventlist)
    {
        this.eventlist = eventlist;
    }

    /**
     * Returns the number of channels reported.<p>
     *
     * @return the number of channels reported.
     */
    public Integer getListitems()
    {
        return listitems;
    }

    /**
     * Sets the number of channels reported.<p>
     *
     * @param items the number of channels reported.
     */
    public void setListitems(Integer listitems)
    {
        this.listitems = listitems;
    }

}
