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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * A CdrEvent is triggered when a call detail record is generated, usually at the end of a call.<br>
 * To enable CdrEvents you have to add <code>enabled = yes</code> to the general section in
 * <code>cdr_manager.conf</code>.<br>
 * This event is implemented in <code>cdr/cdr_manager.c</code>
 * 
 * @author srt
 * @version $Id$
 */
public class CdrEvent extends ManagerEvent
{
    /**
     * Serializable version identifier
     */
    private static final long serialVersionUID = 2541424315212201670L;
    private static final String DATE_TIME_PATTERN = "y-M-D H:m:s";
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

    /**
     * Returns the account number that is usually used to identify the party to bill for the call.<br>
     * Corresponds to CDR field <code>accountcode</code>.
     * 
     * @return the account number.
     */
    public String getAccountCode()
    {
        return accountCode;
    }

    /**
     * Sets the account number.
     * 
     * @param accountCode the account number.
     */
    public void setAccountCode(String accountCode)
    {
        this.accountCode = accountCode;
    }

    /**
     * Returns the Caller*ID number.<br>
     * Corresponds to CDR field <code>src</code>.
     * 
     * @return the Caller*ID number.
     */
    public String getSrc()
    {
        return src;
    }

    /**
     * Sets the Caller*ID number.
     * 
     * @param source the Caller*ID number.
     */
    public void setSrc(String source)
    {
        this.src = source;
    }

    /**
     * Returns the destination extension.<br>
     * Corresponds to CDR field <code>dst</code>.
     * 
     * @return the destination extension.
     */
    public String getDestination()
    {
        return destination;
    }

    /**
     * Sets the destination extension.
     * 
     * @param destination the destination extension.
     */
    public void setDestination(String destination)
    {
        this.destination = destination;
    }

    /**
     * Returns the destination context.<br>
     * Corresponds to CDR field <code>dcontext</code>.
     * 
     * @return the destination context.
     */
    public String getDestinationContext()
    {
        return destinationContext;
    }

    /**
     * Sets the destination context.
     * 
     * @param destinationContext the destination context.
     */
    public void setDestinationContext(String destinationContext)
    {
        this.destinationContext = destinationContext;
    }

    /**
     * Returns the Caller*ID with text.<br>
     * Corresponds to CDR field <code>clid</code>.
     * 
     * @return the Caller*ID with text
     */
    public String getCallerId()
    {
        return callerId;
    }

    /**
     * Sets the Caller*ID with text.
     * 
     * @param callerId the Caller*ID with text.
     */
    public void setCallerId(String callerId)
    {
        this.callerId = callerId;
    }

    /**
     * Returns the name of the channel, for example "SIP/1310-asfe".<br>
     * Corresponds to CDR field <code>channel</code>.
     * 
     * @return the name of the channel.
     */
    public String getChannel()
    {
        return channel;
    }

    /**
     * Sets the name of the channel.
     * 
     * @param channel the name of the channel.
     */
    public void setChannel(String channel)
    {
        this.channel = channel;
    }

    /**
     * Returns the name of the destination channel if appropriate.<br>
     * Corresponds to CDR field <code>dstchannel</code>.
     * 
     * @return the name of the destination channel or <code>null</code> if not available.
     */
    public String getDestinationChannel()
    {
        return destinationChannel;
    }

    /**
     * Sets the name of the destination channel.
     * 
     * @param destinationChannel the name of the destination channel.
     */
    public void setDestinationChannel(String destinationChannel)
    {
        this.destinationChannel = destinationChannel;
    }

    /**
     * Returns the last application if appropriate, for example "VoiceMail".<br>
     * Corresponds to CDR field <code>lastapp</code>.
     * 
     * @return the last application or <code>null</code> if not avaialble.
     */
    public String getLastApplication()
    {
        return lastApplication;
    }

    /**
     * Sets the last application.
     * 
     * @param lastApplication the last application.
     */
    public void setLastApplication(String lastApplication)
    {
        this.lastApplication = lastApplication;
    }

    /**
     * Returns the last application's data (arguments), for example "s1234".<br>
     * Corresponds to CDR field <code>lastdata</code>.
     * 
     * @return the last application's data or <code>null</code> if not avaialble.
     */
    public String getLastData()
    {
        return lastData;
    }

    /**
     * Set the last application's data.
     * 
     * @param lastData the last application's data.
     */
    public void setLastData(String lastData)
    {
        this.lastData = lastData;
    }

    /**
     * Returns when the call has started.<br>
     * This corresponds to CDR field <code>start</code>.
     * 
     * @return A string of the format "%Y-%m-%d %T" (strftime(3)) representing the date/time the
     * call has started, for example "2006-05-19 11:54:48".
     */
    public String getStartTime()
    {
        return startTime;
    }
    
    /**
     * Returns the start time as Date object.<br>
     * This method asumes that the Asterisk server's timezone equals the default 
     * timezone of your JVM.
     * 
     * @return the start time as Date object.
     * @since 0.3
     */
    public Date getStartTimeAsDate()
    {
        return parseDateTime(startTime);
    }
    
    /**
     * Returns the start time as Date object.
     * 
     * @param tz the timezone of the Asterisk server.
     * @return the start time as Date object.
     * @since 0.3
     */
    public Date getStartTimeAsDate(TimeZone tz)
    {
        return parseDateTime(startTime, tz);
    }

    /**
     * Sets the date/time when the call has started.
     * 
     * @param startTime the date/time when the call has started.
     */
    public void setStartTime(String startTime)
    {
        this.startTime = startTime;
    }

    /**
     * Returns when the call was answered.<br>
     * This corresponds to CDR field <code>answered</code>.
     * 
     * @return A string of the format "%Y-%m-%d %T" (strftime(3)) representing the date/time the
     * call was answered, for example "2006-05-19 11:55:01"
     */
    public String getAnswerTime()
    {
        return answerTime;
    }

    /**
     * Returns the answer time as Date object.<br>
     * This method asumes that the Asterisk server's timezone equals the default 
     * timezone of your JVM.
     * 
     * @return the answer time as Date object.
     * @since 0.3
     */
    public Date getAnswerTimeAsDate()
    {
        return parseDateTime(answerTime);
    }

    /**
     * Returns the answer time as Date object.
     * 
     * @param tz the timezone of the Asterisk server.
     * @return the answer time as Date object.
     * @since 0.3
     */
    public Date getAnswerTimeAsDate(TimeZone tz)
    {
        return parseDateTime(answerTime, tz);
    }

    /**
     * Sets the date/time when the call was answered.
     * 
     * @param answerTime the date/time when the call was answered.
     */
    public void setAnswerTime(String answerTime)
    {
        this.answerTime = answerTime;
    }

    /**
     * Returns when the call has ended.<br>
     * This corresponds to CDR field <code>end</code>.
     * 
     * @return A string of the format "%Y-%m-%d %T" (strftime(3)) representing the date/time the
     * call has ended, for example "2006-05-19 11:58:21"
     */
    public String getEndTime()
    {
        return endTime;
    }

    /**
     * Returns the end time as Date object.<br>
     * This method asumes that the Asterisk server's timezone equals the default 
     * timezone of your JVM.
     * 
     * @return the end time as Date object.
     * @since 0.3
     */
    public Date getEndTimeAsDate()
    {
        return parseDateTime(endTime);
    }
    
    /**
     * Returns the end time as Date object.
     * 
     * @param tz the timezone of the Asterisk server.
     * @return the end time as Date object.
     * @since 0.3
     */
    public Date getEndTimeAsDate(TimeZone tz)
    {
        return parseDateTime(endTime, tz);
    }

    /**
     * Sets the date/time when the call has ended.
     * 
     * @param endTime the date/time when the call has ended.
     */
    public void setEndTime(String endTime)
    {
        this.endTime = endTime;
    }

    /**
     * Returns the total time (in seconds) the caller spent in the system from dial to hangup.<br>
     * Corresponds to CDR field <code>duration</code>.
     * 
     * @return the total time in system in seconds.
     */
    public Integer getDuration()
    {
        return duration;
    }

    /**
     * Sets the total time in system.
     * 
     * @param duration total time in system in seconds.
     */
    public void setDuration(Integer duration)
    {
        this.duration = duration;
    }

    /**
     * Returns the total time (in seconds) the call was up from answer to hangup.<br>
     * Corresponds to CDR field <code>billsec</code>.
     * 
     * @return the total time in call in seconds.
     */
    public Integer getBillableSeconds()
    {
        return billableSeconds;
    }

    /**
     * Sets the total time in call.
     * 
     * @param billableSeconds the total time in call in seconds.
     */
    public void setBillableSeconds(Integer billableSeconds)
    {
        this.billableSeconds = billableSeconds;
    }

    /**
     * Returns what happened to the call.<br>
     * This is one of "NO ANSWER", "FAILED", "BUSY", "ANSWERED" or "UNKNOWN".<br>
     * Corresponds to CDR field <code>disposition</code>.
     * 
     * @return the disposition.
     */
    public String getDisposition()
    {
        return disposition;
    }

    /**
     * Sets the disposition.
     * 
     * @param disposition the disposition.
     */
    public void setDisposition(String disposition)
    {
        this.disposition = disposition;
    }

    /**
     * Returns the AMA (Automated Message Accounting) flags.<br>
     * This is one of "OMIT", "BILLING", "DOCUMENTATION" or "Unknown".<br>
     * Corresponds to CDR field <code>amaflags</code>.
     * 
     * @return the AMA flags.
     */
    public String getAmaFlags()
    {
        return amaFlags;
    }

    /**
     * Sets the AMA (Automated Message Accounting) flags.
     * 
     * @param amaFlags the AMA (Automated Message Accounting) flags.
     */
    public void setAmaFlags(String amaFlags)
    {
        this.amaFlags = amaFlags;
    }

    /**
     * Returns the unique id of the channel.
     * 
     * @return the unique id of the channel.
     */
    public String getUniqueId()
    {
        return uniqueId;
    }

    /**
     * Sets the unique id of the channel.
     * 
     * @param uniqueId the unique id of the channel.
     */
    public void setUniqueId(String uniqueId)
    {
        this.uniqueId = uniqueId;
    }

    /**
     * Returns the user-defined field as set by <code>Set(CDR(userfield)=Value)</code>.<br>
     * Corresponds to CDR field <code>userfield</code>.
     * 
     * @return the user-defined field.
     */
    public String getUserField()
    {
        return userField;
    }

    /**
     * Sets the user-defined field.
     * 
     * @param userField the user-defined field
     */
    public void setUserField(String userField)
    {
        this.userField = userField;
    }

    private Date parseDateTime(String s)
    {
        return parseDateTime(s, null);
    }

    private Date parseDateTime(String s, TimeZone tz)
    {
        DateFormat df;
        
        if (s == null)
        {
            return null;
        }
        
        df = new SimpleDateFormat(DATE_TIME_PATTERN);
        if (tz != null)
        {
            df.setTimeZone(tz);
        }
        try
        {
            return df.parse(s);
        }
        catch (ParseException e)
        {
            return null;
        }
    }    
}
