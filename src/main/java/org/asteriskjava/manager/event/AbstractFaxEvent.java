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
 * An AbstractFaxEvent is a base class for fax related events
 */
public class AbstractFaxEvent extends ManagerEvent
{
    /**
     * Serial version identifier.
     */
    private static final long serialVersionUID = -1L;
    private String channel;
    private Integer faxSession;

    public AbstractFaxEvent(Object source)
    {
        super(source);
    }


    /**
     * @return the channel
     */
    public String getChannel()
    {
        return channel;
    }


    /**
     * @param channel the channel to set
     */
    public void setChannel(String channel)
    {
        this.channel = channel;
    }


    /**
     * @return the faxSession
     */
    public Integer getFaxSession()
    {
        return faxSession;
    }


    /**
     * @param faxSession the faxSession to set
     */
    public void setFaxSession(Integer faxSession)
    {
        this.faxSession = faxSession;
    }

}
