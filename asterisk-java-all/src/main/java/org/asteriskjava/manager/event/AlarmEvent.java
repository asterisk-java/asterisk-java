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
 * An AlarmEvent is triggered when a Zap channel enters or changes alarm state.<p>
 * It is implemented in <code>channels/chan_zap.c</code>
 *
 * @author srt
 * @version $Id$
 */
public class AlarmEvent extends ManagerEvent {
    /**
     * Serializable version identifier
     */
    private static final long serialVersionUID = 5235245336934457877L;
    private String alarm;
    private Integer channel;

    /**
     * @param source
     */
    public AlarmEvent(Object source) {
        super(source);
    }

    /**
     * Returns the kind of alarm that happened.<p>
     * This may be one of
     * <ul>
     * <li>Red Alarm</li>
     * <li>Yellow Alarm</li>
     * <li>Blue Alarm</li>
     * <li>Recovering</li>
     * <li>Loopback</li>
     * <li>Not Open</li>
     * </ul>
     */
    public String getAlarm() {
        return alarm;
    }

    /**
     * Sets the kind of alarm that happened.
     */
    public void setAlarm(String alarm) {
        this.alarm = alarm;
    }

    /**
     * Returns the number of the channel the alarm occured on.
     */
    public Integer getChannel() {
        return channel;
    }

    /**
     * Sets the number of the channel the alarm occured on.
     */
    public void setChannel(Integer channel) {
        this.channel = channel;
    }
}
