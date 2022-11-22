package org.asteriskjava.manager.event;

/**
 * A ShowDialplanCompleteEvent is triggered after the dialplan has been reported
 * in response to a ShowDialplanAction.<p>
 * Available since Asterisk 1.6<p>
 * It is implemented in <code>main/pbx.c</code>
 *
 * @author srt
 * @version $Id$
 * @see org.asteriskjava.manager.action.ShowDialplanAction
 * @see org.asteriskjava.manager.event.ListDialplanEvent
 * @since 1.0.0
 */
public class ShowDialplanCompleteEvent extends ResponseEvent {
    private static final long serialVersionUID = 1L;

    private String eventList;
    private Integer listItems;
    private Integer listExtensions;
    private Integer listPriorities;
    private Integer listContexts;

    public ShowDialplanCompleteEvent(Object source) {
        super(source);
    }

    public String getEventList() {
        return eventList;
    }

    public void setEventList(String eventList) {
        this.eventList = eventList;
    }

    /**
     * Returns the total number of list items reported.
     *
     * @return the total number of list items reported.
     */
    public Integer getListItems() {
        return listItems;
    }

    public void setListItems(Integer listItems) {
        this.listItems = listItems;
    }

    /**
     * Returns the number of extensions reported.
     *
     * @return the number of extensions reported.
     */
    public Integer getListExtensions() {
        return listExtensions;
    }

    public void setListExtensions(Integer listExtensions) {
        this.listExtensions = listExtensions;
    }

    /**
     * Returns the number of priorites reported.
     *
     * @return the number of priorites reported.
     */
    public Integer getListPriorities() {
        return listPriorities;
    }

    public void setListPriorities(Integer listPriorities) {
        this.listPriorities = listPriorities;
    }

    /**
     * Returns the number of contexts reported.
     *
     * @return the number of contexts reported.
     */
    public Integer getListContexts() {
        return listContexts;
    }

    public void setListContexts(Integer listContexts) {
        this.listContexts = listContexts;
    }
}
