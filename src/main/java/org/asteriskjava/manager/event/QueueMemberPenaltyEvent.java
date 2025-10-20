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

import org.asteriskjava.ami.event.api.ManagerEvent;

/**
 * A QueueMemberPenaltyEvent is triggered when a queue member is assigned a
 * new penalty.<p>
 * It is implemented in <code>apps/app_queue.c</code>.<p>
 * Available since Asterisk 1.6
 *
 * @author srt
 * @version $Id$
 * @since 1.0.0
 */
public class QueueMemberPenaltyEvent extends ManagerEvent {
    /**
     * Serializable version identifier.
     */
    private static final long serialVersionUID = 0L;
    private String queue;
    private String location;
    private Integer penalty;
    private Boolean paused;
    private Integer wrapuptime;
    private Integer lastpause;
    private String stateinterface;
    private String pausedreason;
    private Integer incall;
    private String membership;
    private String _interface;
    private Integer callstaken;
    private Integer ringinuse;
    private Integer lastcall;
    private String membername;
    private Integer status;

    /**
     * Creates a new instance.
     *
     * @param source
     */
    public QueueMemberPenaltyEvent(Object source) {
        super(source);
    }

    /**
     * Returns if the member is pause
     *
     * @return if the member is pause
     */
    public Boolean getPaused() {
        return paused;
    }

    /**
     * Sets if member is paused or not
     *
     * @param paused if paused
     */
    public void setPaused(Boolean paused) {
        this.paused = paused;
    }

    /**
     * Returns wrapuptime
     *
     * @return Returns wrapuptime
     */
    public Integer getWrapuptime() {
        return wrapuptime;
    }

    /**
     * Sets wrapuptime
     *
     * @param wrapuptime the wrapuptime
     */
    public void setWrapuptime(Integer wrapuptime) {
        this.wrapuptime = wrapuptime;
    }

    /**
     * Returns time in seconds when started last paused the queue member
     *
     * @return Returns time in seconds when started last paused the queue member
     */
    public Integer getLastpause() {
        return lastpause;
    }

    /**
     * Sets time in seconds when started last paused the queue member
     *
     * @param lastpause time in seconds when started last paused the queue member
     */
    public void setLastpause(Integer lastpause) {
        this.lastpause = lastpause;
    }

    /**
     * Returns the queue member's tech or location state
     *
     * @return the queue member's tech or location state
     */
    public String getStateinterface() {
        return stateinterface;
    }

    /**
     * Sets channel technology or location from which the member device changed
     *
     * @param stateinterface channel technology or location from which the member device changed
     */
    public void setStateinterface(String stateinterface) {
        this.stateinterface = stateinterface;
    }

    /**
     * Returns reason if set when paused
     *
     * @return reason if set when paused
     */
    public String getPausedreason() {
        return pausedreason;
    }

    /**
     * Sets reason if set when paused
     *
     * @param pausedreason reason if set when paused
     */
    public void setPausedreason(String pausedreason) {
        this.pausedreason = pausedreason;
    }

    /**
     * Returns if member is in call when event is raised
     *
     * @return if member is in call when event is raised
     */
    public Integer getIncall() {
        return incall;
    }

    /**
     * Sets if member is in call when event is raised
     *
     * @param incall if member is in call when event is raised
     */
    public void setIncall(Integer incall) {
        this.incall = incall;
    }

    /**
     * Returns membership in queue
     * E.g. dynamic, realtime, static
     *
     * @return membership in queue
     */
    public String getMembership() {
        return membership;
    }

    /**
     * Sets membership in queue
     *
     * @param membership membership in queue
     */
    public void setMembership(String membership) {
        this.membership = membership;
    }

    /**
     * Returns queue member's tech or location name
     *
     * @return queue member's tech or location name
     */
    public String getInterface() {
        return _interface;
    }

    /**
     * Sets queue member's tech or location name
     *
     * @param _interface queue member's tech or location name
     */
    public void setInterface(String _interface) {
        this._interface = _interface;
    }

    /**
     * Returns number of calls this queue member has serviced
     *
     * @return number of calls this queue member has serviced
     */
    public Integer getCallstaken() {
        return callstaken;
    }

    /**
     * Sets number of calls this queue member has serviced
     *
     * @param callstaken number of calls this queue member has serviced
     */
    public void setCallstaken(Integer callstaken) {
        this.callstaken = callstaken;
    }

    /**
     * Returns member's ring in use setup
     *
     * @return member's ring in use setup
     */
    public Integer getRinginuse() {
        return ringinuse;
    }

    /**
     * Sets member's ring in use setup
     *
     * @param ringinuse member's ring in use setup
     */
    public void setRinginuse(Integer ringinuse) {
        this.ringinuse = ringinuse;
    }

    /**
     * Returns time this member last took a call, expressed in seconds
     *
     * @return time this member last took a call, expressed in seconds
     */
    public Integer getLastcall() {
        return lastcall;
    }

    /**
     * Sets time this member last took a call, expressed in seconds
     *
     * @param lastcall time this member last took a call, expressed in seconds
     */
    public void setLastcall(Integer lastcall) {
        this.lastcall = lastcall;
    }

    /**
     * Returns name of the queue member.
     *
     * @return name of the queue member.
     */
    public String getMembername() {
        return membername;
    }

    /**
     * Sets name of the queue member.
     *
     * @param membername name of the queue member.
     */
    public void setMembername(String membername) {
        this.membername = membername;
    }

    /**
     * Returns numeric device state status of the queue member
     *
     * @return numeric device state status of the queue member
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * Sets numeric device state status of the queue member
     *
     * @param status numeric device state status of the queue member
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * Returns the name of the queue.
     *
     * @return the name of the queue.
     */
    public String getQueue() {
        return queue;
    }

    /**
     * Sets the name of the queue.
     *
     * @param queue the name of the queue.
     */
    public void setQueue(String queue) {
        this.queue = queue;
    }

    /**
     * Returns the name of the member's interface.<p>
     * E.g. the channel name or agent group.
     *
     * @return the name of the member's interface.
     */
    public String getLocation() {
        return location;
    }

    /**
     * Sets the name of the member's interface.
     *
     * @param member the name of the member's interface.
     */
    public void setLocation(String member) {
        this.location = member;
    }

    /**
     * Returns the new penalty.
     *
     * @return the new penalty.
     */
    public Integer getPenalty() {
        return penalty;
    }

    /**
     * Sets the new penalty.
     *
     * @param penalty the new penalty.
     */
    public void setPenalty(Integer penalty) {
        this.penalty = penalty;
    }
}
