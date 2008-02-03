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
 * <p/>
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
 *
 * @author <a href="mailto:patrick.breucking{@nospam}gonicus.de">Patrick Breucking</a>
 * @version $Id$
 * @since 0.3.1
 */
public enum QueueMemberState
{
    DEVICE_UNKNOWN(0),

    /**
     * Queue member is available, eg. Agent is logged in but idle.
     */
    DEVICE_NOT_INUSE(1),

    DEVICE_INUSE(2),

    /**
     * Busy means, phone is in action, eg. is ringing, in call.
     */
    DEVICE_BUSY(3),

    DEVICE_INVALID(4),

    /**
     * Device is not availible for call, eg. Agent is logged off.
     */
    DEVICE_UNAVAILABLE(5);

    private final int status;

    /**
     * Creates a new instance.
     *
     * @param status the numerical status code.
     */
    QueueMemberState(int status)
    {
        this.status = status;
    }

    /**
     * Returns the numerical status code.
     *
     * @return the numerical status code.
     */
    public int getStatus()
    {
        return status;
    }

    /**
     * Returns value specified by int. Use this to transform
     * {@link org.asteriskjava.manager.event.QueueMemberEvent#getStatus()}.
     *
     * @param status integer representation of the status.
     * @return corresponding QueueMemberState object or <code>null</code> if none matches.
     */
    public static QueueMemberState valueOf(Integer status)
    {
        if (status == null)
        {
            return null;
        }

        for (QueueMemberState tmp : QueueMemberState.values())
        {
            if (tmp.getStatus() == status)
            {
                return tmp;
            }
        }

        return null;
    }
}
