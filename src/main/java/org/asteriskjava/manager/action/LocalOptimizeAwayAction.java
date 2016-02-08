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
 * <a href="https://wiki.asterisk.org/wiki/display/AST/ManagerAction_LocalOptimizeAway">LocalOptimizeAway</a> action -- Optimize away a local channel when possible.
 * <p>
 * Available since Asterisk 1.8
 *
 * @author SYON Communications Inc.
 */
public class LocalOptimizeAwayAction extends AbstractManagerAction
{
    /**
     * Serializable version identifier.
     */
    private static final long serialVersionUID = 1L;
    private String channel;

    /**
     * Creates a new empty LocalOptimizeAwayAction.
     */
    public LocalOptimizeAwayAction()
    {

    }

    /**
     * Creates a new LocalOptimizeAwayAction with channel name.
     *
     * @param channel Name of the channel for clears the flag.
     */
    public LocalOptimizeAwayAction(String channel)
    {
	this.channel = channel;
    }

    /**
     * Returns the name of this action, i.e. "LocalOptimizeAway".
     */
    @Override
    public String getAction()
    {
	return "LocalOptimizeAway";
    }

    /**
     * Sets the name of the channel.
     *
     * @return Name of the channel for clears the flag.
     */
    public String getChannel()
    {
	return channel;
    }

    /**
     * Returns the name of tha channel.
     *
     * @param channel Name of the channel for clears the flag.
     */
    public void setChannel(String channel)
    {
	this.channel = channel;
    }

}

