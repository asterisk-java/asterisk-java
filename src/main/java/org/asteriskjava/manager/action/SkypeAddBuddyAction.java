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

/**
 * The SkypeAddBuddyAction adds a buddy to the buddy list of a Skype for Asterisk user.<p>
 * Available with Skype for Asterisk.
 *
 * @since 1.0.0
 */
public class SkypeAddBuddyAction extends AbstractManagerAction
{
    /**
     * Serializable version identifier
     */
    static final long serialVersionUID = 1L;
    private String user;
    private String buddy;
    private String authMsg;

    /**
     * Creates a new SkypeAddBuddyAction.
     */
    public SkypeAddBuddyAction()
    {

    }

    /**
     * Creates a new SkypeAddBuddyAction that adds the given buddy to the given Skype for
     * Asterisk user's buddy list.
     *
     * @param user  the Skype username of the user to add the buddy to.
     * @param buddy the Skype username of the buddy.
     */
    public SkypeAddBuddyAction(String user, String buddy)
    {
        this.user = user;
        this.buddy = buddy;
    }

    /**
     * Creates a new SkypeAddBuddyAction that adds the given buddy to the given Skype for
     * Asterisk user's buddy list with a custom auth message.
     *
     * @param user    the Skype username of the user to add the buddy to.
     * @param buddy   the Skype username of the buddy.
     * @param authMsg the auth message.
     */
    public SkypeAddBuddyAction(String user, String buddy, String authMsg)
    {
        this.user = user;
        this.buddy = buddy;
        this.authMsg = authMsg;
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
     * Returns the auth message.
     *
     * @return the auth message.
     */
    public String getAuthMsg()
    {
        return authMsg;
    }

    /**
     * Sets the auth message.
     *
     * @param authMsg the auth message.
     */
    public void setAuthMsg(String authMsg)
    {
        this.authMsg = authMsg;
    }

    /**
     * Returns the name of this action, i.e. "SkypeAddBuddy".
     */
    @Override
    public String getAction()
    {
        return "SkypeAddBuddy";
    }
}
