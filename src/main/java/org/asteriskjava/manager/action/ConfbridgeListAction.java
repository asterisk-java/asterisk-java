package org.asteriskjava.manager.action;

public class ConfbridgeListAction extends AbstractManagerAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
    private String conference;    

	public ConfbridgeListAction() {
	}
	
	public ConfbridgeListAction(String conference) {
		this.conference = conference;
	}
	
    public void setConference(String conference) {
        this.conference = conference;
    }
    
    public String getConference() {
        return conference;
    }

	@Override
	public String getAction() {
		return "ConfbridgeList";
	}

}
