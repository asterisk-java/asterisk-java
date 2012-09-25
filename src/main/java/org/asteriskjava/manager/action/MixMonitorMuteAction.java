/*
 * Copyright 2012 Stefan Reuter.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.asteriskjava.manager.action;

/**
 * MixMonitorMute can be used to mute and un-mute an existing recording.
 * @author Bob Wienholt
 * @version $Id$
 */
public class MixMonitorMuteAction extends AbstractManagerAction
{
	private static final long serialVersionUID = 1L;
	private String channel;
	private String direction;
	private Integer state;

	/**
	 * Creates a new empty MixMonitorMuteAction
	 */
	public MixMonitorMuteAction()
	{
		super();
	}
	
	/**
	 * Mutes the monitor on the given channel.
	 * @param channel 
	 */
	public MixMonitorMuteAction(String channel)
	{
		super();
		this.channel = channel;
	}
	
	/**
	 * Mutes the monitor on the given channel for the given portion
	 * of the call.
	 * @param channel
	 * @param direction 
	 */
	public MixMonitorMuteAction(String channel, String direction)
	{
		super();
		this.channel = channel;
		this.direction = direction;
	}
	
	/**
	 * Either mutes or un-mutes the monitor on the given channel for 
	 * the given portion of the call.
	 * @param channel
	 * @param direction
	 * @param state 
	 */
	public MixMonitorMuteAction(String channel, String direction, Integer state)
	{
		super();
		this.channel = channel;
		this.direction = direction;
		this.state = state;
	}
	
	/**
	 * Either mutes or un-mutes the monitor on the given channel.
	 * @param channel
	 * @param state 
	 */
	public MixMonitorMuteAction(String channel, Integer state)
	{
		super();
		this.channel = channel;
		this.state = state;
	}
	
	/**
	 * Returns the name of this action.
	 */
	@Override
	public String getAction() {
		return "MixMonitorMute";
	}

	/**
	 * Returns the name of the channel to mute.
	 */
	public String getChannel() {
		return channel;
	}

	/**
	 * Sets the name of the channel to mute.
	 * @param channel 
	 */
	public void setChannel(String channel) {
		this.channel = channel;
	}

	/**
	 * Gets the direction of the part of the recording to mute.
	 */
	public String getDirection() {
		return direction;
	}

	/**
	 * Sets the direction of the part of the recording to mute.
	 * Can be one of: read, write, or both.  Defaults to both.
	 * @param direction 
	 */
	public void setDirection(String direction) {
		this.direction = direction;
	}

	/**
	 * Gets the state of the mute.
	 */
	public Integer getState() {
		return state;
	}

	/**
	 * Sets the state of the mute operation.  1 = on, 0 = off.
	 * @param state 
	 */
	public void setState(Integer state) {
		this.state = state;
	}
	
}
