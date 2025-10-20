package org.asteriskjava.manager.event;

import org.asteriskjava.ami.action.api.response.event.ResponseEvent;
import org.asteriskjava.manager.action.QueueSummaryAction;

/**
 * A QueueSummaryCompleteEvent is triggered after the summary for all requested
 * queues has been reported in response to a QueueSummaryAction.
 *
 * @author srt
 * @version $Id$
 * @see QueueSummaryAction
 * @see QueueSummaryEvent
 * @since 0.3
 */
public class QueueSummaryCompleteEvent extends ResponseEvent {
    /**
     * Serial version identifier.
     */
    private static final long serialVersionUID = -5044247858568827143L;

    /**
     * ??
     */
    private String eventList;

    /**
     * The number of listed items from queueSummaryAction.
     */
    private Integer listItems;

    public QueueSummaryCompleteEvent(Object source) {
        super(source);
    }

    /**
     * Returns eventList variable.
     *
     * @return String value.
     */

    public String getEventlist() {
        return eventList;
    }

    /**
     * Sets eventList variable.
     *
     * @param eventList variable
     */

    public void setEventlist(String eventList) {
        this.eventList = eventList;
    }

    /**
     * Returns the number of listed items.
     *
     * @return listItems size.
     */

    public Integer getListitems() {
        return listItems;
    }

    /**
     * Sets the returnItems value.
     *
     * @param listItems variable.
     */

    public void setListitems(Integer listItems) {
        this.listItems = listItems;
    }
}
