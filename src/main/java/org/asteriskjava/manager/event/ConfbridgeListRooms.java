package org.asteriskjava.manager.event;

public class ConfbridgeListRooms extends ManagerEvent {
	
	/**
     * Serializable version identifier
     */
	private static final long serialVersionUID = -1766608489405344617L;
	
	private String conference;
	private String parties;
    private String marked;
    private String locked;
	private String actionId;

	
    /**
     * @param source
     */
	public ConfbridgeListRooms(Object source) {
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

	public void setParties(String parties) {
		this.parties = parties;
	}

	public String getParties() {
		return parties;
	}

	public void setMarked(String marked) {
		this.marked = marked;
	}

	public String getMarked() {
		return marked;
	}

	public void setLocked(String locked) {
		this.locked = locked;
	}

	public String getLocked() {
		return locked;
	}

	public void setActionId(String actionId) {
		this.actionId = actionId;
	}

	public String getActionId() {
		return actionId;
	}
    

}
