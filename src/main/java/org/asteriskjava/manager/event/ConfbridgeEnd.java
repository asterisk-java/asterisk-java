package org.asteriskjava.manager.event;

/**
 * A ConfbridgeEnd event is triggered when the last participant leaves a conference.<p>
 * 
 * @author jmb
 * @version $Id$
 */
public class ConfbridgeEnd extends ManagerEvent {

	/**
     * Serializable version identifier
     */
	private static final long serialVersionUID = -8973512592594074108L;

	private String conference;

	/**
     * @param source
     */
	public ConfbridgeEnd(Object source) {
		super(source);
	}
	
	/**
     * Sets the id of the conference ended.
     */	
    public void setConference(String conference)
    {
        this.conference = conference;
    }

    /**
     * Returns the id of the conference ended.
     */ 
    public String getConference()
    {
        return conference;
    }

}
