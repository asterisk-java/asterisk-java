package org.asteriskjava.manager.event;

public class QueueMemberPauseEvent extends QueueMemberPausedEvent
{
    private static final long serialVersionUID = 58564514209197321L;

    // Logger logger = LogManager.getLogger();
    String membership;
    Long lastcall;
    Integer callsTaken;
    Integer penalty;
    Integer status;
    Boolean ringinuse;
    String iface;
    String stateInterface;
    Integer incall;
    String pausedreason;

    public QueueMemberPauseEvent(Object source)
    {
        super(source);
        // TODO Auto-generated constructor stub
    }

    /**
     * @return the membership
     */
    public String getMembership()
    {
        return membership;
    }

    /**
     * @param membership the membership to set
     */
    public void setMembership(String membership)
    {
        this.membership = membership;
    }

    /**
     * @return the lastcall
     */
    public Long getLastcall()
    {
        return lastcall;
    }

    /**
     * @param lastcall the lastcall to set
     */
    public void setLastcall(Long lastcall)
    {
        this.lastcall = lastcall;
    }

    /**
     * @return the callsTaken
     */
    public Integer getCallsTaken()
    {
        return callsTaken;
    }

    /**
     * @param callsTaken the callsTaken to set
     */
    public void setCallsTaken(Integer callsTaken)
    {
        this.callsTaken = callsTaken;
    }

    /**
     * @return the penalty
     */
    public Integer getPenalty()
    {
        return penalty;
    }

    /**
     * @param penalty the penalty to set
     */
    public void setPenalty(Integer penalty)
    {
        this.penalty = penalty;
    }

    /**
     * @return the status
     */
    public Integer getStatus()
    {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(Integer status)
    {
        this.status = status;
    }

    /**
     * @return the ringinuse
     */
    public Boolean getRinginuse()
    {
        return ringinuse;
    }

    /**
     * @param ringinuse the ringinuse to set
     */
    public void setRinginuse(Boolean ringinuse)
    {
        this.ringinuse = ringinuse;
    }

    /**
     * @return the iface
     */
    public String getInterface()
    {
        return iface;
    }

    /**
     * @param iface the iface to set
     */
    public void setInterface(String iface)
    {
        this.iface = iface;
    }

    /**
     * @return the stateInterface
     */
    public String getStateInterface()
    {
        return stateInterface;
    }

    /**
     * @param stateInterface the stateInterface to set
     */
    public void setStateInterface(String stateInterface)
    {
        this.stateInterface = stateInterface;
    }

    /**
     * @return get Incall
     */
    public Integer getIncall()
    {
        return incall;
    }

    /**
     * @param setIncall the incall to set
     */
    public void setIncall(Integer incall)
    {
        this.incall = incall;
    }

    public String getPausedreason() 
    {
        return pausedreason;
    }

    public void setPausedreason(String pausedreason) 
    {
        this.pausedreason = pausedreason;
    }


}
