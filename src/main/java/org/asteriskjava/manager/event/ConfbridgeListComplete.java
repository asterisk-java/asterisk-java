package org.asteriskjava.manager.event;

public class ConfbridgeListComplete extends ManagerEvent {

	/**
     * Serializable version identifier
     */
	private static final long serialVersionUID = 4847288548583149517L;
	
	private String eventList;
	private String listItems;
	private String actionId;

	/**
     * @param source
     */
	public ConfbridgeListComplete(Object source) {
		super(source);
	}

    /**
     * Sets the status of the list e.g. complete.
     */
	public void setEventList(String eventList) {
		this.eventList = eventList;
	}
	
    /**
     * Returns the status of the list e.g. complete.
     */
	public String getEventList() {
		return eventList;
	}
	
    /**
     * Sets the number listitems.
     */
	public void setListItems(String listItems) {
		this.listItems = listItems;
	}
	
    /**
     * Returns the number listitems.
     */
	public String getListItems() {
		return listItems;
	}
	
    /**
     * Sets the actionid.
     */
	public void setActionId(String actionId) {
		this.actionId = actionId;
	}

    /**
     * Returns the actionid.
     */
	public String getActionId() {
		return actionId;
	}
	
}
