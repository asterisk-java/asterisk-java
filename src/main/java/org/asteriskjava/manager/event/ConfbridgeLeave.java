package org.asteriskjava.manager.event;

public class ConfbridgeLeave extends ManagerEvent {
	
	/**
     * Serializable version identifier
     */
	private static final long serialVersionUID = 8228841427502609712L;
	
    private String callerIdNum;
    private String callerIdName;
    private String callerChannel;
	private String conference;
	private String uniqueid;

    /**
     * @param source
     */
	public ConfbridgeLeave(Object source) {
		super(source);
	}
    /**
     * Returns the Caller*ID Name of the channel that left the conference.<p>
     * 
     * @return the Caller*ID Name of the channel that left the conference.
     */
    public String getCallerIdName()
    {
        return callerIdName;
    }

    /**
     * Sets the Caller*ID Name of the channel that left the conference.
     * 
     * @param callerIdName the Caller*ID Name of the channel that left the conference.
     */
    public void setCallerIdName(String callerIdName)
    {
        this.callerIdName = callerIdName;
    }

    /**
     * Returns the Caller*ID Number of the channel that left the conference.<p>
     * 
     * @return the Caller*ID Number of the channel that left the conference.
     */
    public String getCallerIdNum()
    {
        return callerIdNum;
    }

    /**
     * Sets the Caller*ID Number of the channel that left the conference.
     * 
     * @param callerIdNum the Caller*ID Number of the channel that left the conference.
     */
    public void setCallerIdNum(String callerIdNum)
    {
        this.callerIdNum = callerIdNum;
    }

    /**
     * Sets the number of the channel on which the participant left.
     */
    public void setChannel(String callerChannel)
    {
        this.callerChannel = callerChannel;
    }
    
    /**
     * Returns the number of the channel on which the participant left.
     */      
    public String getChannel()
    {
        return callerChannel;
    }

	/**
     * Sets the id of the conference the participant left.
     */	    
    public void setConference(String conference)
    {
        this.conference = conference;
    }

    /**
     * Returns the id of the conference the participant left.
     */ 
    public String getConference()
    {
        return conference;
    }
	public void setUniqueid(String uniqueid) {
		this.uniqueid = uniqueid;
	}
	public String getUniqueid() {
		return uniqueid;
	}

}
