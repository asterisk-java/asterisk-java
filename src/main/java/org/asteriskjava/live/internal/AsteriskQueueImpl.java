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
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.asteriskjava.live.AsteriskChannel;
import org.asteriskjava.live.AsteriskQueue;
import org.asteriskjava.live.AsteriskQueueListener;
import org.asteriskjava.live.AsteriskQueueMember;
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
    /**
     * @author <a href="mailto:patrick.breucking{@nospam}gonicus.de">Patrick
     *         Breucking</a>
     * @since 0.1
     * @version $Id$
     * 
     */
    public class ServiceLevelTimerTask extends TimerTask
    {

	private AsteriskChannel channel;

	/**
	 * 
	 */
	public ServiceLevelTimerTask(AsteriskChannel channel)
	{
	    this.channel = channel;
	}

	/*
	 * (non-Javadoc)
	 * @see java.util.TimerTask#run()
	 */
	@Override
	public void run()
	{
	    System.out.println("Run Timer");
	    fireServiceLevelExceeded(channel);
	}

    }

    private final Log logger = LogFactory.getLog(this.getClass());
    private final String name;
    private Integer max;
    private String strategy;
    private Integer serviceLevel;
    private Integer weight;
    private final ArrayList<AsteriskChannel> entries;
    private final Timer timer;
    private final HashMap<String, AsteriskQueueMemberImpl> members;
    private final List<AsteriskQueueListener> listeners;
    private final HashMap<AsteriskChannel, ServiceLevelTimerTask> timers;

    AsteriskQueueImpl(AsteriskServerImpl server, String name, Integer max,
	    String strategy, Integer serviceLevel, Integer weight)
    {
	super(server);
	this.name = name;
	this.max = max;
	this.strategy = strategy;
	this.serviceLevel = serviceLevel;
	this.weight = weight;
	this.entries = new ArrayList<AsteriskChannel>(25);
	listeners = new ArrayList<AsteriskQueueListener>();
	members = new HashMap<String, AsteriskQueueMemberImpl>();
	timer = new Timer();
	timers = new HashMap<AsteriskChannel, ServiceLevelTimerTask>();
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
	long delay = serviceLevel.longValue() * 1000;
	if (delay > 0)
	{
	    ServiceLevelTimerTask timerTask = new ServiceLevelTimerTask(entry);
	    timer.schedule(timerTask, delay);
	    timers.put(entry, timerTask);
	}
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
	if (timers.containsKey(entry))
	{
	    ServiceLevelTimerTask timerTask = timers.get(entry);
	    timerTask.cancel();
	    timers.remove(timerTask);
	}
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
	synchronized (members)
	{
	    sb.append("members='").append(members.toString()).append("',");
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

    /**
     * Notifies all registered listener that an entry joins the queue.
     * 
     * @param channel that joins the queue
     */
    void fireNewEntry(AsteriskChannel channel)
    {
	synchronized (listeners)
	{
	    for (AsteriskQueueListener listener : listeners)
	    {
		try
		{
		    listener.onNewEntry(channel);
		} catch (Exception e)
		{
		    logger.warn("Exception in onNewEntry()", e);
		}
	    }
	}
    }

    /**
     * Notifies all registered listener that an entry leaves the queue.
     * @param channel that leaves the queue.
     */
    void fireEntryLeave(AsteriskChannel channel)
    {
	synchronized (listeners)
	{
	    for (AsteriskQueueListener listener : listeners)
	    {
		try
		{
		    listener.onEntryLeave(channel);
		} catch (Exception e)
		{
		    logger.warn("Exception in onEntryLeave()", e);
		}
	    }
	}
    }

    /**
     * Returns a collection of members of this queue.
     * @see org.asteriskjava.live.AsteriskQueue#getMembers()
     */
    public Collection<AsteriskQueueMember> getMembers()
    {
	ArrayList<AsteriskQueueMember> listOfMembers = new ArrayList<AsteriskQueueMember>(
		members.size());
	synchronized (members)
	{
	    for (AsteriskQueueMemberImpl asteriskQueueMember : members.values())
	    {
		listOfMembers.add(asteriskQueueMember);
	    }
	}
	return listOfMembers;
    }

    /**
     * Returns a member by its location.
     * @param location ot the member
     * @return the member by its location.
     */
    AsteriskQueueMemberImpl getMember(String location)
    {
	synchronized (members)
	{
	    if (members.containsKey(location))
	    {
		return members.get(location);
	    }
	}
	return null;
    }

    /**
     * Add a new member to this queue
     * @param member to add
     */
    void addMember(AsteriskQueueMemberImpl member)
    {
	synchronized (members)
	{
	    // Check if member already exists
	    if (members.containsValue(member))
	    {
		return;
	    }
	    // If not, add the new member.
	    logger.info("Adding new member to the queue " + getName() + ": "
		    + member.toString());
	    members.put(member.getLocation(), member);
	}
    }

    /**
     * Retrieves a member by its location.
     * @param location of the member
     * @return the requested member.
     */
    AsteriskQueueMemberImpl getMemberByLocation(String location)
    {
	AsteriskQueueMemberImpl member = null;
	synchronized (members)
	{
	    member = members.get(location);
	}
	if (member == null)
	{
	    logger.error("Requested member at location " + location
		    + " not found!");
	}
	return member;
    }

    /**
     * Notifies all registered listener that a queue member changes its state.
     * @param member
     */
    void fireMemberStateChanged(AsteriskQueueMemberImpl member)
    {
	synchronized (listeners)
	{
	    for (AsteriskQueueListener listener : listeners)
	    {
		try
		{
		    listener.onMemberStateChange(member);
		} catch (Exception e)
		{
		    logger.warn("Exception in onMemberStateChange()", e);
		}
	    }
	}
    }

    /**
     * Removes a member from this queue.
     * @param member
     */
    public void removeMember(AsteriskQueueMemberImpl member)
    {
	synchronized (members)
	{
	    // Check if member exists
	    if (!members.containsValue(member))
	    {
		return;
	    }
	    // If so, remove the member.
	    logger.info("Remove member from the queue " + getName() + ": "
		    + member.toString());
	    members.remove(member.getLocation());
	}
    }

    /**
     * @param channel2
     */
    void fireServiceLevelExceeded(AsteriskChannel channel)
    {
	synchronized (listeners)
	{
	    for (AsteriskQueueListener listener : listeners)
	    {
		try
		{
		    listener.onEntryServiceLevelExceeded(channel);
		} catch (Exception e)
		{
		    logger.warn("Exception in fireServiceLevelExceeded()", e);
		}
	    }
	}
    }
}
