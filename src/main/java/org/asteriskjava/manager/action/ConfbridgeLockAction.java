package org.asteriskjava.manager.action;

/**
 * The ConfbridgeLockAction lockes a specified conference.
 * 
 * @author jmb
 * @version $Id$
 */
public class ConfbridgeLockAction extends AbstractManagerAction {

	/**
     * Serializable version identifier
     */
	private static final long serialVersionUID = 6146611916322541533L;
	
	private String conference;
	
    /**
     * Creates a new empty ConfbridgeLockAction.
     */
	public ConfbridgeLockAction() {
		super();
	}
	
	/**
     * Creates a new ConfbridgeLockAction for a specific conference.
     */	
	public ConfbridgeLockAction(String conference) {
		this.setConference(conference);
	}
	
    /**
     * Returns the name of this action "ConfbridgeLock".
     */
	@Override
	public String getAction() {
		return "ConfbridgeLock";
	}
	
	/**
     * Sets the id of the conference to lock.
     */	
	public void setConference(String conference) {
		this.conference = conference;
	}
	
    /**
     * Returns the id of the conference to lock.
     */ 
	public String getConference() {
		return conference;
	}

}
