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

import org.asteriskjava.live.ManagerCommunicationException;
import org.asteriskjava.live.MeetMeRoom;
import org.asteriskjava.live.MeetMeUser;
import org.asteriskjava.lock.LockableMap;
import org.asteriskjava.lock.Locker.LockCloser;
import org.asteriskjava.manager.action.CommandAction;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

/**
 * Default implementation of the MeetMeRoom interface.
 */
class MeetMeRoomImpl extends AbstractLiveObject implements MeetMeRoom {
    private static final String COMMAND_PREFIX = "meetme";
    private static final String LOCK_COMMAND = "lock";
    private static final String UNLOCK_COMMAND = "unlock";

    private final String roomNumber;

    /**
     * Maps userNumber to user.
     */
    private final LockableMap<Integer, MeetMeUserImpl> users;

    MeetMeRoomImpl(AsteriskServerImpl server, String roomNumber) {
        super(server);
        this.roomNumber = roomNumber;
        this.users = new LockableMap<>(new HashMap<>(20));
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public Collection<MeetMeUser> getUsers() {
        try (LockCloser closer = users.withLock()) {
            return new ArrayList<MeetMeUser>(users.values());
        }
    }

    public boolean isEmpty() {
        try (LockCloser closer = users.withLock()) {
            return users.isEmpty();
        }
    }

    Collection<MeetMeUserImpl> getUserImpls() {
        try (LockCloser closer = users.withLock()) {
            return new ArrayList<>(users.values());
        }
    }

    void addUser(MeetMeUserImpl user) {
        try (LockCloser closer = users.withLock()) {
            users.put(user.getUserNumber(), user);
        }
    }

    MeetMeUserImpl getUser(Integer userNumber) {
        try (LockCloser closer = users.withLock()) {
            return users.get(userNumber);
        }
    }

    void removeUser(MeetMeUserImpl user) {
        try (LockCloser closer = users.withLock()) {
            users.remove(user.getUserNumber());
        }
    }

    // action methods

    public void lock() throws ManagerCommunicationException {
        sendMeetMeCommand(LOCK_COMMAND);
    }

    public void unlock() throws ManagerCommunicationException {
        sendMeetMeCommand(UNLOCK_COMMAND);
    }

    private void sendMeetMeCommand(String command) throws ManagerCommunicationException {
        final StringBuilder sb = new StringBuilder();
        sb.append(COMMAND_PREFIX);
        sb.append(" ");
        sb.append(command);
        sb.append(" ");
        sb.append(roomNumber);

        server.sendAction(new CommandAction(sb.toString()));
    }

    @Override
    public String toString() {
        StringBuilder sb;
        int systemHashcode;

        sb = new StringBuilder("MeetMeRoom[");

        try (LockCloser closer = this.withLock()) {
            sb.append("roomNumber='").append(getRoomNumber()).append("',");
            systemHashcode = System.identityHashCode(this);
        }
        sb.append("systemHashcode=").append(systemHashcode);
        sb.append("]");

        return sb.toString();
    }
}
