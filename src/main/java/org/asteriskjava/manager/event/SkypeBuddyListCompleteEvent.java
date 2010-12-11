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
 * A SkypeBuddyListCompleteEvent is triggered in response to a SkypeBuddiesAction when
 * all buddies have been reported.<p>
 * It is implemented in <code>chan_skype.c</code>.<p>
 * Available with Skype for Asterisk.
 *
 * @see org.asteriskjava.manager.action.SkypeBuddiesAction
 * @since 1.0.0
 */
public class SkypeBuddyListCompleteEvent extends ResponseEvent
{
    /**
     * Serial version identifier.
     */
    private static final long serialVersionUID = 1L;
    private Integer listItems;

    public SkypeBuddyListCompleteEvent(Object source)
    {
        super(source);
    }

    /**
     * Returns the number of buddies that have been reported.
     *
     * @return the number of buddies that have been reported.
     */
    public Integer getListItems()
    {
        return listItems;
    }

    /**
     * Sets the number of buddies that have been reported.
     *
     * @param listItems the number of buddies that have been reported.
     */
    public void setListItems(Integer listItems)
    {
        this.listItems = listItems;
    }
}
