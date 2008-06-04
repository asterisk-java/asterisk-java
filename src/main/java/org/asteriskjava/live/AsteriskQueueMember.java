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

/**
 * A queue member. Queue Member can be an agent or a direkt sip account, eg. a
 * Phone.<p>
 * PropertyChangeEvents are fired for the following properties:
 * <ul>
 * <li>state</li>
 * <li>paused</li>
 * <li>penalty (since Asterisk 1.6)</li>
 * </ul>
 *
 * @author <a href="mailto:patrick.breucking{@nospam}gonicus.de">Patrick Breucking</a>
 * @version $Id$
 * @since 0.3.1
 */
public interface AsteriskQueueMember extends LiveObject
{
    String PROPERTY_STATE = "state";
    String PROPERTY_PENALTY = "penalty";
    String PROPERTY_PAUSED = "paused";

    /**
     * Returns the location of this member.
     *
     * @return the location of this member.
     */
    String getLocation();

    /**
     * Returns the queue this member is registerd to.
     *
     * @return the queue this member is registerd to.
     */
    AsteriskQueue getQueue();

    /**
     * Returns the state of this member.
     *
     * @return the state of this member.
     */
    QueueMemberState getState();
    
     /**
     * Returns the paused status of this member.
     *
     * @return paused status if this member.
     */
    boolean getPaused();

    /**
     * Returns the penalty of this member.
     *
     * @return the penalty of this member.
     * @since 1.0.0
     */
    Integer getPenalty();

    /**
     * Assignes a new penalty to this queue member.<p>
     * Available since Asterisk 1.6.
     *
     * @param penalty the new penalty value, must not be negative.
     * @throws IllegalArgumentException if the penalty is negative.
     * @throws ManagerCommunicationException if the QueuePenaltyAction could not be send to Asterisk.
     * @throws InvalidPenaltyException if Asterisk refused to set the new penalty.
     * @since 1.0.0
     */
    void setPenalty(int penalty) throws IllegalArgumentException, ManagerCommunicationException, InvalidPenaltyException;
}
