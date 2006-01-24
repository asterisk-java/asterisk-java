/*
 *  Copyright  2004-2006 Stefan Reuter
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
package org.asteriskjava.manager;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Enumeration that represents the state of a channel.
 * 
 * @author srt
 * @version $Id: ChannelStateEnum.java,v 1.3 2005/03/13 11:26:48 srt Exp $
 */
public class ChannelStateEnum implements Serializable
{
    /**
     * Serializable version identifier
     */
    private static final long serialVersionUID = 6436381500165485011L;

    private static Map<String, ChannelStateEnum> literals = new HashMap<String, ChannelStateEnum>();

    private String state;

    public static final ChannelStateEnum DOWN = new ChannelStateEnum("Down");
    public static final ChannelStateEnum OFF_HOOK = new ChannelStateEnum("OffHook");
    public static final ChannelStateEnum DIALING = new ChannelStateEnum("Dialing");
    public static final ChannelStateEnum RING = new ChannelStateEnum("Ring");
    public static final ChannelStateEnum RINGING = new ChannelStateEnum("Ringing");
    public static final ChannelStateEnum UP = new ChannelStateEnum("Up");
    public static final ChannelStateEnum BUSY = new ChannelStateEnum("Busy");
    public static final ChannelStateEnum RSRVD = new ChannelStateEnum("Rsrvd");
    public static final ChannelStateEnum HUNGUP = new ChannelStateEnum("Hungup");

    private ChannelStateEnum(String state)
    {
        this.state = state;
        literals.put(state, this);
    }

    public static ChannelStateEnum getEnum(String state)
    {
        return literals.get(state);
    }

    public String toString()
    {
        return state;
    }
}
