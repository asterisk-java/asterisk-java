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
 * The SkypeChatSendAction sends a Skype Chat message to a buddy of a Skype for Asterisk user.<p>
 * Available with Skype for Asterisk.
 *
 * @since 1.0.0
 */
public class SkypeChatSendAction extends AbstractManagerAction
{
    /**
     * Serializable version identifier
     */
    static final long serialVersionUID = 1L;
    private String user;
    private String skypename;
    private String message;

    /**
     * Creates a new SkypeAddBuddyAction.
     */
    public SkypeChatSendAction()
    {

    }

    /**
     * Creates a new SkypeChatSendAction with the given parameters.
     *
     * @param user the Skype username of the sender of this chat message.
     * @param skypename the Skype username of the recipient of this chat message.
     * @param message the message to send. Must not contain newlines but you can use "\r".
     */
    public SkypeChatSendAction(String user, String skypename, String message)
    {
        this.user = user;
        this.skypename = skypename;
        this.message = message;
    }

    /**
     * Returns the Skype username of the recipient of this chat message.
     *
     * @return the Skype username of the recipient of this chat message.
     */
    public String getSkypename()
    {
        return skypename;
    }

    /**
     * Sets the Skype username of the recipient of this chat message.
     *
     * @param skypename the Skype username of the recipient of this chat message.
     */
    public void setSkypename(String skypename)
    {
        this.skypename = skypename;
    }

    /**
     * Returns the Skype username of the sender of this chat message.
     *
     * @return the Skype username of the sender of this chat message.
     */
    public String getUser()
    {
        return user;
    }

    /**
     * Sets the Skype username of the sender of this chat message.
     *
     * @param user the Skype username of the sender of this chat message.
     */
    public void setUser(String user)
    {
        this.user = user;
    }

    /**
     * Returns the message to send.
     *
     * @return the message to send.
     */
    public String getMessage()
    {
        return message;
    }

    /**
     * Sets the message to send. Must not contain newlines but you can use "\r".
     *
     * @param message the message to send.
     */
    public void setMessage(String message)
    {
        this.message = message;
    }

    /**
     * Returns the name of this action, i.e. "SkypeChatSend".
     */
    @Override
    public String getAction()
    {
        return "SkypeChatSend";
    }
}
