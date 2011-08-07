package org.asteriskjava.manager.event;

/**
 * A ConfbridgeJoin event is triggered when a participant joins a conference.
 * 
 * @author jmb
 * @version $Id$
 */
public class ConfbridgeJoin extends ManagerEvent {

	/**
     * Serializable version identifier
     */
	private static final long serialVersionUID = -8854590057123958764L;
	
	private String callerIDnum;
    private String callerIdName;
    private String callerChannel;
	private String conference;
	private String uniqueId;

    /**
     * @param source
     */
	public ConfbridgeJoin(Object source) {
		super(source);
	}
	
    /**
     * Returns the Caller*ID Number of the channel that joined the conference.
     * 
     * @return the Caller*ID Number of the channel that joined the conference.
     */
    public String getCallerIdNum()
    {
        return callerIDnum;
    }
    
    /**
     * Sets the Caller*ID Number of the channel that joined the conference.
     * 
     * @param callerIdNumber the Caller*ID Number of the channel that joined the conference.
     */   
    public void setCallerIdNum(String callerIDnum)
    {
        this.callerIDnum = callerIDnum;
    }
    
    /**
     * Returns the Caller*ID Name of the channel that joined the conference.<p>
     * 
     * @return the Caller*ID Name of the channel that joined the conference.
     */
    public String getCallerIdName()
    {
        return callerIdName;
    }

    /**
     * Sets the Caller*ID Name of the channel that joined the conference.
     * 
     * @param callerIdName the Caller*ID Name of the channel that joined the conference.
     */
    public void setCallerIdName(String callerIdName)
    {
        this.callerIdName = callerIdName;
    }
    
    /**
     * Sets the number of the channel on which the participant joined.
     */
    public void setChannel(String callerChannel)
    {
        this.callerChannel = callerChannel;
    }
    
    /**
     * Returns the number of the channel on which the participant joined.
     */  
    public String getChannel()
    {
        return callerChannel;
    }

	/**
     * Sets the id of the conference the participant joined.
     */	
    public void setConference(String conference)
    {
        this.conference = conference;
    }

    /**
     * Returns the id of the conference the participant joined.
     */ 
    public String getConference()
    {
        return conference;
    }

	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}

	public String getUniqueId() {
		return uniqueId;
	}

}
