/*
 *  Copyright 2018 Sean Bright
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
 * Raised when two halves of a Local Channel begin to optimize themselves out
 * of the media path.
 *
 * Available since Asterisk 12
 */
public class LocalOptimizationBeginEvent extends ManagerEvent
{
    private static final long serialVersionUID = 0L;

    private Integer id;
    private String destUniqueId;

    private String localOneChannel;
    private String localOneChannelState;
    private String localOneChannelStateDesc;
    private String localOneCallerIDNum;
    private String localOneCallerIDName;
    private String localOneConnectedLineNum;
    private String localOneConnectedLineName;
    private String localOneAccountCode;
    private String localOneContext;
    private String localOneExten;
    private Integer localOnePriority;
    private String localOneUniqueid;
    private String localOneLinkedid;

    private String localTwoChannel;
    private String localTwoChannelState;
    private String localTwoChannelStateDesc;
    private String localTwoCallerIDNum;
    private String localTwoCallerIDName;
    private String localTwoConnectedLineNum;
    private String localTwoConnectedLineName;
    private String localTwoAccountCode;
    private String localTwoContext;
    private String localTwoExten;
    private Integer localTwoPriority;
    private String localTwoUniqueid;
    private String localTwoLinkedid;

    private String sourceChannel;
    private String sourceChannelState;
    private String sourceChannelStateDesc;
    private String sourceCallerIDNum;
    private String sourceCallerIDName;
    private String sourceConnectedLineNum;
    private String sourceConnectedLineName;
    private String sourceAccountCode;
    private String sourceContext;
    private String sourceExten;
    private Integer sourcePriority;
    private String sourceUniqueid;
    private String sourceLinkedid;

    public LocalOptimizationBeginEvent(Object source)
    {
        super(source);
    }

    public Integer getId()
    {
        return this.id;
    }

    public void setId(Integer value)
    {
        this.id = value;
    }

    public String getDestUniqueId()
    {
        return this.destUniqueId;
    }

    public void setDestUniqueId(String value)
    {
        this.destUniqueId = value;
    }

    public String getLocalOneChannel()
    {
        return this.localOneChannel;
    }

    public void setLocalOneChannel(String value)
    {
        this.localOneChannel = value;
    }

    public String getLocalOneChannelState()
    {
        return this.localOneChannelState;
    }

    public void setLocalOneChannelState(String value)
    {
        this.localOneChannelState = value;
    }

    public String getLocalOneChannelStateDesc()
    {
        return this.localOneChannelStateDesc;
    }

    public void setLocalOneChannelStateDesc(String value)
    {
        this.localOneChannelStateDesc = value;
    }

    public String getLocalOneCallerIDNum()
    {
        return this.localOneCallerIDNum;
    }

    public void setLocalOneCallerIDNum(String value)
    {
        this.localOneCallerIDNum = value;
    }

    public String getLocalOneCallerIDName()
    {
        return this.localOneCallerIDName;
    }

    public void setLocalOneCallerIDName(String value)
    {
        this.localOneCallerIDName = value;
    }

    public String getLocalOneConnectedLineNum()
    {
        return this.localOneConnectedLineNum;
    }

    public void setLocalOneConnectedLineNum(String value)
    {
        this.localOneConnectedLineNum = value;
    }

    public String getLocalOneConnectedLineName()
    {
        return this.localOneConnectedLineName;
    }

    public void setLocalOneConnectedLineName(String value)
    {
        this.localOneConnectedLineName = value;
    }

    public String getLocalOneAccountCode()
    {
        return this.localOneAccountCode;
    }

    public void setLocalOneAccountCode(String value)
    {
        this.localOneAccountCode = value;
    }

    public String getLocalOneContext()
    {
        return this.localOneContext;
    }

    public void setLocalOneContext(String value)
    {
        this.localOneContext = value;
    }

    public String getLocalOneExten()
    {
        return this.localOneExten;
    }

    public void setLocalOneExten(String value)
    {
        this.localOneExten = value;
    }

    public Integer getLocalOnePriority()
    {
        return this.localOnePriority;
    }

    public void setLocalOnePriority(Integer value)
    {
        this.localOnePriority = value;
    }

    public String getLocalOneUniqueid()
    {
        return this.localOneUniqueid;
    }

    public void setLocalOneUniqueid(String value)
    {
        this.localOneUniqueid = value;
    }

    public String getLocalOneLinkedid()
    {
        return this.localOneLinkedid;
    }

    public void setLocalOneLinkedid(String value)
    {
        this.localOneLinkedid = value;
    }

    public String getLocalTwoChannel()
    {
        return this.localTwoChannel;
    }

    public void setLocalTwoChannel(String value)
    {
        this.localTwoChannel = value;
    }

    public String getLocalTwoChannelState()
    {
        return this.localTwoChannelState;
    }

    public void setLocalTwoChannelState(String value)
    {
        this.localTwoChannelState = value;
    }

    public String getLocalTwoChannelStateDesc()
    {
        return this.localTwoChannelStateDesc;
    }

    public void setLocalTwoChannelStateDesc(String value)
    {
        this.localTwoChannelStateDesc = value;
    }

    public String getLocalTwoCallerIDNum()
    {
        return this.localTwoCallerIDNum;
    }

    public void setLocalTwoCallerIDNum(String value)
    {
        this.localTwoCallerIDNum = value;
    }

    public String getLocalTwoCallerIDName()
    {
        return this.localTwoCallerIDName;
    }

    public void setLocalTwoCallerIDName(String value)
    {
        this.localTwoCallerIDName = value;
    }

    public String getLocalTwoConnectedLineNum()
    {
        return this.localTwoConnectedLineNum;
    }

    public void setLocalTwoConnectedLineNum(String value)
    {
        this.localTwoConnectedLineNum = value;
    }

    public String getLocalTwoConnectedLineName()
    {
        return this.localTwoConnectedLineName;
    }

    public void setLocalTwoConnectedLineName(String value)
    {
        this.localTwoConnectedLineName = value;
    }

    public String getLocalTwoAccountCode()
    {
        return this.localTwoAccountCode;
    }

    public void setLocalTwoAccountCode(String value)
    {
        this.localTwoAccountCode = value;
    }

    public String getLocalTwoContext()
    {
        return this.localTwoContext;
    }

    public void setLocalTwoContext(String value)
    {
        this.localTwoContext = value;
    }

    public String getLocalTwoExten()
    {
        return this.localTwoExten;
    }

    public void setLocalTwoExten(String value)
    {
        this.localTwoExten = value;
    }

    public Integer getLocalTwoPriority()
    {
        return this.localTwoPriority;
    }

    public void setLocalTwoPriority(Integer value)
    {
        this.localTwoPriority = value;
    }

    public String getLocalTwoUniqueid()
    {
        return this.localTwoUniqueid;
    }

    public void setLocalTwoUniqueid(String value)
    {
        this.localTwoUniqueid = value;
    }

    public String getLocalTwoLinkedid()
    {
        return this.localTwoLinkedid;
    }

    public void setLocalTwoLinkedid(String value)
    {
        this.localTwoLinkedid = value;
    }

    public String getSourceChannel()
    {
        return this.sourceChannel;
    }

    public void setSourceChannel(String value)
    {
        this.sourceChannel = value;
    }

    public String getSourceChannelState()
    {
        return this.sourceChannelState;
    }

    public void setSourceChannelState(String value)
    {
        this.sourceChannelState = value;
    }

    public String getSourceChannelStateDesc()
    {
        return this.sourceChannelStateDesc;
    }

    public void setSourceChannelStateDesc(String value)
    {
        this.sourceChannelStateDesc = value;
    }

    public String getSourceCallerIDNum()
    {
        return this.sourceCallerIDNum;
    }

    public void setSourceCallerIDNum(String value)
    {
        this.sourceCallerIDNum = value;
    }

    public String getSourceCallerIDName()
    {
        return this.sourceCallerIDName;
    }

    public void setSourceCallerIDName(String value)
    {
        this.sourceCallerIDName = value;
    }

    public String getSourceConnectedLineNum()
    {
        return this.sourceConnectedLineNum;
    }

    public void setSourceConnectedLineNum(String value)
    {
        this.sourceConnectedLineNum = value;
    }

    public String getSourceConnectedLineName()
    {
        return this.sourceConnectedLineName;
    }

    public void setSourceConnectedLineName(String value)
    {
        this.sourceConnectedLineName = value;
    }

    public String getSourceAccountCode()
    {
        return this.sourceAccountCode;
    }

    public void setSourceAccountCode(String value)
    {
        this.sourceAccountCode = value;
    }

    public String getSourceContext()
    {
        return this.sourceContext;
    }

    public void setSourceContext(String value)
    {
        this.sourceContext = value;
    }

    public String getSourceExten()
    {
        return this.sourceExten;
    }

    public void setSourceExten(String value)
    {
        this.sourceExten = value;
    }

    public Integer getSourcePriority()
    {
        return this.sourcePriority;
    }

    public void setSourcePriority(Integer value)
    {
        this.sourcePriority = value;
    }

    public String getSourceUniqueid()
    {
        return this.sourceUniqueid;
    }

    public void setSourceUniqueid(String value)
    {
        this.sourceUniqueid = value;
    }

    public String getSourceLinkedid()
    {
        return this.sourceLinkedid;
    }

    public void setSourceLinkedid(String value)
    {
        this.sourceLinkedid = value;
    }
}
