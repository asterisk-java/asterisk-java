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
 * Abstract base class providing common properties for AyncAgiEvent and AgiExecEvent.
 *
 * @author srt
 * @version $Id$
 * @since 1.0.0
 * @see org.asteriskjava.manager.action.AgiAction
 */
public abstract class AbstractAgiEvent extends AbstractChannelEvent
{
    public static final String SUB_EVENT_START = "Start";
    public static final String SUB_EVENT_EXEC = "Exec";
    public static final String SUB_EVENT_END = "End";

    /**
     * Serializable version identifier.
     */
    static final long serialVersionUID = 0L;

    private String subEvent;
    private String commandId;
    private String result;

    /**
     * Creates a new AbstractAgiEvent.
     *
     * @param source
     */
    protected AbstractAgiEvent(Object source)
    {
        super(source);
    }

    /**
     * Returns the sub event type. This is either "Start", "Exec" or "End".
     *
     * @return the sub event type.
     * @see #SUB_EVENT_START
     * @see #SUB_EVENT_EXEC
     * @see #SUB_EVENT_END
     */
    public String getSubEvent()
    {
        return subEvent;
    }

    /**
     * Sets the sub event type.
     *
     * @param subEvent the sub event type.
     */
    public void setSubEvent(String subEvent)
    {
        this.subEvent = subEvent;
    }

    /**
     * Returns the command id.
     *
     * @return the command id.
     * @see org.asteriskjava.manager.action.AgiAction#setCommandId(String)
     */
    public String getCommandId()
    {
        return commandId;
    }

    /**
     * Sets the command id.
     *
     * @param commandId the command id.
     */
    public void setCommandId(String commandId)
    {
        this.commandId = commandId;
    }

    public String getResult()
    {
        return result;
    }

    public void setResult(String result)
    {
        this.result = result;
    }

    /**
     * Checks is this a start sub event.
     *
     * @return <code>true</code> if this is a start sub event, <code>false</code> otherwise.
     */
    public boolean isStart()
    {
        return isSubEvent(SUB_EVENT_START);
    }

    /**
     * Checks is this an exec sub event.
     *
     * @return <code>true</code> if this is an exec sub event, <code>false</code> otherwise.
     */
    public boolean isExec()
    {
        return isSubEvent(SUB_EVENT_EXEC);
    }

    /**
     * Checks is this an end sub event.
     *
     * @return <code>true</code> if this is an end sub event, <code>false</code> otherwise.
     */
    public boolean isEnd()
    {
        return isSubEvent(SUB_EVENT_END);
    }

    private boolean isSubEvent(String subEvent)
    {
        return this.subEvent != null && this.subEvent.equalsIgnoreCase(subEvent);
    }
}