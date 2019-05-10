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
 * Abstract base class for several queue member related events.
 * 
 * @author srt
 * @version $Id$
 * @since 0.2
 */
public abstract class AbstractQueueMemberEvent extends ManagerEvent
{
    /**
     * Serializable version identifier
     */
    private static final long serialVersionUID = -7437833328723536814L;
    private String queue;
    private String _interface;
    private String memberName;
    private String pausedReason;
    private String inCall;

    /**
     * @param source
     */
    protected AbstractQueueMemberEvent(Object source)
    {
        super(source);
    }

    /**
     * Returns the name of the queue.
     * 
     * @return the name of the queue.
     */
    public String getQueue()
    {
        return queue;
    }

    /**
     * Sets the name of the queue.
     * 
     * @param queue the name of the queue.
     */
    public void setQueue(String queue)
    {
        this.queue = queue;
    }

    /**
     * Returns the name of the member's interface.<p>
     * E.g. the channel name or agent group.
     * 
     * @return the name of the member's interface.
     */
    final public String getInterface()
    {
        return _interface;
    }

    /**
     * Sets the name of the member's interface.
     * 
     * @param member the name of the member's interface.
     */
    final public void setInterface(String _interface)
    {
        this._interface = _interface;
    }

    /**
     * Returns the name of the member's interface.<p>
     * E.g. the channel name or agent group.
     *
     * @deprecated since Asterisk 12
     *
     * @return the name of the member's interface.
     */
    @Deprecated
    final public String getLocation()
    {
        return _interface;
    }

    /**
     * Sets the name of the member's interface.
     *
     * @deprecated since Asterisk 12
     *
     * @param member the name of the member's interface.
     */
    @Deprecated
    final public void setLocation(String _interface)
    {
        if ((_interface != null) && (!"null".equals(_interface)))
        {  // Location is not in use since asterisk 12
            this._interface = _interface;
        }
    }

    /**
     * Retruns the name of the queue member.
     * <p>
     * Available since Asterisk 1.4
     * 
     * @return the name of the queue member.
     * @since 0.3
     */
    public String getMemberName()
    {
        return memberName;
    }

    /**
     * Sets the name of the queue member.
     * <p>
     * Available since Asterisk 1.4
     * 
     * @param memberName the name of the queue member.
     * @since 0.3
     */
    public void setMemberName(String memberName)
    {
        this.memberName = memberName;
    }

    public String getPausedReason()
    {
        return pausedReason;
    }

    public void setPausedReason(String pausedReason)
    {
        this.pausedReason = pausedReason;
    }

    public String getInCall()
    {
        return inCall;
    }

    public void setInCall(String inCall)
    {
        this.inCall = inCall;
    }
}
