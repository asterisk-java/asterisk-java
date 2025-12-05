package org.asteriskjava.manager.event;

/**
 * A QueueMemberPauseEvent is triggered when a queue member is paused or
 * unpaused.
 * <p>
 * Before the release of Asterisk 12, this event was called
 * {@code QueueMemberPaused} but was erroneously changed in commit a2d02edc to
 * {@code QueueMemberPause}.
 */
public class QueueMemberPauseEvent extends QueueMemberPausedEvent {
    private static final long serialVersionUID = 5184794168561845630L;

    // Logger logger = LogManager.getLogger();
    String membership;
    Long lastcall;
    Long lastpause;
    Integer callsTaken;
    Integer penalty;
    Integer status;
    Boolean ringinuse;
    String stateInterface;
    Integer incall;
    String pausedreason;
    Integer logintime;
    Integer wrapuptime;

    public QueueMemberPauseEvent(Object source) {
        super(source);
        // TODO Auto-generated constructor stub
    }

    /**
     * @return the membership
     */
    public String getMembership() {
        return membership;
    }

    /**
     * @param membership the membership to set
     */
    public void setMembership(String membership) {
        this.membership = membership;
    }

    /**
     * @return the lastcall
     */
    public Long getLastcall() {
        return lastcall;
    }

    /**
     * @param lastcall the lastcall to set
     */
    public void setLastcall(Long lastcall) {
        this.lastcall = lastcall;
    }

    /**
     * @return the lastpause time in seconds
     */
    public Long getLastpause() {
        return lastpause;
    }

    /**
     * @param lastpause the lastpause time in seconds to set
     */
    public void setLastpause(Long lastpause) {
        this.lastpause = lastpause;
    }

    /**
     * @return the callsTaken
     */
    public Integer getCallsTaken() {
        return callsTaken;
    }

    /**
     * @param callsTaken the callsTaken to set
     */
    public void setCallsTaken(Integer callsTaken) {
        this.callsTaken = callsTaken;
    }

    /**
     * @return the penalty
     */
    public Integer getPenalty() {
        return penalty;
    }

    /**
     * @param penalty the penalty to set
     */
    public void setPenalty(Integer penalty) {
        this.penalty = penalty;
    }

    /**
     * @return the status
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * @return the ringinuse
     */
    public Boolean getRinginuse() {
        return ringinuse;
    }

    /**
     * @param ringinuse the ringinuse to set
     */
    public void setRinginuse(Boolean ringinuse) {
        this.ringinuse = ringinuse;
    }


    /**
     * @return the stateInterface
     */
    public String getStateInterface() {
        return stateInterface;
    }

    /**
     * @param stateInterface the stateInterface to set
     */
    public void setStateInterface(String stateInterface) {
        this.stateInterface = stateInterface;
    }

    /**
     * @return get Incall
     */
    public Integer getIncall() {
        return incall;
    }

    /**
     * @param setIncall the incall to set
     */
    public void setIncall(Integer incall) {
        this.incall = incall;
    }

    public String getPausedreason() {
        return pausedreason;
    }

    public void setPausedreason(String pausedreason) {
        this.pausedreason = pausedreason;
    }

    /**
     * Gets the login time
     *
     * @return the login time of the agent as a UNIX timestamp
     */
    public Integer getLoginTime() {
        return logintime;
    }

    /**
     * Sets the login time
     *
     * @param logintime the login time of the agent as a UNIX timestamp
     */
    public void setLoginTime(Integer logintime) {
        this.logintime = logintime;
    }

    /**
     * Gets the agent's wrap up time
     *
     * @return the agent's wrap up time (in seconds)
     */
    public Integer getWrapupTime() {
        return wrapuptime;
    }

    /**
     * Sets the agent's wrap up time
     *
     * @param wrapuptime the agent's wrap up time (in seconds)
     */
    public void setWrapupTime(Integer wrapuptime) {
        this.wrapuptime = wrapuptime;
    }
}
