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
package org.asteriskjava.manager.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.asteriskjava.manager.Channel;
import org.asteriskjava.manager.Queue;

/**
 * @author srt
 * @version $Id: Queue.java,v 1.3 2005/08/21 22:39:22 srt Exp $
 */
public class QueueImpl implements Serializable, Queue
{
    /**
     * Serial version identifier
     */
    private static final long serialVersionUID = -6597536667933738312L;
    private String name;
    private Integer max;
    private List<Channel> entries;

    public QueueImpl(String name)
    {
        this.name = name;
        this.entries = Collections.synchronizedList(new ArrayList<Channel>());
    }

    public String getName()
    {
        return name;
    }

    public Integer getMax()
    {
        return max;
    }

    public void setMax(Integer max)
    {
        this.max = max;
    }

    public List getEntries()
    {
        return entries;
    }

    public void addEntry(Channel entry)
    {
        entries.add(entry);
    }

    public void removeEntry(Channel entry)
    {
        entries.remove(entry);
    }

    public String toString()
    {
        StringBuffer sb;

        sb = new StringBuffer(getClass().getName() + ": ");

        sb.append("name='" + getName() + "'; ");
        sb.append("max='" + getMax() + "'; ");
        sb.append("entries='" + getEntries() + "'; ");
        sb.append("systemHashcode=" + System.identityHashCode(this));

        return sb.toString();
    }
}
