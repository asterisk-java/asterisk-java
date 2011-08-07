package org.asteriskjava.manager.event;

/**
 * This event is sent when the conference detects that a user has either begin or stopped talking.
 * 
 * @author jmb
 * @version $Id$
 */
public class ConfbridgeTalking extends ManagerEvent {

    /**
     * Serializable version identifier
     */
	private static final long serialVersionUID = 6686576410903534777L;

	private String conference;
    private String channel;
    private String talkingStatus;
    
	/**
     * @param source
     */
	public ConfbridgeTalking(Object source) {
		super(source);
	}
	
	/**
     * Sets the id of the conference.
     */	
	public void setConference(String conference) {
		this.conference = conference;
	}
	
    /**
     * Returns the id of the conference.
     */ 
	public String getConference() {
		return conference;
	}
	
    /**
     * Sets the number of the channel on which a participant started or stopped talking.
     */
	public void setChannel(String channel) {
		this.channel = channel;
	}
	
    /**
     * Returns the number of the channel on which a participant started or stopped talking.
     */  
	public String getChannel() {
		return channel;
	}

    /**
     * Sets the talking status on or off.
     */  
	public void setTalkingStatus(String talkingStatus) {
		this.talkingStatus = talkingStatus;
	}
	
    /**
     * Returns the talking status on or off.
     */  
	public String getTalkingStatus() {
		return talkingStatus;
	}

}
