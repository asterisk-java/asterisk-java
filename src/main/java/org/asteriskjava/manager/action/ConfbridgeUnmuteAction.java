package org.asteriskjava.manager.action;

/**
 * The ConfbridgeUnmuteAction unmutes a channel in a conference.
 * 
 * @author jmb
 * @version $Id$
 */
public class ConfbridgeUnmuteAction extends AbstractManagerAction {

	/**
     * Serializable version identifier
     */
	private static final long serialVersionUID = -590852239774371298L;
	
	private String conference;
    private String channel;
    
    /**
     * Creates a new empty ConfbridgeUnmuteAction.
     */
	public ConfbridgeUnmuteAction() {
		super();
	}

    /**
     * Creates a new ConfbridgeUnmuteAction.
     * 
     * @param conference the conference number.
     * @param channel number of the channel in the conference.
     */
	public ConfbridgeUnmuteAction(String conference, String channel) {
		this.setConference(conference);
		this.setChannel(channel);
	}

    /**
     * Returns the name of this action, i.e. "ConfbridgeUnmute".
     */
	@Override
	public String getAction() {
		return "ConfbridgeUnmute";
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
     * Sets the number of the channel to unmute.
     */
	public void setChannel(String channel) {
		this.channel = channel;
	}

    /**
     * Returns the number of the channel to unmute.
     */  
	public String getChannel() {
		return channel;
	}
	
}

