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
 * The lifecycle status of an {@link org.asteriskjava.live.AsteriskChannel}.
 * 
 * @author srt
 * @version $Id$
 */
public enum ChannelState
{
    /**
     * The initial state of the channel when it is not yet in use.
     */
    DOWN,
    OFF_HOOK,
    DIALING,
    RING,
    RINGING,
    UP,
    BUSY,
    RSRVD,

    /**
     * The channel has been hung up.
     */
    HUNGUP
}
