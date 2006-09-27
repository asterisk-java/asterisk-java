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
package org.asteriskjava.live.internal;

import java.util.ArrayList;
import java.util.List;

import org.asteriskjava.live.AsteriskChannel;
import org.asteriskjava.live.AsteriskQueue;

/**
 * Default implementation of the AsteriskQueue interface.
 * 
 * @author srt
 * @version $Id$
 */
class AsteriskQueueImpl extends AbstractLiveObject implements AsteriskQueue
{
    private final String name;
    private Integer max;
    private Integer serviceLevel;
    private Integer weight;
    private final ArrayList<AsteriskChannel> entries;

    AsteriskQueueImpl(AsteriskServerImpl server, String name, Integer max, Integer serviceLevel, Integer weight)
    {
        super(server);
        this.name = name;
        this.max = max;
        this.serviceLevel = serviceLevel;
        this.weight = weight;
        this.entries = new ArrayList<AsteriskChannel>(25);
    }

    public String getName()
    {
        return name;
    }

    public Integer getMax()
    {
        return max;
    }

    void setMax(Integer max)
    {
        this.max = max;
    }

    public Integer getServiceLevel()
    {
        return serviceLevel;
    }

    void setServiceLevel(Integer serviceLevel)
    {
        this.serviceLevel = serviceLevel;
    }

    public Integer getWeight()
    {
        return weight;
    }

    void setWeight(Integer weight)
    {
        this.weight = weight;
    }

    @SuppressWarnings("unchecked")
    public List<AsteriskChannel> getEntries()
    {
        synchronized (entries)
        {
            return (List<AsteriskChannel>) entries.clone();
        }
    }

    void addEntry(AsteriskChannel entry)
    {
        synchronized (entries)
        {
            // only add if not yet there
            if (entries.contains(entry))
            {
                return;
            }
            entries.add(entry);
        }
    }

    void removeEntry(AsteriskChannel entry)
    {
        synchronized (entries)
        {
            entries.remove(entry);
        }
    }

    public String toString()
    {
        final StringBuffer sb;

        sb = new StringBuffer("AsteriskQueue[");
        sb.append("name='" + getName() + "',");
        sb.append("max='" + getMax() + "',");
        sb.append("serviceLevel='" + getServiceLevel() + "',");
        sb.append("weight='" + getWeight() + "',");
        synchronized (entries)
        {
            sb.append("entries='" + entries.toString() + "',");
        }
        sb.append("systemHashcode=" + System.identityHashCode(this));
        sb.append("]");

        return sb.toString();
    }
}
