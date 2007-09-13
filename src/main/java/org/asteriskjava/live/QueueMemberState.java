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

import org.asteriskjava.manager.event.QueueMemberEvent;

/**
 * <p>
 * Valid status codes are:
 * <dl>
 * <dt>AST_DEVICE_UNKNOWN (0)</dt>
 * <dd>Queue member is available</dd>
 * <dt>AST_DEVICE_NOT_INUSE (1)</dt>
 * <dd>?</dd>
 * <dt>AST_DEVICE_INUSE (2)</dt>
 * <dd>?</dd>
 * <dt>AST_DEVICE_BUSY (3)</dt>
 * <dd>?</dd>
 * <dt>AST_DEVICE_INVALID (4)</dt>
 * <dd>?</dd>
 * <dt>AST_DEVICE_UNAVAILABLE (5)</dt>
 * <dd>?</dd>
 * </dl>
 * @since 0.3.1
 * @author <a href="mailto:patrick.breucking{@nospam}gonicus.de">Patrick
 *         Breucking</a>
 * @version $Id$
 */
public enum QueueMemberState
{
    /**
     * Busy means, phone is in action, eg. is ringing, in call.
     */
    DEVICE_BUSY, // 3

    DEVICE_INUSE, // 2

    DEVICE_INVALID, // 4

    /**
     * Queue member is available, eg. Agent is logged in but idle.
     */
    DEVICE_NOT_INUSE, // 1
    /**
     * Device is not availible for call, eg. Agent is logged off.
     */
    DEVICE_UNAVAILABLE, // 5

    DEVICE_UNKNOWN; // 0

    /**
     * Returns value specified by int. Use this to transform
     * {@link QueueMemberEvent.getStatus}
     * 
     * @param status
     * @return
     */
    public static QueueMemberState valueOf(Integer status)
    {
	QueueMemberState resultedStatus;
	switch (status)
	{
	case 0:
	default:
	    resultedStatus = DEVICE_UNKNOWN;
	    break;
	case 1:
	    resultedStatus = DEVICE_NOT_INUSE;
	    break;
	case 2:
	    resultedStatus = DEVICE_INUSE;
	    break;
	case 3:
	    resultedStatus = DEVICE_BUSY;
	    break;
	case 4:
	    resultedStatus = DEVICE_INVALID;
	    break;
	case 5:
	    resultedStatus = DEVICE_UNAVAILABLE;
	    break;
	}
	return resultedStatus;
    }
}
