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
    private static final long serialVersionUID = 3256725065466000696L;

    /**
     * The name of the channel.
     */
    private String spyeechannel;

    private String spyerUniqueId;
    private String spyerLinkedId;
    private Integer spyerChannelState;
    private Integer spyerPriority;
    private String spyerContext;    
    private String spyerLanguage;
    private String spyerChannelStateDesc;    
    private String spyerExten;
    private String spyerCallerIdNum;
    private String spyerConnectedLineNum;
    private String spyerConnectedLineName;    
    private String spyerCallerIdName;
    private String spyerChannel;
    

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


    public String getSpyeechannel()
    {
        return spyeechannel;
    }


    public void setSpyeechannel(String spyeechannel)
    {
        this.spyeechannel = spyeechannel;
    }


    public String getSpyerUniqueId()
    {
        return spyerUniqueId;
    }


    public void setSpyerUniqueId(String spyerUniqueId)
    {
        this.spyerUniqueId = spyerUniqueId;
    }


    public String getSpyerLinkedId()
    {
        return spyerLinkedId;
    }


    public void setSpyerLinkedId(String spyerLinkedId)
    {
        this.spyerLinkedId = spyerLinkedId;
    }


    public Integer getSpyerChannelState()
    {
        return spyerChannelState;
    }


    public void setSpyerChannelState(Integer spyerChannelState)
    {
        this.spyerChannelState = spyerChannelState;
    }


    public Integer getSpyerPriority()
    {
        return spyerPriority;
    }


    public void setSpyerPriority(Integer spyerPriority)
    {
        this.spyerPriority = spyerPriority;
    }


    public String getSpyerContext()
    {
        return spyerContext;
    }


    public void setSpyerContext(String spyerContext)
    {
        this.spyerContext = spyerContext;
    }


    public String getSpyerLanguage()
    {
        return spyerLanguage;
    }


    public void setSpyerLanguage(String spyerLanguage)
    {
        this.spyerLanguage = spyerLanguage;
    }


    public String getSpyerChannelStateDesc()
    {
        return spyerChannelStateDesc;
    }


    public void setSpyerChannelStateDesc(String spyerChannelStateDesc)
    {
        this.spyerChannelStateDesc = spyerChannelStateDesc;
    }


    public String getSpyerExten()
    {
        return spyerExten;
    }


    public void setSpyerExten(String spyerExten)
    {
        this.spyerExten = spyerExten;
    }


    public String getSpyerCallerIdNum()
    {
        return spyerCallerIdNum;
    }


    public void setSpyerCallerIdNum(String spyerCallerIdNum)
    {
        this.spyerCallerIdNum = spyerCallerIdNum;
    }


    public String getSpyerConnectedLineNum()
    {
        return spyerConnectedLineNum;
    }


    public void setSpyerConnectedLineNum(String spyerConnectedLineNum)
    {
        this.spyerConnectedLineNum = spyerConnectedLineNum;
    }


    public String getSpyerConnectedLineName()
    {
        return spyerConnectedLineName;
    }


    public void setSpyerConnectedLineName(String spyerConnectedLineName)
    {
        this.spyerConnectedLineName = spyerConnectedLineName;
    }


    public String getSpyerCallerIdName()
    {
        return spyerCallerIdName;
    }


    public void setSpyerCallerIdName(String spyerCallerIdName)
    {
        this.spyerCallerIdName = spyerCallerIdName;
    }


    public String getSpyerChannel()
    {
        return spyerChannel;
    }


    public void setSpyerChannel(String spyerChannel)
    {
        this.spyerChannel = spyerChannel;
    }

    
    
}
