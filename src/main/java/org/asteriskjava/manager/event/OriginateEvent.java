/*
 *  Copyright  2004-2006 Stefan Reuter
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
 * Abstract base class for events triggered in response to an OriginateAction.
 * 
 * @see org.asteriskjava.manager.action.OriginateAction
 * @author srt
 * @version $Id: OriginateEvent.java,v 1.3 2005/06/08 02:21:31 srt Exp $
 */
public abstract class OriginateEvent extends ResponseEvent
{
    private String channel;
    private String context;
    private String exten;
    private String uniqueId;
    private Integer reason;

    /**
     * @param source
     */
    public OriginateEvent(Object source)
    {
        super(source);
    }

    /**
     * Returns the name of the channel to connect to the outgoing call.
     */
    public String getChannel()
    {
        return channel;
    }

    /**
     * Sets the name of the channel to connect to the outgoing call.
     */
    public void setChannel(String channel)
    {
        this.channel = channel;
    }

    /**
     * Returns the name of the context of the extension to connect to.
     */
    public String getContext()
    {
        return context;
    }

    /**
     * Sets the name of the context of the extension to connect to.
     */
    public void setContext(String context)
    {
        this.context = context;
    }

    /**
     * Returns the the extension to connect to.
     */
    public String getExten()
    {
        return exten;
    }

    /**
     * Sets the the extension to connect to.
     */
    public void setExten(String exten)
    {
        this.exten = exten;
    }

    public Integer getReason()
    {
        return reason;
    }

    public void setReason(Integer reason)
    {
        this.reason = reason;
    }

    public String getUniqueId()
    {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId)
    {
        this.uniqueId = uniqueId;
    }
}
