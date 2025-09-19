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
 * A MeetMeTalkingEvent is triggered when a user starts talking in a meet me
 * conference.
 * <p>
 * To enable talker detection you must pass the option 'T' to the MeetMe
 * application.
 * <p>
 * It is implemented in <code>apps/app_meetme.c</code>
 * <p>
 * Available since Asterisk 1.2
 *
 * @author srt
 * @version $Id$
 * @see org.asteriskjava.manager.event.MeetMeStopTalkingEvent
 * @since 0.2
 */
public class MeetMeTalkingEvent extends AbstractMeetMeEvent {
    /**
     * Serializable version identifier.
     */
    private static final long serialVersionUID = -8554403451985143184L;

    private String meetme;
    private String channel;
    private Integer channelState;
    private String channelStateDesc;
    private String callerIDNum;
    private String connectedLineNum;
    private String connectedLineName;
    private String language;
    private String accountCode;
    private String context;
    private String exten;
    private Integer priority;
    private String uniqueid;
    private String linkedid;
    private Integer duration;
    protected Boolean status = Boolean.TRUE;

    public MeetMeTalkingEvent(Object source) {
        super(source);
    }

    /**
     * Returns whether the user has started or stopped talking.<p>
     * Until Asterisk 1.2 Asterisk used different events to indicate start
     * and stop: This MeetMeTalkingEvent when the user started talking and the
     * {@link MeetMeStopTalkingEvent} when he stopped. With Asterisk 1.2
     * only this MeetMeTalkingEvent is used with the status property indicating
     * start and stop. For backwards compatibility this property defaults to
     * <code>true</code> so when used with version 1.2 of Asterisk you get
     * <code>true</code>.
     *
     * @return <code>true</code> if ther user has started talking,
     * <code>false</code> if the user has stopped talking.
     * @since 0.3
     */
    public String getMeetMe() {
        return meetme;
    }

    public void setMeetMe(String meetme) {
        this.meetme = meetme;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public Integer getChannelState() {
        return channelState;
    }

    public void setChannelState(Integer channelState) {
        this.channelState = channelState;
    }

    public String getChannelStateDesc() {
        return channelStateDesc;
    }

    public void setChannelStateDesc(String channelStateDesc) {
        this.channelStateDesc = channelStateDesc;
    }

    public String getCallerIDNum() {
        return callerIDNum;
    }

    public void setCallerIDNum(String callerIDNum) {
        this.callerIDNum = callerIDNum;
    }

    public String getConnectedLineNum() {
        return connectedLineNum;
    }

    public void setConnectedLineNum(String connectedLineNum) {
        this.connectedLineNum = connectedLineNum;
    }

    public String getConnectedLineName() {
        return connectedLineName;
    }

    public void setConnectedLineName(String connectedLineName) {
        this.connectedLineName = connectedLineName;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getAccountCode() {
        return accountCode;
    }

    public void setAccountCode(String accountCode) {
        this.accountCode = accountCode;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getExten() {
        return exten;
    }

    public void setExten(String exten) {
        this.exten = exten;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public String getUniqueId() {
        return uniqueid;
    }

    public void setUniqueId(String uniqueid) {
        this.uniqueid = uniqueid;
    }

    public String getLinkedId() {
        return linkedid;
    }

    public void setLinkedId(String linkedid) {
        this.linkedid = linkedid;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
