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

import java.util.Map;
import java.util.HashMap;
import java.util.Collections;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * Abstract base class providing common properties for HangupEvent, NewChannelEvent and
 * NewStateEvent.
 *
 * @author srt
 * @version $Id$
 */
public abstract class AbstractChannelStateEvent extends AbstractChannelEvent
{
    /**
     * Serializable version identifier.
     */
    static final long serialVersionUID = 0L;

    /* from include/asterisk/channel.h */

    /**
     * Channel is down and available.
     */
    public static final int AST_STATE_DOWN = 0;

    /**
     * Channel is down, but reserved.
     */
    public static final int AST_STATE_RSRVD = 1;

    /**
     * Channel is off hook.
     */
    public static final int AST_STATE_OFFHOOK = 2;

    /**
     * Digits (or equivalent) have been dialed.
     */
    public static final int AST_STATE_DIALING = 3;

    /**
     * Line is ringing.
     */
    public static final int AST_STATE_RING = 4;

    /**
     * Remote end is ringing.
     */
    public static final int AST_STATE_RINGING = 5;

    /**
     * Line is up.
     */
    public static final int AST_STATE_UP = 6;

    /**
     * Line is busy.
     */
    public static final int AST_STATE_BUSY = 7;

    /**
     * Digits (or equivalent) have been dialed while offhook.
     */
    public static final int AST_STATE_DIALING_OFFHOOK = 8;

    /**
     * Channel has detected an incoming call and is waiting for ring.
     */
    public static final int AST_STATE_PRERING = 9;

    private static final Map<String, Integer> inverseStateMap;

    static
    {
        final Map<String, Integer> tmpInverseStateMap = new HashMap<String, Integer>();

        tmpInverseStateMap.put("Down", AST_STATE_DOWN);
        tmpInverseStateMap.put("Rsrvd", AST_STATE_RSRVD);
        tmpInverseStateMap.put("OffHook", AST_STATE_OFFHOOK);
        tmpInverseStateMap.put("Dialing", AST_STATE_DIALING);
        tmpInverseStateMap.put("Ring", AST_STATE_RING);
        tmpInverseStateMap.put("Ringing", AST_STATE_RINGING);
        tmpInverseStateMap.put("Up", AST_STATE_UP);
        tmpInverseStateMap.put("Busy", AST_STATE_BUSY);
        tmpInverseStateMap.put("Dialing Offhook", AST_STATE_DIALING_OFFHOOK);
        tmpInverseStateMap.put("Pre-ring", AST_STATE_PRERING);

        inverseStateMap = Collections.unmodifiableMap(tmpInverseStateMap);
    }

    private static final Pattern UNKNOWN_STATE_PATTERN = Pattern.compile("^Unknown \\((\\d+)\\)$");

    private Integer channelState;
    private String channelStateDesc;

    protected AbstractChannelStateEvent(Object source)
    {
        super(source);
    }

    /**
     * Returns the new state of the channel.<p>
     * For Asterisk versions prior to 1.6 (that do not send the numeric value) it is derived
     * from the descriptive text.
     *
     * @return the new state of the channel.
     * @since 1.0.0
     */
    public Integer getChannelState()
    {
        return channelState == null ? str2state(channelStateDesc) : channelState;
    }

    /**
     * Sets the new state of the channel.
     *
     * @param channelState the new state of the channel.
     * @since 1.0.0
     */
    public void setChannelState(Integer channelState)
    {
        this.channelState = channelState;
    }

    /**
     * Returns the new state of the channel as a descriptive text.<p>
     * The following states are used:<p>
     * <ul>
     * <li>Down</li>
     * <li>Rsrvd</li>
     * <li>OffHook</li>
     * <li>Dialing</li>
     * <li>Ring</li>
     * <li>Ringing</li>
     * <li>Up</li>
     * <li>Busy</li>
     * <li>Dialing Offhook (since Asterik 1.6)</li>
     * <li>Pre-ring (since Asterik 1.6)</li>
     * <ul>
     *
     * @return the new state of the channel as a descriptive text.
     * @since 1.0.0
     */
    public String getChannelStateDesc()
    {
        return channelStateDesc;
    }

    /**
     * Sets the new state of the channel as a descriptive text.
     *
     * @param channelStateDesc the new state of the channel as a descriptive text.
     * @since 1.0.0
     */
    public void setChannelStateDesc(String channelStateDesc)
    {
        this.channelStateDesc = channelStateDesc;
    }

    /**
     * Returns the new state of the channel as a descriptive text.<p>
     * This is an alias for {@link @getChannelStateDesc}.
     *
     * @return the new state of the channel as a descriptive text.
     * @deprecated as of 1.0.0, use {@link #getChannelStateDesc()} instead or even better switch to numeric
     *             values as returned by {@link #getChannelState()}.
     */
    public String getState()
    {
        return channelStateDesc;
    }

    /**
     * Sets the new state of the channel as a descriptive text.<p>
     * The state property is used by Asterisk versions prior to 1.6.
     *
     * @param state the new state of the channel as a descriptive text.
     */
    public void setState(String state)
    {
        this.channelStateDesc = state;
    }

    /**
     * This is the inverse to <code>ast_state2str</code> in <code>channel.c</code>.
     *
     * @param str state as a descriptive text.
     * @return numeric state.
     */
    private Integer str2state(String str)
    {
        Integer state;

        if (str == null)
        {
            return null;
        }

        state = inverseStateMap.get(str);

        if (state == null)
        {
            Matcher matcher = UNKNOWN_STATE_PATTERN.matcher(str);
            if (matcher.matches())
            {
                try
                {
                    state = Integer.valueOf(matcher.group(1));
                }
                catch (NumberFormatException e)
                {
                    // should not happen as the pattern requires \d+ for the state.
                }
            }
        }

        return state;
    }
}
