package org.asteriskjava.manager.event;

public class ChannelsHungupListComplete extends ResponseEvent {
    /**
     * Serial version identifier.
     */
    private static final long serialVersionUID = 2531650218749540207L;
    private Integer listItems;

    /**
     * Constructs a ChannelsHungupListComplete
     *
     * @param source The object on which the Event initially occurred.
     * @throws IllegalArgumentException if source is null.
     */
    public ChannelsHungupListComplete(Object source) {
        super(source);
    }

    /**
     * Returns the number of ChannelHungup that have been reported.
     *
     * @return the number of ChannelHungup that have been reported.
     */
    public Integer getListItems() {
        return listItems;
    }

    /**
     * Sets the number of ChannelHungup that have been reported.
     *
     * @param listItems the number of ChannelHungup that have been reported.
     */
    public void setListItems(Integer listItems) {
        this.listItems = listItems;
    }

    public void setEventList(String eventList) {
        // This exists just to silence a warning, the value is always 'Complete'
    }
}
