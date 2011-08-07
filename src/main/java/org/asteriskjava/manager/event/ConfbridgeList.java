package org.asteriskjava.manager.event;

public class ConfbridgeList extends ManagerEvent {
	
	/**
     * Serializable version identifier
     */
	private static final long serialVersionUID = -1766608489405344617L;
	
	private String conference;
	private String callerIDnum;
    private String callerIdName;
    private String admin;
    private String markedUser;
    private String actionId;
    private String channel;
	
    /**
     * @param source
     */
	public ConfbridgeList(Object source) {
		super(source);
	}

	/**
     * Sets the id of the conference to be listed.
     */	
    public void setConference(String conference)
    {
        this.conference = conference;
    }

    /**
     * Returns the id of the conference to be listed.
     */ 
    public String getConference()
    {
        return conference;
    }
    
    /**
     * Sets the Caller*ID Number of the channel in the list of the conference.
     * 
     * @param callerIdNumber the Caller*ID Number of the channel in the list of the conference.
     */   
	public void setCallerIDnum(String callerIDnum) {
		this.callerIDnum = callerIDnum;
	}
	
    /**
     * Returns the Caller*ID Number of the channel in the list of the conference.
     * 
     * @return the Caller*ID Number of the channel in the list of the conference.
     */
	public String getCallerIDnum() {
		return callerIDnum;
	}
	
    /**
     * Sets the Caller*ID Name of the channel in the list of the conference.
     * 
     * @param callerIdName the Caller*ID Name of the channel in the list of the conference.
     */
	public void setCallerIdName(String callerIdName) {
		this.callerIdName = callerIdName;
	}
	
    /**
     * Returns the Caller*ID Name of the channel in the list of the conference.<p>
     * 
     * @return the Caller*ID Name of the channel in the list of the conference.
     */
	public String getCallerIdName() {
		return callerIdName;
	}
	
    /**
     * Sets the role of the caller in the list admin = yes or no of the conference.
     * 
     * @param admin = yes or no the role of the caller in the list of the conference.
     */
	public void setAdmin(String admin) {
		this.admin = admin;
	}
	
    /**
     * Returns the role of the caller in the list admin = yes or no of the conference.<p>
     * 
     * @return the role of the caller in the list admin = yes or no of the conference.
     */	
	public String getAdmin() {
		return admin;
	}

	public void setMarkedUser(String markedUser) {
		this.markedUser = markedUser;
	}

	public String getMarkedUser() {
		return markedUser;
	}

	public void setActionId(String actionId) {
		this.actionId = actionId;
	}

	public String getActionId() {
		return actionId;
	}
	
    /**
     * Sets the number of the channel in the list.
     */
	public void setChannel(String channel) {
		this.channel = channel;
	}
	
    /**
     * Returns the number of the channel in the list.
     */  
	public String getChannel() {
		return channel;
	}

}
