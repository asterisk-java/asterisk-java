package org.asteriskjava.manager.action;

/**
 * The ConfbridgeMuteAction mutes a channel in a conference.
 * 
 * @author jmb
 * @version $Id$
 */
public class ConfbridgeMuteAction extends AbstractManagerAction {

    /**
     * Serializable version identifier
     */
	private static final long serialVersionUID = 1439791922666681989L;

	private String conference;
    private String channel;
    
    /**
     * Creates a new empty ConfbridgeMuteAction.
     */
	public ConfbridgeMuteAction() {
		super();
	}

    /**
     * Creates a new ConfbridgeMuteAction.
     * 
     * @param conference the conference number.
     * @param channel number of the channel in the conference.
     */
	public ConfbridgeMuteAction(String conference, String channel) {
		this.setConference(conference);
		this.setChannel(channel);
	}
	
    /**
     * Returns the name of this action, i.e. "ConfbridgeMute".
     */
	@Override
	public String getAction() {
		return "ConfbridgeMute";
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
     * Sets the number of the channel to mute.
     */
	public void setChannel(String channel) {
		this.channel = channel;
	}
	
	public String getChannel() {
		return channel;
	}
	
}
