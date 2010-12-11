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
 * A SkypeBuddyEntryEvent is triggered in response to a SkypeBuddiesAction for each
 * buddy on the buddy list.<p>
 * It is implemented in <code>chan_skype.c</code>.<p>
 * Available with Skype for Asterisk.
 *
 * @see org.asteriskjava.manager.action.SkypeBuddiesAction
 * @since 1.0.0
 */
public class SkypeBuddyEntryEvent extends ResponseEvent
{
    /**
     * Serial version identifier.
     */
    private static final long serialVersionUID = 1L;
    private String buddy;
    private String status;
    private String fullname;

    public SkypeBuddyEntryEvent(Object source)
    {
        super(source);
    }

    /**
     * Returns the Skype username of the buddy.
     *
     * @return the Skype username of the buddy.
     */
    public String getBuddy()
    {
        return buddy;
    }

    /**
     * Sets the Skype username of the buddy.
     *
     * @param buddy the Skype username of the buddy.
     */
    public void setBuddy(String buddy)
    {
        this.buddy = buddy;
    }

    /**
     * Returns the status of the buddy.
     *
     * @return the status of the buddy.
     */
    public String getStatus()
    {
        return status;
    }

    /**
     * Sets the status of the buddy.
     *
     * @param status the status of the buddy.
     */
    public void setStatus(String status)
    {
        this.status = status;
    }

    /**
     * Returns the full name of the buddy.
     *
     * @return the full name of the buddy.
     */
    public String getFullname()
    {
        return fullname;
    }

    /**
     * Sets the full name of the buddy.
     *
     * @param fullname the full name of the buddy.
     */
    public void setFullname(String fullname)
    {
        this.fullname = fullname;
    }
}
