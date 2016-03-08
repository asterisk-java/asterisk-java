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
 * Action: MuteAudio Synopsis: Mute an audio stream Privilege: system,all
 * Description: Mute an incoming or outbound audio stream in a channel.
 * Variables: Channel: <name> The channel you want to mute. Direction: in | out
 * |all The stream you want to mute. State: on | off Whether to turn mute on or
 * off. ActionID: <id> Optional action ID for this AMI transaction.
 * 
 * It is defined in <code>res/res_mutestream.c</code>.
 * <p>
 * Available since Asterisk 1.8
 * 
 * @author sbs
 * @version $Id: MuteAction.java 938 2007-12-31 03:23:38Z srt $
 * @since 1.0.0
 */
public class MuteAudioAction extends AbstractManagerAction
{
	/**
	 * Serializable version identifier.
	 */
	private static final long serialVersionUID = 0L;

	private String channel;

	/** The audio direct (relative to the pbx) which is to be muted. */
	public enum Direction
	{
		IN("in"), OUT("out"), ALL("all");
                
		String value;
		
		Direction(String value)
		{
			this.value = value;
		}
		
		public String toString()
		{
			return this.value;
		}                
	}

	private Direction direction;

	/** Controls whether to mute (on) or unmute (off) the call **/
	public enum State
	{
		MUTE("on"), UNMUTE("off");  //$NON-NLS-1$//$NON-NLS-2$
		
		String value;
		
		State(String value)
		{
			this.value = value;
		}
		
		public String toString()
		{
			return this.value;
		}
		
	}

	private State state;

	/**
	 * Creates a new empty MuteAction.
	 */
	public MuteAudioAction()
	{

	}

	/**
	 * Creates a new MuteAction that Mutes or Unmutes the two given channel.
	 * 
	 * @param channel
	 *            the name of the channel to Mute.
	 *            
	 * @param direction
	 *            the audio direction which is to be muted/unmuted
	 *            
	 * @param state controls whether we are muting or unmuting the channel.
	 */
	
	public MuteAudioAction(String channel, Direction direction, State state)
	{
		this.channel = channel;
		this.direction = direction;
		this.state = state;
	}

	/**
	 * Returns the name of this action, i.e. "MuteAudio".
	 */
	@Override
	public String getAction()
	{
		return "MuteAudio"; //$NON-NLS-1$
	}
	
    /**
     * Returns the name of the channel to monitor.
     */	
	public String getChannel()
	{
		return this.channel;
	}
	
    /**
     * Returns the audio direction which is to be muted/unmuted.
     */	
	public Direction getDirection()
	{
		return this.direction;
	}
	
    /**
     * Returns the state controls whether we are muting or unmuting the channel.
     */	
	public State getState()
	{
		return this.state;
	}

    /**
     * Sets the name of the channel to monitor.<p>
     * This property is mandatory.
     */	
    public void setChannel(String channel) {
        this.channel = channel;
    }

    /**
     * Sets the audio direction which is to be muted/unmuted.<p>
     * This property is mandatory.
     */	
    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    /**
     * Sets the state controls whether we are muting or unmuting the channel.<p>
     * This property is mandatory.
     */		
    public void setState(State state) {
        this.state = state;
    }	

}
