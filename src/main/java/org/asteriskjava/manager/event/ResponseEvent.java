/*
 *  Copyright 2004-2006 Stefan Reuter
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */
package org.asteriskjava.manager.event;

/**
 * Abstract base class for events triggered in response to a ManagerAction.<br>
 * All ResponseEvents contain an additional action id property that links the
 * event to the action that caused it.
 * 
 * @see org.asteriskjava.manager.action.ManagerAction
 * @author srt
 * @version $Id: ResponseEvent.java,v 1.3 2005/07/16 00:16:02 srt Exp $
 */
public abstract class ResponseEvent extends ManagerEvent
{
    private String actionId;
    private String internalActionId;

    /**
     * @param source
     */
    public ResponseEvent(Object source)
    {
        super(source);
    }

    /**
     * Returns the user provided action id of the ManagerAction that caused 
     * this event. If the application did not set an action id this method
     * returns <code>null</code>.
     * 
     * @return the action id of the ManagerAction that caused this event or
     *         <code>null</code> if none was set.
     * @see org.asteriskjava.manager.action.ManagerAction#setActionId()
     */
    public String getActionId()
    {
        return actionId;
    }

    /**
     * Sets the action id of the ManagerAction that caused this event.
     * 
     * @param actionId the action id of the ManagerAction that caused this
     *            event.
     */
    public void setActionId(String actionId)
    {
        this.actionId = actionId;
    }

    /**
     * Returns the internal action id of the ManagerAction that caused this
     * event.<br>
     * Warning: This method is internal to Asterisk-Java and should never be
     * used in application code.
     * 
     * @return the internal action id of the ManagerAction that caused this
     *         event.
     * @since 0.2
     */
    public String getInternalActionId()
    {
        return internalActionId;
    }

    /**
     * Sets the internal action id of the ManagerAction that caused this event.
     * 
     * @param internalActionId the internal action id of the ManagerAction that
     *            caused this event.
     * @since 0.2
     */
    public void setInternalActionId(String internalActionId)
    {
        this.internalActionId = internalActionId;
    }
}
