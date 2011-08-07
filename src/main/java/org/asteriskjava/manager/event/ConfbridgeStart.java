package org.asteriskjava.manager.event;

/**
 * A ConfbridgeStart event is triggered when the first participant joins a conference.
 * 
 * @author jmb
 * @version $Id$
 */
public class ConfbridgeStart extends ManagerEvent {

	/**
     * Serializable version identifier
     */
	private static final long serialVersionUID = 5892869247458804705L;
	
	private String conference;

	/**
     * @param source
     */
	public ConfbridgeStart(Object source) {
		super(source);
	}

	/**
     * Sets the id of the conference started.
     */	
    public void setConference(String conference)
    {
        this.conference = conference;
    }
    
    /**
     * Returns the id of the conference started.
     */ 
    public String getConference()
    {
        return conference;
    }

}
