/*
 *  Copyright 2004-2006 Stefan Reuter
 *  
 *  amended 2010 Allan Wylie
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
 * A SkypeAccountStatusEvent is sent when a Skype for Asterisk user logs in or out
 * of the Skype community.<p>
 * It is implemented in <code>chan_skye.c</code>.<p>
 * Available with Skype for Asterisk.
 *
 * @since 1.0.0
 */
public class SkypeAccountStatusEvent extends ManagerEvent
{
    private static final long serialVersionUID = 1L;
    private String username;
    private String status;

    static final String STATUS_LOGGED_IN = "Logged In";
    static final String STATUS_LOGGED_OUT = "Logged Out";

    public SkypeAccountStatusEvent(Object source)
    {
        super(source);
    }

    /**
     * Returns the name of the Skype user.
     *
     * @return the name of the Skype user.
     */
    public String getUsername()
    {
        return username;
    }

    /**
     * Sets the name of the Skype user.
     *
     * @param username the name of the Skype user.
     */
    public void setUsername(String username)
    {
        this.username = username;
    }

    /**
     * Returns the Skype user status.
     *
     * @return the Skype user status.
     */
    public String getStatus()
    {
        return status;
    }

    /**
     * Sets the Skype user status.
     *
     * @param status user status.
     */
    public void setStatus(String status)
    {
        this.status = status;
    }
}
