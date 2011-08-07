package org.asteriskjava.manager.action;

/**
 * The ConfbridgeStartAction starts an audio recording of a conference.
 * 
 * @author jmb
 * @version $Id$
 */
public class ConfbridgeStartRecordAction extends AbstractManagerAction {

    /**
     * Serializable version identifier
     */
	private static final long serialVersionUID = 3059632508521358701L;
	
	private String conference;
	
    /**
     * Creates a new empty ConfbridgeStartRecordAction.
     */
	public ConfbridgeStartRecordAction() {
		super();
	}
	
	/**
     * Creates a new ConfbridgeStartRecordAction for a specific conference.
     */	
	public ConfbridgeStartRecordAction(String conference) {
		this.setConference(conference);
	}
	
    /**
     * Returns the name of this action, i.e. "ConfbridgeStartRecord".
     */
	@Override
	public String getAction() {
		return "ConfbridgeStartRecord";
	}
	
	/**
     * Sets the id of the conference for which to start an audio recording.
     */	
	public void setConference(String conference) {
		this.conference = conference;
	}
	
    /**
     * Returns the id of the conference for which to start an audio recording.
     */ 
	public String getConference() {
		return conference;
	}

}
