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

import org.asteriskjava.manager.ExpectedResponse;
import org.asteriskjava.manager.response.SkypeBuddyResponse;

/**
 * The SkypeBuddyAction retrieves the detais of a buddy for a Skype for Asterisk user.<p>
 * Available with Skype for Asterisk.
 *
 * @since 1.0.0
 */
@ExpectedResponse(SkypeBuddyResponse.class)
public class SkypeBuddyAction extends AbstractManagerAction
{
    /**
     * Serializable version identifier
     */
    static final long serialVersionUID = 1L;
    private String user;
    private String buddy;

    /**
     * Creates a new SkypeBuddiesAction.
     */
    public SkypeBuddyAction()
    {

    }

    /**
     * Creates a new SkypeBuddiesAction that retrieves the details of the given buddy for the given user.
     *
     * @param user  the Skype username of the user to retrieve the buddy details for.
     * @param buddy the Skype username of the buddy.
     */
    public SkypeBuddyAction(String user, String buddy)
    {
        this.user = user;
        this.buddy = buddy;
    }

    /**
     * Returns the Skype username of the Skype for Asterisk user.<p>
     * This property is mandatory.
     *
     * @return the Skype username of the Skype for Asterisk user.
     */
    public String getUser()
    {
        return user;
    }

    /**
     * Sets the Skype username of the Skype for Asterisk user.
     *
     * @param user the Skype username of the Skype for Asterisk user.
     */
    public void setUser(String user)
    {
        this.user = user;
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
     * Sets the Skype username of the buddy.<p>
     * This property is mandatory.
     *
     * @param buddy the Skype username of the buddy.
     */
    public void setBuddy(String buddy)
    {
        this.buddy = buddy;
    }

    /**
     * Returns the name of this action, i.e. "SkypeBuddy".
     */
    @Override
    public String getAction()
    {
        return "SkypeBuddy";
    }
}
