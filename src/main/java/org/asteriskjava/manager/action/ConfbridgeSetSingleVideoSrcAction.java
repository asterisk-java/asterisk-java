package org.asteriskjava.manager.action;

/**
 * The ConfbridgeSetSingleVideoSrcAction sets a conference user as the single video source distributed to all other video-capable participants.
 * 
 * @author jmb
 * @version $Id$
 */
public class ConfbridgeSetSingleVideoSrcAction extends AbstractManagerAction {

	/**
     * Serializable version identifier
     */
	private static final long serialVersionUID = 88241670521642551L;
	
	private String conference;
	private String channel;
	
    /**
     * Creates a new empty ConfbridgeSetSingleVideoSrcAction.
     */
	public ConfbridgeSetSingleVideoSrcAction() {
		super();
	}
		
    /**
     * Returns the name of this action "ConfbridgeSetSingleVideoSrc".
     */
	@Override
	public String getAction() {
		return "ConfbridgeSetSingleVideoSrc";
	}
	
	/**
     * Sets the id of the conference for which the video source is to be set.
     */	
	public void setConference(String conference) {
		this.conference = conference;
	}
	
    /**
     * Returns the id of the conference for which the video source is to be set.
     */ 
	public String getConference() {
		return conference;
	}

	/**
     * Sets the channel which will be the single video source of the conference.
     */	
	public void setChannel(String channel) {
		this.channel = channel;
	}
	
	/**
     * Returns the channel which will be the single video source of the conference.
     */	
	public String getChannel() {
		return channel;
	}

}
