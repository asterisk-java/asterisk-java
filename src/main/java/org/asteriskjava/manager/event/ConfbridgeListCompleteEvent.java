package org.asteriskjava.manager.event;

public class ConfbridgeListCompleteEvent extends ResponseEvent
{
    /**
     * Serializable version identifier
     */
    private static final long serialVersionUID = 1L;

    private String eventList;
    private String listItems;

    /**
     * @param source
     */
    public ConfbridgeListCompleteEvent(Object source)
    {
        super(source);
    }

    /**
     * Sets the status of the list e.g. complete.
     */
    public void setEventList(String eventList)
    {
        this.eventList = eventList;
    }

    /**
     * Returns the status of the list e.g. complete.
     */
    public String getEventList()
    {
        return eventList;
    }

    /**
     * Sets the number listitems.
     */
    public void setListItems(String listItems)
    {
        this.listItems = listItems;
    }

    /**
     * Returns the number listitems.
     */
    public String getListItems()
    {
        return listItems;
    }
}
