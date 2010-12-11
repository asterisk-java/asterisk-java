/*
 *  Copyright 2004-2006 Stefan Reuter
 *  
 *  amended 2010 - Allan Wylie
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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A SkypeBuddyStatusEvent indicates a status change for a contact in a Skype for Asterisk user's
 * contact list.<p>
 * It is implemented in <code>chan_skye.c</code>.<p>
 * Available with Skype for Asterisk.
 *
 * @since 1.0.0
 */
public class SkypeBuddyStatusEvent extends ManagerEvent
{
    /**
     * Serializable version identifier.
     */
    private static final long serialVersionUID = 1L;

    public static final String BUDDYSTATUS_WAITAUTHORIZATION = "Waiting for Authorization";
    public static final String BUDDYSTATUS_BLOCKED = "Blocked";
    public static final String BUDDYSTATUS_BLOCKEDSKYPEOUT = "Blocked Skypout";
    public static final String BUDDYSTATUS_SKYPEOUT = "Skypeout";
    public static final String BUDDYSTATUS_OFFLINE = "Offline";
    public static final String BUDDYSTATUS_ONLINE = "Online";
    public static final String BUDDYSTATUSS_AWAY = "Away";
    public static final String BUDDYSTATUS_NOTAVAILABLE = "Not Available";
    public static final String BUDDYSTATUS_DONTDISTURB = "Do Not Disturb";
    public static final String BUDDYSTATUS_SKYPEME = "Skype Me";
    public static final String BUDDYSTATUS_OFFLINEVOICEENABLE = "Offline (Voicemail Enabled)";
    public static final String BUDDYSTATUS_OFFLINEFORWARDENABLEL = "Offline (Call Forwarding Enabled)";
    public static final String BUDDYSTATUS_UNKNOWN = "Unknown";

    private static final Pattern BUDDY_PATTERN = Pattern.compile("Skype/(.*)@(.*)");

    /**
     * The name of the buddy
     */
    private String buddy;

    /**
     * The buddy status.
     */
    private String buddyStatus;


    public SkypeBuddyStatusEvent(Object source)
    {
        super(source);
    }

    /**
     * Returns the address of the buddy. The format is "Skype/user@buddy" where user is the Skype username of
     * the Skype for Asterisk user and buddy is the Skype username of the buddy who changed his status.<p>
     * For ease of use consider using {@link #getUser()} and {@link #getBuddySkypename()} which already do
     * the parsing.
     *
     * @return the address of the buddy.
     * @see #getUser()
     * @see #getBuddySkypename()
     */
    public String getBuddy()
    {
        return buddy;
    }

    /**
     * Returns the Skype username of the Skype for Asterisk user whose buddy changed his status.
     *
     * @return the Skype username of the Skype for Asterisk user.
     */
    public String getUser()
    {
        return buddyGroup(1);
    }

    /**
     * Returns the Skype username of the buddy who changed his status.
     *
     * @return the Skype username of the buddy who changed his status.
     */
    public String getBuddySkypename()
    {
        return buddyGroup(2);
    }

    private String buddyGroup(int group)
    {
        if (buddy == null)
        {
            return null;
        }
        final Matcher buddyMatcher = BUDDY_PATTERN.matcher(buddy);
        if (buddyMatcher.matches())
        {
            return buddyMatcher.group(group);
        }
        return null;
    }

    /**
     * Sets the address of buddy.
     *
     * @param buddy the address of the buddy.
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
    public String getBuddyStatus()
    {
        return buddyStatus;
    }

    public void setBuddyStatus(String buddyStatus)
    {
        this.buddyStatus = buddyStatus;
    }
}
