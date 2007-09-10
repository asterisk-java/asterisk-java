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
import org.asteriskjava.live.AsteriskQueueListener;
import org.asteriskjava.util.Log;
import org.asteriskjava.util.LogFactory;

/**
 * Default implementation of the AsteriskQueue interface.
 * 
 * @author srt
 * @version $Id$
 */
class AsteriskQueueImpl extends AbstractLiveObject implements AsteriskQueue
{
	private final Log logger = LogFactory.getLog(this.getClass());
    private final String name;
    private Integer max;
    private String strategy;
    private Integer serviceLevel;
    private Integer weight;
    private final ArrayList<AsteriskChannel> entries;
    private final List<AsteriskQueueListener> listeners;

    AsteriskQueueImpl(AsteriskServerImpl server, String name, Integer max, String strategy, Integer serviceLevel, Integer weight)
    {
        super(server);
        this.name = name;
        this.max = max;
        this.strategy = strategy;
        this.serviceLevel = serviceLevel;
        this.weight = weight;
        this.entries = new ArrayList<AsteriskChannel>(25);
        listeners = new ArrayList<AsteriskQueueListener>();
    }

    public String getName()
    {
        return name;
    }

    public Integer getMax()
    {
        return max;
    }

    public String getStrategy()
    {
        return strategy;
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
        fireNewEntry(entry);
    }

    void removeEntry(AsteriskChannel entry)
    {
        synchronized (entries)
        {
            entries.remove(entry);
        }
        fireEntryLeave(entry);
    }

    @Override
   public String toString()
    {
        final StringBuffer sb;

        sb = new StringBuffer("AsteriskQueue[");
        sb.append("name='").append(getName()).append("',");
        sb.append("max='").append(getMax()).append("',");
        sb.append("strategy='").append(getStrategy()).append("',");
        sb.append("serviceLevel='").append(getServiceLevel()).append("',");
        sb.append("weight='").append(getWeight()).append("',");
        synchronized (entries)
        {
            sb.append("entries='").append(entries.toString()).append("',");
        }
        sb.append("systemHashcode=").append(System.identityHashCode(this));
        sb.append("]");

        return sb.toString();
    }
    
    public void addAsteriskQueueListener(AsteriskQueueListener listener)
    {
        synchronized (listeners)
        {
            listeners.add(listener);
        }
    }

    public void removeAsteriskQueueListener(AsteriskQueueListener listener)
    {
        synchronized (listeners)
        {
            listeners.remove(listener);
        }
    }

    void fireNewEntry(AsteriskChannel channel)
    {
        synchronized (listeners)
        {
            for (AsteriskQueueListener listener : listeners)
            {
                try
                {
                    listener.onNewEntry(channel);
                }
                catch (Exception e)
                {
                    logger.warn("Exception in onNewEntry()", e);
                }
            }
        }
    }

    void fireEntryLeave(AsteriskChannel channel)
    {
        synchronized (listeners)
        {
            for (AsteriskQueueListener listener : listeners)
            {
                try
                {
                    listener.onEntryLeave(channel);
                }
                catch (Exception e)
                {
                    logger.warn("Exception in onEntryLeave()", e);
                }
            }
        }
    }
}
