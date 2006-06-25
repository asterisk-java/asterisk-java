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

/**
 * Represents an Asterisk MeetMe room.<p>
 * MeetMe rooms bridge multiple channels.
 * 
 * @author srt
 * @since 0.3
 */
public interface MeetMeRoom
{
    /**
     * Returns the number of this MeetMe room.<p>
     * This property is immutable.
     * 
     * @return the number of this MeetMe room.
     */
    String getRoomNumber();

    /**
     * Returns a collection of all active users in this MeetMe room.
     * 
     * @return a collection of all active users in this MeetMe room.
     */
    Collection<MeetMeUser> getUsers();

    /**
     * Locks this room so no addtional users can join.
     * 
     * @throws ManagerCommunicationException if the room can't be locked.
     */
    void lock() throws ManagerCommunicationException;

    /**
     * Unlocks this room so additional users can join again.
     * 
     * @throws ManagerCommunicationException if the room can't be locked.
     */
    void unlock() throws ManagerCommunicationException;
}
