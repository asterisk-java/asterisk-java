package org.asteriskjava.manager.action;

import org.asteriskjava.manager.event.ConfbridgeListCompleteEvent;
import org.asteriskjava.manager.event.ResponseEvent;

/**
 * Lists all users in a particular ConfBridge conference. ConfbridgeList will follow as separate events,
 * followed by a final event called ConfbridgeListComplete.
 *
 * @since 1.0.0
 */
public class ConfbridgeListAction extends AbstractManagerAction implements EventGeneratingAction
{
    private static final long serialVersionUID = 1L;
    private String conference;

    public ConfbridgeListAction()
    {
    }

    public ConfbridgeListAction(String conference)
    {
        this.conference = conference;
    }

    public void setConference(String conference)
    {
        this.conference = conference;
    }

    public String getConference()
    {
        return conference;
    }

    @Override
    public String getAction()
    {
        return "ConfbridgeList";
    }

    @Override
    public Class<? extends ResponseEvent> getActionCompleteEventClass()
    {
        return ConfbridgeListCompleteEvent.class;
    }
}
