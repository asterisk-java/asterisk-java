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
public class LocalOptimizationBeginEvent extends AbstractLocalOptimizationEvent {
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


    public String getDestUniqueId()
    {
        return this.destUniqueId;
    }

    public void setDestUniqueId(String value)
    {
        this.destUniqueId = value;
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
