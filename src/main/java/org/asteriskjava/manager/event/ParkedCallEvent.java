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
 * A ParkedCallEvent is triggered when a channel is parked (in this case no
 * action id is set) and in response to a ParkedCallsAction.
 * <p>
 * It is implemented in <code>res/res_features.c</code>
 * 
 * @see org.asteriskjava.manager.action.ParkedCallsAction
 * @author srt
 * @version $Id$
 */
public class ParkedCallEvent extends AbstractParkedCallEvent
{
    /**
     * Serializable version identifier
     */
    private static final long serialVersionUID = 0L;

    private Integer timeout;
    private String connectedlinenum;
    private String connectedlinename;
    private String parkeelinkedid;

    /**
     * @param source
     */
    public ParkedCallEvent(Object source)
    {
        super(source);
    }

    /**
     * Returns the number of seconds this call will be parked.
     * <p>
     * This corresponds to the <code>parkingtime</code> option in
     * <code>features.conf</code>.
     */
    public Integer getTimeout()
    {
        return timeout;
    }

    /**
     * Sets the number of seconds this call will be parked.
     */
    public void setTimeout(Integer timeout)
    {
        this.timeout = timeout;
    }

    /**
     * Sets the unique id of the parked channel as a workaround for a typo in
     * asterisk manager event.
     */
    public void setUnqiueId(String unqiueId)
    {
        setUniqueId(unqiueId);
    }

    
    public String getConnectedLinenum()
    {
        return connectedlinenum;
    }


    public void setConnectedLinenum(String connectedlinenum)
    {
        this.connectedlinenum = connectedlinenum;
    }

    public String getConnectedLinename()
    {
        return connectedlinename;
    }


    public void setConnectedLinename(String connectedlinename)
    {
        this.connectedlinename = connectedlinename;
    }

    /**
     * @param parkeelinkedid the parkeelinkedid to set
     */
    public void setParkeelinkedid(String parkeelinkedid) {
        this.parkeelinkedid = parkeelinkedid;
    }

    /**
     * @return the parkeelinkedid
     */
    public String getParkeelinkedid() {
        return parkeelinkedid;
    }


}
