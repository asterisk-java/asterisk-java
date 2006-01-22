/*
 * Copyright  2004-2005 Stefan Reuter
 * 
 * Copyright 2005 Ben Hencke
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
 */
package net.sf.asterisk.manager;

import java.util.*;

/**
 * @author Ben Hencke
 */
public class Call
{
    private String uniqueId;
    private Integer reason;
    private Channel channel;

    private Date startTime;
    private Date endTime;

    public Call()
    {
        startTime = new Date();
    }

    public String getUniqueId()
    {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId)
    {
        this.uniqueId = uniqueId;
    }

    public Channel getChannel()
    {
        return channel;
    }

    public void setChannel(Channel channel)
    {
        this.channel = channel;
    }

    public Integer getReason()
    {
        return reason;
    }

    public void setReason(Integer reason)
    {
        this.reason = reason;
    }

    public Date getStartTime()
    {
        return startTime;
    }

    public void setStartTime(Date startTime)
    {
        this.startTime = startTime;
    }

    public Date getEndTime()
    {
        return endTime;
    }

    public void setEndTime(Date endTime)
    {
        this.endTime = endTime;
    }

    /**
     * @return The duration of the call in milliseconds. If the call is has not
     *         ended, the duration so far is calculated.
     */
    public long calcDuration()
    {
        Date compTime;

        if (endTime != null)
        {
            compTime = endTime;
        }
        else
        {
            compTime = new Date();
        }

        return compTime.getTime() - startTime.getTime();
    }

    public String toString()
    {
        StringBuffer sb;

        sb = new StringBuffer(getClass().getName() + ": ");
        sb.append("uniqueId=" + getUniqueId() + "; ");
        sb.append("reason=" + getReason() + "; ");
        sb.append("startTime=" + getStartTime() + "; ");
        sb.append("endTime=" + getEndTime() + "; ");
        sb.append("systemHashcode=" + System.identityHashCode(this));

        return sb.toString();
    }
}
