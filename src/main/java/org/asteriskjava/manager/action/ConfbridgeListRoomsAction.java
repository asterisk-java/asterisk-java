package org.asteriskjava.manager.action;

public class ConfbridgeListRoomsAction extends AbstractManagerAction{


	/**
	 * 
	 */
	private static final long serialVersionUID = 6697372255990670959L;
	
	private String conferenceName;

	@Override
	public String getAction() {
		return "ConfbridgeListRooms";
	}

	public void setConferenceName(String conferenceName) {
		this.conferenceName = conferenceName;
	}

	public String getConferenceName() {
		return conferenceName;
	}

}
