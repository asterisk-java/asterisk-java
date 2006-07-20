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
 * An ExtensionStatusEvent is triggered when the state of an extension changes.
 * <p>
 * It is implemented in <code>manager.c</code>
 * 
 * @author srt
 * @version $Id$
 */
public class ExtensionStatusEvent extends ManagerEvent
{
    /**
     * Serializable version identifier
     */
    private static final long serialVersionUID = -6459014125704286869L;
    private String exten;
    private String context;
    private Integer status;
    private String callerId;

    /**
     * @param source
     */
    public ExtensionStatusEvent(Object source)
    {
        super(source);
    }

    /**
     * Returns the extension.
     */
    public String getExten()
    {
        return exten;
    }

    /**
     * Sets the extension.
     */
    public void setExten(String exten)
    {
        this.exten = exten;
    }

    /**
     * Returns the context of the extension.
     */
    public String getContext()
    {
        return context;
    }

    /**
     * Sets the context of the extension.
     */
    public void setContext(String context)
    {
        this.context = context;
    }

    /**
     * Returns the state of the extension.
     */
    public Integer getStatus()
    {
        return status;
    }

    /**
     * Sets the state of the extension.
     */
    public void setStatus(Integer status)
    {
        this.status = status;
    }

    /**
     * Returns the Caller*ID in the form <code>"Some Name" &lt;1234&gt;</code>.
     * <p>
     * This property is only available on BRIstuffed Asterisk servers.
     * 
     * @return the Caller*ID.
     */
    public String getCallerId()
    {
        return callerId;
    }

    /**
     * Sets the Caller*ID.
     * 
     * @param callerId the Caller*ID.
     */
    public void setCallerId(String callerId)
    {
        this.callerId = callerId;
    }
}
