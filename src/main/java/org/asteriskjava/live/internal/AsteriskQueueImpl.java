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
public class AsteriskQueueImpl implements AsteriskQueue
{
    private String name;
    private Integer max;
    private ArrayList<AsteriskChannel> entries;

    AsteriskQueueImpl(String name)
    {
        this.name = name;
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
        entries.add(entry);
    }

    void removeEntry(AsteriskChannel entry)
    {
        entries.remove(entry);
    }

    public String toString()
    {
        StringBuffer sb;

        sb = new StringBuffer("AsteriskQueue[");

        sb.append("name='" + getName() + "',");
        sb.append("max='" + getMax() + "',");
        synchronized (entries)
        {
            sb.append("entries='" + entries.toString() + "',");
        }
        sb.append("systemHashcode=" + System.identityHashCode(this));
        sb.append("]");

        return sb.toString();
    }
}
