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


public class ChanSpyStopEvent extends ManagerEvent
{
    /**
     * Serial version identifier
     */
    private static final long serialVersionUID = 3256725065466000695L;

    /**
     * The name of the channel.
     */
    private String spyeechannel;

     
    

    public ChanSpyStopEvent(Object source)
    {
        super(source);
    }

   
    public String getSpyeeChannel()
    {
        return spyeechannel;
    }

    public void setSpyeeChannel(String channel)
    {
        this.spyeechannel = channel;
    }

    
}
