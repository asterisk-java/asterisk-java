package org.asteriskjava.manager.action;

/**
 * The ConfbridgeStopAction stops an audio recording of a conference.
 * 
 * @author jmb
 * @version $Id$
 */
public class ConfbridgeStopRecordAction extends AbstractManagerAction {

    /**
     * Serializable version identifier
     */
	private static final long serialVersionUID = 7429055239528793083L;
	
	private String conference;    

    /**
     * Creates a new empty ConfbridgeStopRecordAction.
     */
	public ConfbridgeStopRecordAction() {
		super();
	}
	
	/**
     * Creates a new ConfbridgeStopRecordAction for a specific conference.
     */	
	public ConfbridgeStopRecordAction(String conference) {
		this.setConference(conference);
	}
	
    /**
     * Returns the name of this action, i.e. "ConfbridgeStopRecord".
     */
	@Override
	public String getAction() {
		return "ConfbridgeStopRecord";
	}

	/**
     * Sets the id of the conference for which to stop an audio recording.
     */		
	public void setConference(String conference) {
		this.conference = conference;
	}

    /**
     * Returns the id of the conference for which to stop an audio recording.
     */ 
	public String getConference() {
		return conference;
	}

}
