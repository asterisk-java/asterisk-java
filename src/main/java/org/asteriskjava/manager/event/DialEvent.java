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
 * A dial event is triggered whenever a phone attempts to dial someone.<br>
 * This event is implemented in <code>apps/app_dial.c</code>.<br>
 * Available since Asterisk 1.2.
 * 
 * @author Asteria Solutions Group, Inc. <http://www.asteriasgi.com/>
 * @version $Id$
 * @since 0.2
 */
public class DialEvent extends ManagerEvent
{
    /**
     * Serializable version identifier
     */
    private static final long serialVersionUID = 3258130241292417336L;

    /**
     * The name of the source channel.
     */
    private String src;

    /**
     * The name of the destination channel.
     */
    private String destination;

    /**
     * The new Caller*ID.
     */
    private String callerId;

    /**
     * The new Caller*ID Name.
     */
    private String callerIdName;

    /**
     * The unique id of the source channel.
     */
    private String srcUniqueId;

    /**
     * The unique id of the destination channel.
     */
    private String destUniqueId;

    /**
     * Creates a new DialEvent.
     * 
     * @param source
     */
    public DialEvent(Object source)
    {
        super(source);
    }

    /**
     * Returns the name of the source channel.
     * 
     * @return the name of the source channel.
     */
    public String getSrc()
    {
        return src;
    }

    /**
     * Sets the name of the source channel.
     * 
     * @param src the name of the source channel.
     */
    public void setSrc(String src)
    {
        this.src = src;
    }

    /**
     * Returns the name of the destination channel.
     * 
     * @return the name of the destination channel.
     */
    public String getDestination()
    {
        return destination;
    }

    /**
     * Sets the name of the destination channel.
     * 
     * @param destination the name of the destination channel.
     */
    public void setDestination(String destination)
    {
        this.destination = destination;
    }

    /**
     * Returns the Caller*ID.
     * 
     * @return the Caller*ID or "<unknown>" if none has been set.
     */
    public String getCallerId()
    {
        return callerId;
    }

    /**
     * Sets the caller*ID.
     * 
     * @param callerId the caller*ID.
     */
    public void setCallerId(String callerId)
    {
        this.callerId = callerId;
    }

    /**
     * Returns the Caller*ID Name.
     * 
     * @return the Caller*ID Name or "<unknown>" if none has been set.
     */
    public String getCallerIdName()
    {
        return callerIdName;
    }

    /**
     * Sets the Caller*Id Name.
     * 
     * @param callerIdName the Caller*Id Name to set.
     */
    public void setCallerIdName(String callerIdName)
    {
        this.callerIdName = callerIdName;
    }

    /**
     * Returns the unique ID of the source channel.
     * 
     * @return the unique ID of the source channel.
     */
    public String getSrcUniqueId()
    {
        return srcUniqueId;
    }

    /**
     * Sets the unique ID of the source channel.
     * 
     * @param srcUniqueId the unique ID of the source channel.
     */
    public void setSrcUniqueId(String srcUniqueId)
    {
        this.srcUniqueId = srcUniqueId;
    }

    /**
     * Returns the unique ID of the distination channel.
     * 
     * @return the unique ID of the distination channel.
     */
    public String getDestUniqueId()
    {
        return destUniqueId;
    }

    /**
     * Sets the unique ID of the distination channel.
     * 
     * @param destUniqueId the unique ID of the distination channel.
     */
    public void setDestUniqueId(String destUniqueId)
    {
        this.destUniqueId = destUniqueId;
    }
}
