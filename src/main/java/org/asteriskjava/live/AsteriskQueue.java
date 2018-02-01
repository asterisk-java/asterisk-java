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
package org.asteriskjava.live;

import java.util.Collection;
import java.util.List;

/**
 * An Asterisk ACD queue.
 *
 * @author srt
 * @author itaqua
 * @version $Id$
 */
public interface AsteriskQueue
{
    String STRATEGY_RINGALL = "ringall";
    String STRATEGY_ROUNDROBIN = "roundrobin";
    String STRATAGY_LEAST_RECENT = "leastrecent";
    String STRATEGY_FEWEST_CALLS = "fewestcalls";
    String STRATEGY_RANDOM = "random";
    String STRATEGY_RRMEMORY = "rrmemory";

    /**
     * Returns the name of this queue as defined in Asterisk's
     * <code>queues.conf</code>.
     *
     * @return the name of this queue.
     */
    String getName();

    /**
     * Returns the maximum number of people allowed to wait in this queue or 0
     * for unlimited.
     * <br>
     * Corresponds to the <code>maxlen</code> option in Asterisk's
     * <code>queues.conf</code>.
     *
     * @return the maximum number of people allowed to wait in this queue.
     */
    Integer getMax();

    /**
     * Returns the strategy used for this queue.
     * <br>
     * Possible values are:
     * <ul>
     * <li>ringall</li>
     * <li>roundrobin</li>
     * <li>leastrecent</li>
     * <li>fewestcalls</li>
     * <li>random</li>
     * <li>rrmemory</li>
     * </ul>
     * Available since Asterisk 1.6
     *
     * @return the strategy used for this queue.
     * @since 1.0.0
     */
    String getStrategy();

    /**
     * Returns the service level (in seconds) as defined by the
     * <code>servicelevel</code> setting in <code>queues.conf</code>.
     *
     * @return the service level (in seconds).
     */
    Integer getServiceLevel();

    /**
     * Returns the weight of this queue.
     * <br>
     * A queue can be assigned a 'weight' to ensure calls waiting in a higher
     * priority queue will deliver its calls first. Only delays the lower weight
     * queue's call if the member is also in the higher weight queue.
     * <br>
     * Available since Asterisk 1.2
     *
     * @return the weight of this queue or <code>null</code> if not supported
     *         by your version of Asterisk.
     */
    Integer getWeight();

    /**
     * Returns the list of entries currently waiting in this queue.
     *
     * @return the (ordered) list of entries currently waiting in this queue.
     */
    List<AsteriskQueueEntry> getEntries();

    /**
     * Returns the list of Asterisk members of this queue.
     *
     * @return the list of Asterisk members of this queue.
     * @since 0.3.1
     */
    Collection<AsteriskQueueMember> getMembers();

    /*
    void addMember(String iface);

    void addMember(String iface, int penality);

    void addMember(String iface, String memberName);

    void addMember(String iface, String memberName, String stateInterface);
    */

    /**
     * Registers a new queue listener.
     *
     * @param listener the listener to add.
     * @since 0.3
     */
    void addAsteriskQueueListener(AsteriskQueueListener listener);

    /**
     * Removes a previously registered queue listener.
     *
     * @param listener the listener to remove.
     * @since 0.3
     */
    void removeAsteriskQueueListener(AsteriskQueueListener listener);

    /**
     * Returns the number of abandoned calls.
     * @author itaqua
     * @return the number of abandoned calls.
     */
    Integer getAbandoned();

    /**
     * Returns the ratio of calls answered within the specified service level per total completed
     * calls (in percent).
     * @author itaqua
     * @return the ratio of calls answered within the specified service level per total completed
     *         calls (in percent).
     */
	Double getServiceLevelPerf();

	/**
     * Returns the number of completed calls.
     * @author itaqua
     * @return the number of completed calls.
     */
	Integer getCompleted();

	/**
     * Returns the current average talk time for this queue based on an exponential average.
     *
     * @return the current average talk time for this queue.
     */
	Integer getTalkTime();

	/**
     * Returns the current average holdtime for this queue (in seconds).
     * @author itaqua
     * @return the current average holdtime for this queue (in seconds).
     */
	Integer getHoldTime();

	/**
     * Returns the number of calls currently waiting in the queue.
     * Is better to use get Waiting()
     * @author itaqua
     * @see getWaiting()
     * @return the number of calls currently waiting in the queue.
     */
	Integer getCalls();

	/**
     * Returns the number of calls currently waiting in the queue.
     * More verbose Method than getCalls()
     * @author itaqua
     * @return the number of calls currently waiting in the queue.
     */
	Integer getWaiting();

	/**
     * timestamp (miliseconds) of last update of this object
     * @todo maybe change this to an immutable object
     * @author itaqua
     * @return
     */
    long getLastUpdateMillis();

}
