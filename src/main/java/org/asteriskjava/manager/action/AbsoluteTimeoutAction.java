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
package org.asteriskjava.manager.action;

/**
 * The AbsoluteTimeoutAction sets the absolute maximum amount of time permitted
 * for a call on a given channel.<br>
 * Note that the timeout is set from the current time forward, not counting the
 * number of seconds the call has already been up.<br>
 * When setting a new timeout all previous absolute timeouts are cancelled.<br>
 * When the timeout is reached the call is returned to the T extension so that
 * you can playback an explanatory note to the calling party (the called party
 * will not hear that).<br>
 * This action corresponds the the AbsoluteTimeout command used in the dialplan.
 * 
 * @author srt
 * @version $Id: AbsoluteTimeoutAction.java,v 1.4 2005/08/07 00:09:42 srt Exp $
 */
public class AbsoluteTimeoutAction extends AbstractManagerAction
{
    /**
     * Serializable version identifier
     */
    static final long serialVersionUID = 3073237188819825503L;

    private String channel;
    private Integer timeout;

    /**
     * Creates a new empty AbsoluteTimeoutAction.
     */
    public AbsoluteTimeoutAction()
    {

    }

    /**
     * Creates a new AbsoluteTimeoutAction with the given channel and timeout.
     * 
     * @param channel the name of the channel
     * @param timeout the timeout in seconds or 0 to cancel the AbsoluteTimeout
     * @since 0.2
     */
    public AbsoluteTimeoutAction(String channel, Integer timeout)
    {
        this.channel = channel;
        this.timeout = timeout;
    }

    /**
     * Returns the name of this action, i.e. "AbsoluteTimeout".
     */
    public String getAction()
    {
        return "AbsoluteTimeout";
    }

    /**
     * Returns the name of the channel.
     */
    public String getChannel()
    {
        return channel;
    }

    /**
     * Sets the name of the channel.
     */
    public void setChannel(String channel)
    {
        this.channel = channel;
    }

    /**
     * Returns the timeout (in seconds) to set.
     */
    public Integer getTimeout()
    {
        return timeout;
    }

    /**
     * Sets the timeout (in seconds) to set on channel.<br>
     * Setting the timeout to 0 cancels the timeout.
     */
    public void setTimeout(Integer timeout)
    {
        this.timeout = timeout;
    }
}
