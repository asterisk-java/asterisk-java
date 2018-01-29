/*
 *  Copyright 2018 CallShaper, LLC
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
 * Stop the specified MixMonitor on the specified channel.
 *
 * Available since Asterisk 11
 *
 * @see MixMonitorAction
 * @see PauseMixMonitorAction
 * @see MixMonitorMuteAction
 */
public class StopMixMonitorAction extends AbstractManagerAction
{
    private static final long serialVersionUID = 1L;
    private String channel;
    private String mixMonitorId;

    /**
     * Creates a new empty StopMixMonitorAction
     */
    public StopMixMonitorAction()
    {
        super();
    }

    /**
     * Creates a StopMixMonitorAction that stops all MixMonitors on the
     * specified channel.
     *
     * @param channel	the name of the channel
     */
    public StopMixMonitorAction(String channel)
    {
        this(channel, null);
    }

    /**
     * Creates a StopMixMonitorAction that stops the specified MixMonitor on
     * the specified channel.
     *
     * @param channel		the name of the channel
     * @param mixMonitorId	the ID of the MixMonitor to stop
     */
    public StopMixMonitorAction(String channel, String mixMonitorId)
    {
        this.channel = channel;
        this.mixMonitorId = mixMonitorId;
    }

    /**
     * Returns the name of this action, i.e. "StopMixMonitor"
     *
     * @return the name of the AMI action that this class implements
     */
    @Override
    public String getAction()
    {
        return "StopMixMonitor";
    }

    /**
     * Returns the name of the Asterisk channel to monitor.
     *
     * @return the Asterisk channel name
     */
    public String getChannel()
    {
        return channel;
    }

    /**
     * Sets the name of the Asterisk channel to the given value.
     *
     * @param channel the Asterisk channel name
     */
    public void setChannel(String channel)
    {
        this.channel = channel;
    }

    /**
     * Returns the ID of the MixMonitor to stop.
     *
     * @return the MixMonitor ID
     */
    public String getMixMonitorId()
    {
        return mixMonitorId;
    }

    /**
     * Sets the ID of the MixMonitor so stop.
     *
     * @param mixMonitorId the MixMonitor ID
     */
    public void setMixMonitorId(String mixMonitorId)
    {
        this.mixMonitorId = mixMonitorId;
    }
}