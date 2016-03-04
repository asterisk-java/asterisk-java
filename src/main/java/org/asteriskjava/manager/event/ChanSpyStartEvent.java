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


public class ChanSpyStartEvent extends ManagerEvent
{
    /**
     * Serial version identifier
     */
    private static final long serialVersionUID = 3256725065466000696L;

    private String spyeeChannel;
    private String spyerChannel;
    
    private Integer spyeeChannelState;
    private String spyeeLinkedId;
    private String spyeeUniqueId;
    private String spyeeCallerIdNum;
    private String spyeeConnectedLineName;
    private String spyeeLanguage;
    private String spyeeContext;
    private String spyeeConnectedLineNum;
    private String spyeeChannelStateDesc;
    private String spyeeExten;
    private Integer spyeeChannelstate;
    private String spyeeAccountCode;
    private Integer spyeePriority;
    private String spyeeCallerIdName;
    
    
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
    
    

    public ChanSpyStartEvent(Object source)
    {
        super(source);
    }



    public String getSpyeeChannel()
    {
        return spyeeChannel;
    }



    public void setSpyeeChannel(String spyeeChannel)
    {
        this.spyeeChannel = spyeeChannel;
    }



    public String getSpyerChannel()
    {
        return spyerChannel;
    }



    public void setSpyerChannel(String spyerChannel)
    {
        this.spyerChannel = spyerChannel;
    }



    public Integer getSpyeeChannelState()
    {
        return spyeeChannelState;
    }



    public void setSpyeeChannelState(Integer spyeeChannelState)
    {
        this.spyeeChannelState = spyeeChannelState;
    }



    public String getSpyeeLinkedId()
    {
        return spyeeLinkedId;
    }



    public void setSpyeeLinkedId(String spyeeLinkedId)
    {
        this.spyeeLinkedId = spyeeLinkedId;
    }



    public String getSpyeeUniqueId()
    {
        return spyeeUniqueId;
    }



    public void setSpyeeUniqueId(String spyeeUniqueId)
    {
        this.spyeeUniqueId = spyeeUniqueId;
    }



    public String getSpyeeCallerIdNum()
    {
        return spyeeCallerIdNum;
    }



    public void setSpyeeCallerIdNum(String spyeeCallerIdNum)
    {
        this.spyeeCallerIdNum = spyeeCallerIdNum;
    }



    public String getSpyeeConnectedLineName()
    {
        return spyeeConnectedLineName;
    }



    public void setSpyeeConnectedLineName(String spyeeConnectedLineName)
    {
        this.spyeeConnectedLineName = spyeeConnectedLineName;
    }



    public String getSpyeeLanguage()
    {
        return spyeeLanguage;
    }



    public void setSpyeeLanguage(String spyeeLanguage)
    {
        this.spyeeLanguage = spyeeLanguage;
    }



    public String getSpyeeContext()
    {
        return spyeeContext;
    }



    public void setSpyeeContext(String spyeeContext)
    {
        this.spyeeContext = spyeeContext;
    }



    public String getSpyeeConnectedLineNum()
    {
        return spyeeConnectedLineNum;
    }



    public void setSpyeeConnectedLineNum(String spyeeConnectedLineNum)
    {
        this.spyeeConnectedLineNum = spyeeConnectedLineNum;
    }



    public String getSpyeeChannelStateDesc()
    {
        return spyeeChannelStateDesc;
    }



    public void setSpyeeChannelStateDesc(String spyeeChannelStateDesc)
    {
        this.spyeeChannelStateDesc = spyeeChannelStateDesc;
    }



    public String getSpyeeExten()
    {
        return spyeeExten;
    }



    public void setSpyeeExten(String spyeeExten)
    {
        this.spyeeExten = spyeeExten;
    }



    public Integer getSpyeeChannelstate()
    {
        return spyeeChannelstate;
    }



    public void setSpyeeChannelstate(Integer spyeeChannelstate)
    {
        this.spyeeChannelstate = spyeeChannelstate;
    }



    public String getSpyeeAccountCode()
    {
        return spyeeAccountCode;
    }



    public void setSpyeeAccountCode(String spyeeAccountCode)
    {
        this.spyeeAccountCode = spyeeAccountCode;
    }



    public Integer getSpyeePriority()
    {
        return spyeePriority;
    }



    public void setSpyeePriority(Integer spyeePriority)
    {
        this.spyeePriority = spyeePriority;
    }



    public String getSpyeeCallerIdName()
    {
        return spyeeCallerIdName;
    }



    public void setSpyeeCallerIdName(String spyeeCallerIdName)
    {
        this.spyeeCallerIdName = spyeeCallerIdName;
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

   
 
    
    
    
}
