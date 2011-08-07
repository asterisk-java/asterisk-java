package org.asteriskjava.manager.action;

/**
 * The ConfbridgeKickAction kicks a channel out of a conference.
 * 
 * @author jmb
 * @version $Id$
 */
public class ConfbridgeKickAction extends AbstractManagerAction {
	
    /**
     * Serializable version identifier
     */
	private static final long serialVersionUID = 3827556611709875112L;
	
	private String conference;
    private String channel;

    /**
     * Creates a new empty ConfbridgeKickAction.
     */
	public ConfbridgeKickAction() {
		super();
	}
	
    /**
     * Creates a new ConfbridgeKickAction.
     * 
     * @param conference the conference number.
     * @param channel number of the channel in the conference.
     */
	public ConfbridgeKickAction(String conference, String channel) {
		this.setConference(conference);
		this.setChannel(channel);
	}

    /**
     * Returns the name of this action, i.e. "ConfbridgeKick".
     */
	@Override
	public String getAction() {
		return "ConfbridgeKick";
	}

	/**
     * Sets the id of the conference to kick a channel from.
     */	
	public void setConference(String conference) {
		this.conference = conference;
	}

    /**
     * Returns the id of the conference to kick a channel from.
     */ 
	public String getConference() {
		return conference;
	}

    /**
     * Sets the number of the channel to kick.
     */
	public void setChannel(String channel) {
		this.channel = channel;
	}

    /**
     * Returns the number of the channel to kick.
     */  
	public String getChannel() {
		return channel;
	}

}
