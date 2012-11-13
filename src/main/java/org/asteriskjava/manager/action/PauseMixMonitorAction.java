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
 * The PauseMixMonitorAction temporarily stop/start monitoring (recording) a/both channel(s).
 *
 * Available since Asterisk 1.4.
 *   Channel - Used to specify the channel to mute.
 *   Direction - Which part of the recording to mute: read, write or both (from channel, to channel or both channels).
 *   State - Turn mute on or off : 1 to turn on, 0 to turn off.
 *
 * @author Adrian Videanu
 * @since 0.3
 * @version $Id$
 */

public class PauseMixMonitorAction extends AbstractManagerAction{
		
	private static final long serialVersionUID = -7438670441797420390L;
		
	private String channel;
    private Integer state;
    private String direction;
	    
    public PauseMixMonitorAction() {
    	super();
    }    
    
    public PauseMixMonitorAction(String ch, Integer st,String dir) {
        this.channel = ch;
        this.state = st;
        this.direction = dir;
    }
    
	@Override
	public String getAction() {
		return "MixMonitorMute";
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}
}
