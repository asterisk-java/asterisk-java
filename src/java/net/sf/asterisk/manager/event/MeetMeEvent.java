/*
 * Copyright  2004-2005 Stefan Reuter
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
package net.sf.asterisk.manager.event;

/**
 * Abstract base class providing common properties for meet me (asterisk's conference system)
 * events.
 * 
 * @author srt
 * @version $Id: MeetMeEvent.java,v 1.2 2005/02/23 22:50:58 srt Exp $
 */
public abstract class MeetMeEvent extends ManagerEvent
{
    private String channel;
    private String uniqueId;
    private String meetMe;
    private Integer userNum;

    /**
     * @param source
     */
    public MeetMeEvent(Object source)
    {
        super(source);
    }

    /**
     * Returns the name of the channel that joined of left a conference.
     */
    public String getChannel()
    {
        return channel;
    }

    /**
     * Sets the name of the channel that joined of left a conference.
     */
    public void setChannel(String channel)
    {
        this.channel = channel;
    }

    /**
     * Returns the unique id of the channel.
     */
    public String getUniqueId()
    {
        return uniqueId;
    }

    /**
     * Sets the unique id of the channel.
     */
    public void setUniqueId(String uniqueId)
    {
        this.uniqueId = uniqueId;
    }

    /**
     * Returns the conference number.
     */
    public String getMeetMe()
    {
        return meetMe;
    }

    /**
     * Sets the conference number.
     */
    public void setMeetMe(String meetMe)
    {
        this.meetMe = meetMe;
    }

    public Integer getUserNum()
    {
        return userNum;
    }

    public void setUserNum(Integer userNum)
    {
        this.userNum = userNum;
    }
}
