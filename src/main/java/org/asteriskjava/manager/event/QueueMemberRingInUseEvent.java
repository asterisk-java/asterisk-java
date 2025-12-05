package org.asteriskjava.manager.event;

public class QueueMemberRingInUseEvent extends ManagerEvent {
    private static final long serialVersionUID = -631622399932926664L;

    private String queue;
    private String memberName;
    private String _interface;
    private String stateInterface;
    private String membership;
    private Integer penalty;
    private Integer callsTaken;
    private Integer lastCall;
    private Integer lastPause;
    private Integer loginTime;
    private Integer inCall;
    private Integer status;
    private Boolean paused;
    private String pausedReason;
    private Integer ringInUse;
    private Integer wrapUpTime;

    /**
     * Constructs a QueueMemberRingInUseEvent
     *
     * @param source The object on which the Event initially occurred.
     * @throws IllegalArgumentException if source is null.
     */
    public QueueMemberRingInUseEvent(Object source) {
        super(source);
    }

    /**
     * Returns if the member is paused
     *
     * @return if the member is paused
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
        return wrapUpTime;
    }

    /**
     * Sets wrapuptime
     *
     * @param wrapuptime the wrapuptime
     */
    public void setWrapuptime(Integer wrapuptime) {
        this.wrapUpTime = wrapuptime;
    }

    /**
     * Returns time in seconds when started last paused the queue member
     *
     * @return Returns time in seconds when started last paused the queue member
     */
    public Integer getLastpause() {
        return lastPause;
    }

    /**
     * Sets time in seconds when started last paused the queue member
     *
     * @param lastpause time in seconds when started last paused the queue member
     */
    public void setLastpause(Integer lastpause) {
        this.lastPause = lastpause;
    }

    /**
     * Returns the queue member's tech or location state
     *
     * @return the queue member's tech or location state
     */
    public String getStateinterface() {
        return stateInterface;
    }

    /**
     * Sets channel technology or location from which the member device changed
     *
     * @param stateinterface channel technology or location from which the member device changed
     */
    public void setStateinterface(String stateinterface) {
        this.stateInterface = stateinterface;
    }

    /**
     * Returns reason if set when paused
     *
     * @return reason if set when paused
     */
    public String getPausedreason() {
        return pausedReason;
    }

    /**
     * Sets reason if set when paused
     *
     * @param pausedreason reason if set when paused
     */
    public void setPausedreason(String pausedreason) {
        this.pausedReason = pausedreason;
    }

    /**
     * Returns if member is in call when event is raised
     *
     * @return if member is in call when event is raised
     */
    public Integer getIncall() {
        return inCall;
    }

    /**
     * Sets if member is in call when event is raised
     *
     * @param incall if member is in call when event is raised
     */
    public void setIncall(Integer incall) {
        this.inCall = incall;
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
        return callsTaken;
    }

    /**
     * Sets number of calls this queue member has serviced
     *
     * @param callstaken number of calls this queue member has serviced
     */
    public void setCallstaken(Integer callstaken) {
        this.callsTaken = callstaken;
    }

    /**
     * Returns member's ring in use setup
     *
     * @return member's ring in use setup
     */
    public Integer getRinginuse() {
        return ringInUse;
    }

    /**
     * Sets member's ring in use setup
     *
     * @param ringinuse member's ring in use setup
     */
    public void setRinginuse(Integer ringinuse) {
        this.ringInUse = ringinuse;
    }

    /**
     * Returns time this member last took a call, expressed in seconds
     *
     * @return time this member last took a call, expressed in seconds
     */
    public Integer getLastcall() {
        return lastCall;
    }

    /**
     * Sets time this member last took a call, expressed in seconds
     *
     * @param lastcall time this member last took a call, expressed in seconds
     */
    public void setLastcall(Integer lastcall) {
        this.lastCall = lastcall;
    }

    /**
     * Returns name of the queue member.
     *
     * @return name of the queue member.
     */
    public String getMembername() {
        return memberName;
    }

    /**
     * Sets name of the queue member.
     *
     * @param membername name of the queue member.
     */
    public void setMembername(String membername) {
        this.memberName = membername;
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

    /**
     * Gets the login time
     *
     * @return the login time of the agent as a UNIX timestamp
     */
    public Integer getLoginTime() {
        return loginTime;
    }

    /**
     * Sets the login time
     *
     * @param logintime the login time of the agent as a UNIX timestamp
     */
    public void setLoginTime(Integer logintime) {
        this.loginTime = logintime;
    }
}
