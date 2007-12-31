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
 * Abstract base class providing common properties for LinkEvent and
 * UnlinkEvent.
 * 
 * @author srt
 * @version $Id$
 */
public abstract class LinkageEvent extends ManagerEvent
{
    /**
     * Serializable version identifier
     */
    static final long serialVersionUID = 6563044930071030273L;

    private String uniqueId1;
    private String uniqueId2;
    private String channel1;
    private String channel2;
    private String callerId1;
    private String callerId2;

    /**
     * @param source
     */
    public LinkageEvent(Object source)
    {
        super(source);
    }

    /**
     * Returns the unique id of the first channel.
     */
    public String getUniqueId1()
    {
        return uniqueId1;
    }

    /**
     * Sets the unique id of the first channel.
     */
    public void setUniqueId1(String uniqueId1)
    {
        this.uniqueId1 = uniqueId1;
    }

    /**
     * Returns the unique id of the second channel.
     */
    public String getUniqueId2()
    {
        return uniqueId2;
    }

    /**
     * Sets the unique id of the second channel.
     */
    public void setUniqueId2(String uniqueId2)
    {
        this.uniqueId2 = uniqueId2;
    }

    /**
     * Returns the name of the first channel.
     */
    public String getChannel1()
    {
        return channel1;
    }

    /**
     * Sets the name of the first channel.
     */
    public void setChannel1(String channel1)
    {
        this.channel1 = channel1;
    }

    /**
     * Returns the name of the second channel.
     */
    public String getChannel2()
    {
        return channel2;
    }

    /**
     * Sets the name of the second channel.
     */
    public void setChannel2(String channel2)
    {
        this.channel2 = channel2;
    }

    /**
     * Returns the Caller*Id number of the first channel.
     * 
     * @return the Caller*Id number of the first channel.
     * @since 0.2
     */
    public String getCallerId1()
    {
        return callerId1;
    }

    /**
     * Sets the Caller*Id number of the first channel.
     * 
     * @param callerId1 the Caller*Id number of the first channel.
     * @since 0.2
     */
    public void setCallerId1(String callerId1)
    {
        this.callerId1 = callerId1;
    }

    /**
     * Returns the Caller*Id number of the second channel.
     * 
     * @return the Caller*Id number of the second channel.
     * @since 0.2
     */
    public String getCallerId2()
    {
        return callerId2;
    }

    /**
     * Sets the Caller*Id number of the second channel.
     * 
     * @param callerId1 the Caller*Id number of the second channel.
     * @since 0.2
     */
    public void setCallerId2(String callerId2)
    {
        this.callerId2 = callerId2;
    }
}
