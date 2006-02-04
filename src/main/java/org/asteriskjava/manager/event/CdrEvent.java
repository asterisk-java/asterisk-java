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
 * A CdrEvent is triggered when a call detail record is generated, usually at the end of a call.<br>
 * To enable CdrEvents you have to add <code>enabled = yes</code> to the general section in
 * <code>cdr_manager.conf</code>.<br>
 * This event is implemented in <code>cdr/cdr_manager.c</code>
 * 
 * @author srt
 * @version $Id: CdrEvent.java,v 1.2 2005/02/23 22:50:58 srt Exp $
 */
public class CdrEvent extends ManagerEvent
{
    /**
     * Serializable version identifier
     */
    private static final long serialVersionUID = 2541424315212201670L;
    private String accountCode;
    private String src;
    private String destination;
    private String destinationContext;
    private String callerId;
    private String channel;
    private String destinationChannel;
    private String lastApplication;
    private String lastData;
    private String startTime;
    private String answerTime;
    private String endTime;
    private Integer duration;
    private Integer billableSeconds;
    private String disposition;
    private String amaFlags;
    private String uniqueId;
    private String userField;

    /**
     * @param source
     */
    public CdrEvent(Object source)
    {
        super(source);
    }

    public String getAccountCode()
    {
        return accountCode;
    }

    public void setAccountCode(String accountCode)
    {
        this.accountCode = accountCode;
    }

    public String getSrc()
    {
        return src;
    }

    public void setSrc(String source)
    {
        this.src = source;
    }

    public String getDestination()
    {
        return destination;
    }

    public void setDestination(String destination)
    {
        this.destination = destination;
    }

    public String getDestinationContext()
    {
        return destinationContext;
    }

    public void setDestinationContext(String destinationContext)
    {
        this.destinationContext = destinationContext;
    }

    public String getCallerId()
    {
        return callerId;
    }

    public void setCallerId(String callerId)
    {
        this.callerId = callerId;
    }

    public String getChannel()
    {
        return channel;
    }

    public void setChannel(String channel)
    {
        this.channel = channel;
    }

    public String getDestinationChannel()
    {
        return destinationChannel;
    }

    public void setDestinationChannel(String destinationChannel)
    {
        this.destinationChannel = destinationChannel;
    }

    public String getLastApplication()
    {
        return lastApplication;
    }

    public void setLastApplication(String lastApplication)
    {
        this.lastApplication = lastApplication;
    }

    public String getLastData()
    {
        return lastData;
    }

    public void setLastData(String lastData)
    {
        this.lastData = lastData;
    }

    public String getStartTime()
    {
        return startTime;
    }

    public void setStartTime(String startTime)
    {
        this.startTime = startTime;
    }

    public String getAnswerTime()
    {
        return answerTime;
    }

    public void setAnswerTime(String answerTime)
    {
        this.answerTime = answerTime;
    }

    public String getEndTime()
    {
        return endTime;
    }

    public void setEndTime(String endTime)
    {
        this.endTime = endTime;
    }

    public Integer getDuration()
    {
        return duration;
    }

    public void setDuration(Integer duration)
    {
        this.duration = duration;
    }

    public Integer getBillableSeconds()
    {
        return billableSeconds;
    }

    public void setBillableSeconds(Integer billableSeconds)
    {
        this.billableSeconds = billableSeconds;
    }

    public String getDisposition()
    {
        return disposition;
    }

    public void setDisposition(String disposition)
    {
        this.disposition = disposition;
    }

    public String getAmaFlags()
    {
        return amaFlags;
    }

    public void setAmaFlags(String amaFlags)
    {
        this.amaFlags = amaFlags;
    }

    public String getUniqueId()
    {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId)
    {
        this.uniqueId = uniqueId;
    }

    public String getUserField()
    {
        return userField;
    }

    public void setUserField(String userField)
    {
        this.userField = userField;
    }
}
