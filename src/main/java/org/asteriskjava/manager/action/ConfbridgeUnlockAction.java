package org.asteriskjava.manager.action;

/**
 * The ConfbridgeUnlockAction unlocks a specified conference.
 * 
 * @author jmb
 * @version $Id$
 */
public class ConfbridgeUnlockAction extends AbstractManagerAction {

	/**
     * Serializable version identifier
     */
	private static final long serialVersionUID = 88241670521642551L;
	
	private String conference;
	
    /**
     * Creates a new empty ConfbridgeUnlockAction.
     */
	public ConfbridgeUnlockAction() {
		super();
	}
	
	/**
     * Creates a new ConfbridgeUnlockAction for a specific conference.
     */	
	public ConfbridgeUnlockAction(String conference) {
		this.setConference(conference);
	}
	
    /**
     * Returns the name of this action "ConfbridgeUnlock".
     */
	@Override
	public String getAction() {
		return "ConfbridgeUnlock";
	}
	
	/**
     * Sets the id of the conference to unlock.
     */	
	public void setConference(String conference) {
		this.conference = conference;
	}
	
    /**
     * Returns the id of the conference to unlock.
     */ 
	public String getConference() {
		return conference;
	}

}
