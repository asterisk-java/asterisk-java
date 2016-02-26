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
 * An ExtensionStatusEvent is triggered when the state of an extension changes.<p>
 * For this to work for you must provide appropriate hints in your dialplan to
 * map channels to extensions.
 * <br>
 * Example:
 * <pre>exten =&gt; 1234,1,Dial(SIP/myuser)
 * exten =&gt; 1234,hint,SIP/myuser</pre>
 * Hints can also be used to map the state of multiple channels to an extension:
 * <pre>exten =&gt; 6789,hint,SIP/user1&amp;SIP/user2</pre>
 * <br>
 * It is implemented in <code>manager.c</code>, values for state are defined in
 * <code>include/asterisk/pbx.h</code>.
 *
 * @author srt
 * @version $Id$
 */
public class ExtensionStatusEvent extends ManagerEvent
{
    /**
     * Serializable version identifier.
     */
    private static final long serialVersionUID = 0L;

    /**
     * No device INUSE or BUSY.
     */
    public static final int NOT_INUSE = 0;

    /**
     * One or more devices INUSE.
     */
    public static final int INUSE = 1;

    /**
     * All devices BUSY.
     */
    public static final int BUSY = 1 << 1;

    /**
     * All devices UNAVAILABLE/UNREGISTERED.
     */
    public static final int UNAVAILABLE = 1 << 2;

    /**
     * One or more devices RINGING.
     */
    public static final int RINGING = 1 << 3;

    private String hint;
    private Integer status;
    private String callerId;

    public ExtensionStatusEvent(Object source)
    {
        super(source);
    }

    /**
     * Returns the hint assigned to the extension. The hint is used to map a channel (e.g. "SIP/123") to an
     * extension (e.g. "123").<p>
     * Available since Asterisk 1.6.
     *
     * @return the hint (channel name) assigned to the extension.
     * @since 1.0.0
     */
    public String getHint()
    {
        return hint;
    }

    public void setHint(String hint)
    {
        this.hint = hint;
    }

    /**
     * Returns the state of the extension.<p>
     * Possible values are:
     * <ul>
     * <li>RINGING</li>
     * <li>INUSE | RINGING</li>
     * <li>INUSE</li>
     * <li>NOT_INUSE</li>
     * <li>BUSY</li>
     * <li>UNAVAILABLE</li>
     * </ul>
     */
    public Integer getStatus()
    {
        return status;
    }

    /**
     * Sets the state of the extension.
     */
    public void setStatus(Integer status)
    {
        this.status = status;
    }

    /**
     * Returns the Caller*ID in the form <code>"Some Name" &lt;1234&gt;</code>.
     * <br>
     * This property is only available on BRIstuffed Asterisk servers.
     *
     * @return the Caller*ID.
     * @since 0.3
     */
    public String getCallerId()
    {
        return callerId;
    }

    /**
     * Sets the Caller*ID.
     *
     * @param callerId the Caller*ID.
     * @since 0.3
     */
    public void setCallerId(String callerId)
    {
        this.callerId = callerId;
    }
}
