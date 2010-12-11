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

import org.asteriskjava.util.Base64;

import java.nio.charset.Charset;

/**
 * The SkypeChatSendAction sends a Skype Chat message a buddy to the buddy list of a Skype for Asterisk user.<p>
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
    private String from;
    private String to;
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
     * @param from the Skype username of the sender of this chat message.
     * @param to the Skype username of the recipient of this chat message.
     * @param decodedMessage the message to send.
     */
    public SkypeChatSendAction(String from, String to, String decodedMessage)
    {
        this.from = from;
        this.to = to;
        setDecodedMessage(decodedMessage);
    }

    /**
     * Returns the Skype username of the recipient of this chat message.
     *
     * @return the Skype username of the recipient of this chat message.
     */
    public String getTo()
    {
        return to;
    }

    /**
     * Sets the Skype username of the recipient of this chat message.
     *
     * @param to the Skype username of the recipient of this chat message.
     */
    public void setTo(String to)
    {
        this.to = to;
    }

    /**
     * Returns the Skype username of the sender of this chat message.
     *
     * @return the Skype username of the sender of this chat message.
     */
    public String getFrom()
    {
        return from;
    }

    /**
     * Sets the Skype username of the sender of this chat message.
     *
     * @param from the Skype username of the sender of this chat message.
     */
    public void setFrom(String from)
    {
        this.from = from;
    }

    /**
     * Returns the Base64 encoded message.
     *
     * @return the Base64 encoded message.
     */
    public String getMessage()
    {
        return message;
    }

    /**
     * Sets the Base64 encoded message.
     *
     * @param message the Base64 encoded message.
     */
    public void setMessage(String message)
    {
        this.message = message;
    }

    /**
     * Sets the message to send. The decoded message will be converted to Base64.
     *
     * @param decodedMessage the message to send.
     * @see #setMessage(String)
     */
    public void setDecodedMessage(String decodedMessage)
    {
        if (decodedMessage == null)
        {
            this.message = null;
        }
        this.message = Base64.byteArrayToBase64(message.getBytes(Charset.forName("UTF-8")));
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
