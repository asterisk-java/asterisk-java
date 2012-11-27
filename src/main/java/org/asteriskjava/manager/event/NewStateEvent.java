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
 * A NewStateEvent is triggered when the state of a channel has changed.<p>
 * It is implemented in <code>channel.c</code>
 * 
 * @author srt
 * @version $Id$
 */
public class NewStateEvent extends AbstractChannelStateEvent
{
    /**
     * Serializable version identifier.
     */
    static final long serialVersionUID = -0L;

    private String connectedlinename;
    private String connectedlinenum;

    public NewStateEvent(Object source)
    {
        super(source);
    }

    /**
     * Returns the Caller*ID name of the channel connected if set.
     * If the channel has no caller id set "unknown" is returned.
     *
     * @since 1.0.0
     */
    public String getConnectedlinename()
    {
        return connectedlinename;
    }

    public void setConnectedlinename(String connectedlinename)
    {
        this.connectedlinename = connectedlinename;
    }

    /**
     * Returns the Caller*ID number of the channel connected if set.
     * If the channel has no caller id set "unknown" is returned.
     *
     * @since 1.0.0
     */
    public String getConnectedlinenum()
    {
        return connectedlinenum;
    }

    public void setConnectedlinenum(String connectedlinenum)
    {
        this.connectedlinenum = connectedlinenum;
    }

}
