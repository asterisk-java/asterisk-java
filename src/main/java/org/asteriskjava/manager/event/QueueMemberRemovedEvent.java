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
 * A QueueMemberRemovedEvent is triggered when a queue member is removed from a
 * queue.<p>
 * It is implemented in <code>apps/app_queue.c</code>.<p>
 * Available since Asterisk 1.2
 * 
 * @author srt
 * @version $Id$
 * @since 0.2
 */
public class QueueMemberRemovedEvent extends AbstractQueueMemberEvent
{
    /**
     * Serial version identifier.
     */
    private static final long serialVersionUID = 2108033737226142194L;
    
    Boolean paused;
    Integer penalty;
    String stateinterface;
    String membership;
    String _interface;
    Long callstaken;
    Boolean ringinuse;
    Long lastcall;
    Integer status;
    

    public QueueMemberRemovedEvent(Object source)
    {
        super(source);
    }


	public Boolean getPaused()
	{
		return paused;
	}


	public void setPaused(Boolean paused)
	{
		this.paused = paused;
	}


	public Integer getPenalty()
	{
		return penalty;
	}


	public void setPenalty(Integer penalty)
	{
		this.penalty = penalty;
	}


	public String getStateinterface()
	{
		return stateinterface;
	}


	public void setStateinterface(String stateinterface)
	{
		this.stateinterface = stateinterface;
	}


	public String getMembership()
	{
		return membership;
	}


	public void setMembership(String membership)
	{
		this.membership = membership;
	}


	public String getInterface()
	{
		return _interface;
	}


	public void setInterface(String _interface)
	{
		this._interface = _interface;
	}


	public Long getCallstaken()
	{
		return callstaken;
	}


	public void setCallstaken(Long callstaken)
	{
		this.callstaken = callstaken;
	}


	public Boolean getRinginuse()
	{
		return ringinuse;
	}


	public void setRinginuse(Boolean ringinuse)
	{
		this.ringinuse = ringinuse;
	}


	public Long getLastcall()
	{
		return lastcall;
	}


	public void setLastcall(Long lastcall)
	{
		this.lastcall = lastcall;
	}


	public Integer getStatus()
	{
		return status;
	}


	public void setStatus(Integer status)
	{
		this.status = status;
	}
}
