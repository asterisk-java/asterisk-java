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

import java.nio.charset.Charset;

import org.asteriskjava.util.Base64;

/**
 * A SkypeChatMessageEvent is triggered when a Skype Chat message is sent or received.<p>
 * It is implemented in <code>chan_skye.c</code>.<p>
 * Available with Skype for Asterisk.
 *
 * @since 1.0.0
 */
public class SkypeChatMessageEvent extends ManagerEvent
{
    /**
     * Serializable version identifier.
     */
    private static final long serialVersionUID = 1L;

    private String to;
    private String from;
    private String message;

    public SkypeChatMessageEvent(Object source)
    {
        super(source);
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
     * Returns the decoded message.
     *
     * @return the decoded message.
     */
    public String getDecodedMessage()
    {
        if (message == null)
        {
            return null;
        }
        return new String(Base64.base64ToByteArray(message), Charset.forName("UTF-8"));
    }
}
