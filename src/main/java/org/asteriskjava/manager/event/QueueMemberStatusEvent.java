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
 * A QueueMemberStatusEvent shows the status of a QueueMemberEvent
 * 
 * @author Asteria Solutions Group, Inc. http://www.asteriasgi.com/
 * @version $Id$
 */
public class QueueMemberStatusEvent extends QueueMemberEvent
{
    /**
     * Serializable version identifier
     */
    private static final long serialVersionUID = -2293926744791895763L;

    private String ringinuse;
    private String iface;
    private Integer incall;

    /**
     * @param source
     */
    public QueueMemberStatusEvent(Object source)
    {
        super(source);
    }

    public String getInterface()
    {
        return iface;
    }

    public void setInterface(String iface)
    {
        this.iface = iface;
    }

    /**
     * @return the ringinuse
     */
    public String getRinginuse()
    {
        return ringinuse;
    }

    /**
     * @param ringinuse the ringinuse to set
     */
    public void setRinginuse(String ringinuse)
    {
        this.ringinuse = ringinuse;
    }

    public Integer getIncall()
    {
        return incall;
    }

    public void setIncall(Integer incall)
    {
        this.incall = incall;
    }
    
    
}
