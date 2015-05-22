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
 * Enro 2015-03: Asterisk 13 Support
 */
public class AbstractUnParkedEvent extends AbstractParkedCallEvent
{
    /**
     * Serializable version identifier
     */
    private static final long serialVersionUID = -7437833328723536814L;

    private String parkerChannel;
    private Integer parkerChannelState;
    private String parkerChannelStateDesc;
    private String parkerCallerIDNum;
    private String parkerCallerIDName;
    private String parkerConnectedLineNum;
    private String parkerConnectedLineName;
    private String parkerAccountCode;
    private String parkerContext;
    private String parkerExten;
    private String parkerPriority;
    private String parkerUniqueid;

    /**
     * @param source
     */
    public AbstractUnParkedEvent(Object source)
    {
        super(source);
    }

    public String getParkerChannel()
    {
        return parkerChannel;
    }

    public void setParkerChannel(String parkerChannel)
    {
        this.parkerChannel = parkerChannel;
    }

    public Integer getParkerChannelState()
    {
        return parkerChannelState;
    }

    public void setParkerChannelState(Integer parkerChannelState)
    {
        this.parkerChannelState = parkerChannelState;
    }

    public String getParkerChannelStateDesc()
    {
        return parkerChannelStateDesc;
    }

    public void setParkerChannelStateDesc(String parkerChannelStateDesc)
    {
        this.parkerChannelStateDesc = parkerChannelStateDesc;
    }

    public String getParkerCallerIDNum()
    {
        return parkerCallerIDNum;
    }

    public void setParkerCallerIDNum(String parkerCallerIDNum)
    {
        this.parkerCallerIDNum = parkerCallerIDNum;
    }

    public String getParkerCallerIDName()
    {
        return parkerCallerIDName;
    }

    public void setParkerCallerIDName(String parkerCallerIDName)
    {
        this.parkerCallerIDName = parkerCallerIDName;
    }

    public String getParkerConnectedLineNum()
    {
        return parkerConnectedLineNum;
    }

    public void setParkerConnectedLineNum(String parkerConnectedLineNum)
    {
        this.parkerConnectedLineNum = parkerConnectedLineNum;
    }

    public String getParkerConnectedLineName()
    {
        return parkerConnectedLineName;
    }

    public void setParkerConnectedLineName(String parkerConnectedLineName)
    {
        this.parkerConnectedLineName = parkerConnectedLineName;
    }

    public String getParkerAccountCode()
    {
        return parkerAccountCode;
    }

    public void setParkerAccountCode(String parkerAccountCode)
    {
        this.parkerAccountCode = parkerAccountCode;
    }

    public String getParkerContext()
    {
        return parkerContext;
    }

    public void setParkerContext(String parkerContext)
    {
        this.parkerContext = parkerContext;
    }

    public String getParkerExten()
    {
        return parkerExten;
    }

    public void setParkerExten(String parkerExten)
    {
        this.parkerExten = parkerExten;
    }

    public String getParkerPriority()
    {
        return parkerPriority;
    }

    public void setParkerPriority(String parkerPriority)
    {
        this.parkerPriority = parkerPriority;
    }

    public String getParkerUniqueid()
    {
        return parkerUniqueid;
    }

    public void setParkerUniqueid(String parkerUniqueid)
    {
        this.parkerUniqueid = parkerUniqueid;
    }
}
