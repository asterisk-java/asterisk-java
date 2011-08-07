package org.asteriskjava.manager.event;

public class ConfbridgeListRoomsCompleteEvent extends ResponseEvent
{
    /**
     * Serializable version identifier
     */
    private static final long serialVersionUID = 1L;

    private String eventList;
    private String listItems;

    public ConfbridgeListRoomsCompleteEvent(Object source)
    {
        super(source);
    }

    /**
     * Sets the status of the list that is always "Complete".
     */
    public void setEventList(String eventList)
    {
        this.eventList = eventList;
    }

    /**
     * Returns the status of the list that is always "Complete".
     */
    public String getEventList()
    {
        return eventList;
    }

    /**
     * Sets the number items returned.
     *
     * @param listItems the number items returned.
     */
    public void setListItems(String listItems)
    {
        this.listItems = listItems;
    }

    /**
     * Returns the number listitems.
     *
     * @return the number items returned.
     */
    public String getListItems()
    {
        return listItems;
    }
}
