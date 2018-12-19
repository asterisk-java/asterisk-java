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

public class DialStateEvent extends ManagerEvent
{
    private static final long serialVersionUID = 1L;

    String destAccountCode;
    String destCallerIdName;
    String destCallerIdNum;
    String destChannel;
    String destChannelState;
    String destChannelStateDesc;
    String destConnectedLineName;
    String destConnectedLineNum;
    String destContext;
    String destExten;
    String destLanguage;
    String destLinkedId;
    String destPriority;
    String destUniqueId;
    String dialStatus;
    String privilege;
    String uniqueId;
    String linkedId;
    String accountCode;
    String language;
    String channel;

    public DialStateEvent(Object source)
    {
        super(source);
    }

    public String getDestAccountCode()
    {
        return destAccountCode;
    }

    public void setDestAccountCode(String destAccountCode)
    {
        this.destAccountCode = destAccountCode;
    }

    public String getDestCallerIdName()
    {
        return destCallerIdName;
    }

    public void setDestCallerIdName(String destCallerIdName)
    {
        this.destCallerIdName = destCallerIdName;
    }

    public String getDestCallerIdNum()
    {
        return destCallerIdNum;
    }

    public void setDestCallerIdNum(String destCallerIdNum)
    {
        this.destCallerIdNum = destCallerIdNum;
    }

    public String getDestChannel()
    {
        return destChannel;
    }

    public void setDestChannel(String destChannel)
    {
        this.destChannel = destChannel;
    }

    public String getDestChannelState()
    {
        return destChannelState;
    }

    public void setDestChannelState(String destChannelState)
    {
        this.destChannelState = destChannelState;
    }

    public String getDestChannelStateDesc()
    {
        return destChannelStateDesc;
    }

    public void setDestChannelStateDesc(String destChannelStateDesc)
    {
        this.destChannelStateDesc = destChannelStateDesc;
    }

    public String getDestConnectedLineName()
    {
        return destConnectedLineName;
    }

    public void setDestConnectedLineName(String destConnectedLineName)
    {
        this.destConnectedLineName = destConnectedLineName;
    }

    public String getDestConnectedLineNum()
    {
        return destConnectedLineNum;
    }

    public void setDestConnectedLineNum(String destConnectedLineNum)
    {
        this.destConnectedLineNum = destConnectedLineNum;
    }

    public String getDestContext()
    {
        return destContext;
    }

    public void setDestContext(String destContext)
    {
        this.destContext = destContext;
    }

    public String getDestExten()
    {
        return destExten;
    }

    public void setDestExten(String destExten)
    {
        this.destExten = destExten;
    }

    public String getDestLanguage()
    {
        return destLanguage;
    }

    public void setDestLanguage(String destLanguage)
    {
        this.destLanguage = destLanguage;
    }

    public String getDestLinkedId()
    {
        return destLinkedId;
    }

    public void setDestLinkedId(String destLinkedId)
    {
        this.destLinkedId = destLinkedId;
    }

    public String getDestPriority()
    {
        return destPriority;
    }

    public void setDestPriority(String destPriority)
    {
        this.destPriority = destPriority;
    }

    public String getDestUniqueId()
    {
        return destUniqueId;
    }

    public void setDestUniqueId(String destUniqueId)
    {
        this.destUniqueId = destUniqueId;
    }

    public String getDialStatus()
    {
        return dialStatus;
    }

    public void setDialStatus(String dialStatus)
    {
        this.dialStatus = dialStatus;
    }

    public String getPrivilege()
    {
        return privilege;
    }

    public void setPrivilege(String privilege)
    {
        this.privilege = privilege;
    }

    public String getUniqueId()
    {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId)
    {
        this.uniqueId = uniqueId;
    }

    public String getLinkedId()
    {
        return linkedId;
    }

    public void setLinkedId(String linkedId)
    {
        this.linkedId = linkedId;
    }

    public String getAccountCode()
    {
        return accountCode;
    }

    public void setAccountCode(String accountCode)
    {
        this.accountCode = accountCode;
    }

    public String getLanguage()
    {
        return language;
    }

    public void setLanguage(String language)
    {
        this.language = language;
    }

    public String getChannel()
    {
        return channel;
    }

    public void setChannel(String channel)
    {
        this.channel = channel;
    }
}
