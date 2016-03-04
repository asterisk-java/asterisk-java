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
 * An AgentsCompleteEvent is triggered after the state of all agents has been
 * reported in response to an AgentsAction.<p>
 * Available since Asterisk 1.2
 * 
 * @see org.asteriskjava.manager.action.AgentsAction
 * @author srt
 * @version $Id$
 * @since 0.2
 */
public class AgentsCompleteEvent extends ResponseEvent
{
    /**
     * Serial version identifier
     */
    private static final long serialVersionUID = -1177773673509373296L;

    private Integer listItems;
    private String eventList;
    
    /**
     * @param source
     */
    public AgentsCompleteEvent(Object source)
    {
        super(source);
    }

    public Integer getListItems()
    {
        return listItems;
    }

    public void setListItems(Integer listItems)
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
  
}
