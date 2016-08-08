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
 * Response to an OriginateAction.
 * 
 * @see org.asteriskjava.manager.action.OriginateAction
 * @author srt
 * @version $Id$
 */
public class OriginateResponseEvent extends ResponseEvent
{
    /**
     * Serial version identifier.
     */
    private static final long serialVersionUID = 910724860608259687L;
    private String response;
    private String channel;
    private String uniqueId;
    private Integer reason;
    private String data;
    private String application;

    /**
     * @param source
     */
    public OriginateResponseEvent(Object source)
    {
        super(source);
    }

    /**
     * Returns the result of the corresponding Originate action.
     * 
     * @return "Success" or "Failure"
     */
    public String getResponse()
    {
        return response;
    }

    /**
     * Sets the result of the corresponding Originate action.
     * 
     * @param response "Success" or "Failure"
     */
    public void setResponse(String response)
    {
        this.response = response;
    }
    
    public boolean isSuccess()
    {
        return "Success".equalsIgnoreCase(response);
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

    public Integer getReason()
    {
        return reason;
    }

    public void setReason(Integer reason)
    {
        this.reason = reason;
    }

    /**
     * Returns the unique id of the originated channel.
     * 
     * @return the unique id of the originated channel or "&lt;null&gt;" if none
     *         is available.
     */
    public String getUniqueId()
    {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId)
    {
        this.uniqueId = uniqueId;
    }

    // for backward compatibility only
    public void setCallerId(String callerId)
    {
        if (getCallerIdNum() == null)
        {
            setCallerIdNum(callerId);
        }
    }

    public String getData()
    {
        return data;
    }

    public void setData(String data)
    {
        this.data = data;
    }

    public String getApplication()
    {
        return application;
    }

    public void setApplication(String application)
    {
        this.application = application;
    }
}
