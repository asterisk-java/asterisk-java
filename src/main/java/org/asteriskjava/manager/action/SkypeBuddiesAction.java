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
package org.asteriskjava.manager.action;

import org.asteriskjava.manager.event.ResponseEvent;
import org.asteriskjava.manager.event.SkypeBuddyListCompleteEvent;

/**
 * The SkypeBuddiesAction retrieves the buddy list of a Skype for Asterisk user
 * including the full name and status of each buddy.<p>
 * For each agent a SkypeBuddyEntryEvent is generated. After the full buddy list has been
 * reported a SkypeBuddyListCompleteEvent is generated.<p>
 * Available with Skype for Asterisk.
 *
 * @see org.asteriskjava.manager.event.SkypeBuddyEntryEvent
 * @see org.asteriskjava.manager.event.SkypeBuddyListCompleteEvent
 * @since 1.0.0
 */
public class SkypeBuddiesAction extends AbstractManagerAction implements EventGeneratingAction
{
    /**
     * Serializable version identifier
     */
    static final long serialVersionUID = 1L;
    private String user;

    /**
     * Creates a new SkypeBuddiesAction.
     */
    public SkypeBuddiesAction()
    {

    }

    /**
     * Creates a new SkypeBuddiesAction that retrieves the buddy list for the given user.
     *
     * @param user the Skype username of the user to retrieve the buddy list for.
     */
    public SkypeBuddiesAction(String user)
    {
        this.user = user;
    }

    /**
     * Returns the Skype username of the user to retrieve the buddy list for.<p>
     * This property is mandatory.
     *
     * @return the Skype username of the user to retrieve the buddy list for.
     */
    public String getUser()
    {
        return user;
    }

    /**
     * Sets the Skype username of the user to retrieve the buddy list for.
     *
     * @param user the Skype username of the user to retrieve the buddy list for.
     */
    public void setUser(String user)
    {
        this.user = user;
    }

    /**
     * Returns the name of this action, i.e. "SkypeBuddies".
     */
    @Override
    public String getAction()
    {
        return "SkypeBuddies";
    }

    public Class<? extends ResponseEvent> getActionCompleteEventClass()
    {
        return SkypeBuddyListCompleteEvent.class;
    }
}
