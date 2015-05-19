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
 * A UnparkedCallEvent is triggered when a channel that has been parked is
 * resumed.
 * <p>
 * It is implemented in <code>res/res_features.c</code>
 * <p>
 * Available since Asterisk 1.2
 * 
 * @author srt
 * @version $Id$
 * @since 0.2
 */
public class UnparkedCallEvent extends AbstractUnParkedEvent
{
    /**
     * Serializable version identifier
     */
    private static final long serialVersionUID = -7437833328723536814L;

    /**
     * Enro 2015-03: Asterisk 13 Support
     */
    private String RetrieverChannel;
    private Integer RetrieverChannelState;
    private String RetrieverChannelStateDesc;
    private String RetrieverCallerIDNum;
    private String RetrieverCallerIDName;
    private String RetrieverConnectedLineNum;
    private String RetrieverConnectedLineName;
    private String RetrieverAccountCode;
    private String RetrieverContext;
    private String RetrieverExten;
    private String RetrieverPriority;
    private String RetrieverUniqueid;

    /**
     * @param source
     */
    public UnparkedCallEvent(Object source)
    {
        super(source);
    }

    public String getRetrieverChannel()
    {
        return RetrieverChannel;
    }

    public void setRetrieverChannel(String retrieverChannel)
    {
        RetrieverChannel = retrieverChannel;
    }

    public Integer getRetrieverChannelState()
    {
        return RetrieverChannelState;
    }

    public void setRetrieverChannelState(Integer retrieverChannelState)
    {
        RetrieverChannelState = retrieverChannelState;
    }

    public String getRetrieverChannelStateDesc()
    {
        return RetrieverChannelStateDesc;
    }

    public void setRetrieverChannelStateDesc(String retrieverChannelStateDesc)
    {
        RetrieverChannelStateDesc = retrieverChannelStateDesc;
    }

    public String getRetrieverCallerIDNum()
    {
        return RetrieverCallerIDNum;
    }

    public void setRetrieverCallerIDNum(String retrieverCallerIDNum)
    {
        RetrieverCallerIDNum = retrieverCallerIDNum;
    }

    public String getRetrieverCallerIDName()
    {
        return RetrieverCallerIDName;
    }

    public void setRetrieverCallerIDName(String retrieverCallerIDName)
    {
        RetrieverCallerIDName = retrieverCallerIDName;
    }

    public String getRetrieverConnectedLineNum()
    {
        return RetrieverConnectedLineNum;
    }

    public void setRetrieverConnectedLineNum(String retrieverConnectedLineNum)
    {
        RetrieverConnectedLineNum = retrieverConnectedLineNum;
    }

    public String getRetrieverConnectedLineName()
    {
        return RetrieverConnectedLineName;
    }

    public void setRetrieverConnectedLineName(String retrieverConnectedLineName)
    {
        RetrieverConnectedLineName = retrieverConnectedLineName;
    }

    public String getRetrieverAccountCode()
    {
        return RetrieverAccountCode;
    }

    public void setRetrieverAccountCode(String retrieverAccountCode)
    {
        RetrieverAccountCode = retrieverAccountCode;
    }

    public String getRetrieverContext()
    {
        return RetrieverContext;
    }

    public void setRetrieverContext(String retrieverContext)
    {
        RetrieverContext = retrieverContext;
    }

    public String getRetrieverExten()
    {
        return RetrieverExten;
    }

    public void setRetrieverExten(String retrieverExten)
    {
        RetrieverExten = retrieverExten;
    }

    public String getRetrieverPriority()
    {
        return RetrieverPriority;
    }

    public void setRetrieverPriority(String retrieverPriority)
    {
        RetrieverPriority = retrieverPriority;
    }

    public String getRetrieverUniqueid()
    {
        return RetrieverUniqueid;
    }

    public void setRetrieverUniqueid(String retrieverUniqueid)
    {
        RetrieverUniqueid = retrieverUniqueid;
    }
}
