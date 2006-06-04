package org.asteriskjava.live.internal;

import org.asteriskjava.live.ManagerCommunicationException;
import org.asteriskjava.manager.EventTimeoutException;

/**
 * Maps exceptions received from
 * {@link org.asteriskjava.manager.ManagerConnection} to the corresponding
 * {@link org.asteriskjava.live.ManagerCommunicationException}.
 * 
 * @author srt
 * @version $Id$
 */
class ManagerCommunicationExceptionMapper
{
    // hide constructor
    private ManagerCommunicationExceptionMapper()
    {

    }

    /**
     * Maps exceptions received from
     * {@link org.asteriskjava.manager.ManagerConnection} when sending a
     * {@link org.asteriskjava.manager.action.ManagerAction} to the corresponding
     * {@link org.asteriskjava.live.ManagerCommunicationException}.
     * 
     * @param actionName name of the action that has been tried to send
     * @param e exception received
     * @return the corresponding ManagerCommunicationException
     */
    static ManagerCommunicationException mapSendActionException(String actionName, Exception e)
    {
        if (e instanceof IllegalStateException)
        {
            return new ManagerCommunicationException("Not connected to Asterisk Server", e);
        }
        else if (e instanceof EventTimeoutException)
        {
            return new ManagerCommunicationException("Timeout waiting for events from " + actionName + "Action", e);
        }
        else
        {
            return new ManagerCommunicationException("Unable to send " + actionName + "Action", e);
        }
    }
}
