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
 * https://wiki.asterisk.org/wiki/display/AST/Asterisk+11+ManagerEvent_LocalBridge
 * Triggered when 2 halves of a local channels are bridged
 *
 * It is implemented in <code>channels/chan_local.c</code>
 *
 * @author jylebleu
 * @version $Id$
 */
public class LocalBridgeEvent extends ManagerEvent
{
    /**
     * Serializable version identifier.
     */
    static final long serialVersionUID = 0L;

    private String uniqueId1;
    private String uniqueId2;
    private String channel1;
    private String channel2;
    private String callerId1;
    private String callerId2;
    private String localOptimization;
    private String localOneCalleridName;
    private String localTwoChannel;
    private String localTwoLanguage;
    private String localTwoExten;
    private String localOneChannel;
    private String localOneContext;
    private String localOneConnectedLineNum;
    private String localOneConnectedLineName;
    private String localOneChannelStateDesc;
    private String localOneChannelState;
    private String localOneExten;
    private String localOneLanguage;
    private String localOnePriority;
    private String localOneUniqueId;
    private String localTwochannelState;
    private String localTwoChannelStateDesc;
    private String localTwoPriority;
    private String localTwoContext;
    private String localTwoCalleridNum;
    private String localTwoCalleridName;
    private String localTwoUniqueid;
    private String localOneCalleridNum;
    private String localTwoConnectedLineName;
    private String localTwoConnectedLineNum;
    
    private String localTwoLinkedid;
    private String localOneLinkedid;
    private String localOneAccountCode;
    private String localTwoAccountCode;

    public LocalBridgeEvent(Object source)
    {
        super(source);
    }

    public String getUniqueId1() {
        return uniqueId1;
    }

    public void setUniqueId1(String uniqueId1) {
        this.uniqueId1 = uniqueId1;
    }

    public String getUniqueId2() {
        return uniqueId2;
    }

    public void setUniqueId2(String uniqueId2) {
        this.uniqueId2 = uniqueId2;
    }

    /**
     * The name of the Local Channel half that bridges to another channel.
     *
     * @return The name of the Local Channel half that bridges to another channel.
     */
    public String getChannel1() {
        return channel1;
    }

    public void setChannel1(String channel1) {
        this.channel1 = channel1;
    }

    /**
     * The name of the Local Channel half that executes the dialplan.
     *
     * @return The name of the Local Channel half that executes the dialplan.
     */
    public String getChannel2() {
        return channel2;
    }

    public void setChannel2(String channel2) {
        this.channel2 = channel2;
    }

    public String getCallerId1() {
        return callerId1;
    }

    public void setCallerId1(String callerId1) {
        this.callerId1 = callerId1;
    }

    public String getCallerId2() {
        return callerId2;
    }

    public void setCallerId2(String callerId2) {
        this.callerId2 = callerId2;
    }

    /**
     * Local optimization values
     *
     *  Yes
     *  No
     *
     * @return Local optimization
     */

    public String getLocalOptimization() {
        return localOptimization;
    }

    public void setLocalOptimization(String localOptimization) {
        this.localOptimization = localOptimization;
    }

    public String getLocalOneCalleridName()
    {
        return localOneCalleridName;
    }

    public void setLocalOneCalleridName(String localOneCalleridName)
    {
        this.localOneCalleridName = localOneCalleridName;
    }

    public String getLocalTwoChannel()
    {
        return localTwoChannel;
    }

    public void setLocalTwoChannel(String localTwoChannel)
    {
        this.localTwoChannel = localTwoChannel;
    }

    public String getLocalTwoLanguage()
    {
        return localTwoLanguage;
    }

    public void setLocalTwoLanguage(String localTwoLanguage)
    {
        this.localTwoLanguage = localTwoLanguage;
    }

    public String getLocalTwoExten()
    {
        return localTwoExten;
    }

    public void setLocalTwoExten(String localTwoExten)
    {
        this.localTwoExten = localTwoExten;
    }

    public String getLocalOneChannel()
    {
        return localOneChannel;
    }

    public void setLocalOneChannel(String localOneChannel)
    {
        this.localOneChannel = localOneChannel;
    }

    public String getLocalOneContext()
    {
        return localOneContext;
    }

    public void setLocalOneContext(String localOneContext)
    {
        this.localOneContext = localOneContext;
    }

    public String getLocalOneConnectedLineNum()
    {
        return localOneConnectedLineNum;
    }

    public void setLocalOneConnectedLineNum(String localOneConnectedLineNum)
    {
        this.localOneConnectedLineNum = localOneConnectedLineNum;
    }

    public String getLocalOneConnectedLineName()
    {
        return localOneConnectedLineName;
    }

    public void setLocalOneConnectedLineName(String localOneConnectedLineName)
    {
        this.localOneConnectedLineName = localOneConnectedLineName;
    }

    public String getLocalOneChannelStateDesc()
    {
        return localOneChannelStateDesc;
    }

    public void setLocalOneChannelStateDesc(String localOneChannelStateDesc)
    {
        this.localOneChannelStateDesc = localOneChannelStateDesc;
    }

    public String getLocalOneChannelState()
    {
        return localOneChannelState;
    }

    public void setLocalOneChannelState(String localOneChannelState)
    {
        this.localOneChannelState = localOneChannelState;
    }

    public String getLocalOneExten()
    {
        return localOneExten;
    }

    public void setLocalOneExten(String localOneExten)
    {
        this.localOneExten = localOneExten;
    }

    public String getLocalOneLanguage()
    {
        return localOneLanguage;
    }

    public void setLocalOneLanguage(String localOneLanguage)
    {
        this.localOneLanguage = localOneLanguage;
    }

    public String getLocalOnePriority()
    {
        return localOnePriority;
    }

    public void setLocalOnePriority(String localOnePriority)
    {
        this.localOnePriority = localOnePriority;
    }

    public String getLocalOneUniqueId()
    {
        return localOneUniqueId;
    }

    public void setLocalOneUniqueId(String localOneUniqueId)
    {
        this.localOneUniqueId = localOneUniqueId;
    }

    public String getLocalTwochannelState()
    {
        return localTwochannelState;
    }

    public void setLocalTwochannelState(String localTwochannelState)
    {
        this.localTwochannelState = localTwochannelState;
    }

    public String getLocalTwoChannelStateDesc()
    {
        return localTwoChannelStateDesc;
    }

    public void setLocalTwoChannelStateDesc(String localTwoChannelStateDesc)
    {
        this.localTwoChannelStateDesc = localTwoChannelStateDesc;
    }

    public String getLocalTwoPriority()
    {
        return localTwoPriority;
    }

    public void setLocalTwoPriority(String localTwoPriority)
    {
        this.localTwoPriority = localTwoPriority;
    }

    public String getLocalTwoContext()
    {
        return localTwoContext;
    }

    public void setLocalTwoContext(String localTwoContext)
    {
        this.localTwoContext = localTwoContext;
    }

    public String getLocalTwoCalleridNum()
    {
        return localTwoCalleridNum;
    }

    public void setLocalTwoCalleridNum(String localTwoCalleridNum)
    {
        this.localTwoCalleridNum = localTwoCalleridNum;
    }

    public String getLocalTwoCalleridName()
    {
        return localTwoCalleridName;
    }

    public void setLocalTwoCalleridName(String localTwoCalleridName)
    {
        this.localTwoCalleridName = localTwoCalleridName;
    }

    public String getLocalTwoUniqueid()
    {
        return localTwoUniqueid;
    }

    public void setLocalTwoUniqueid(String localTwoUniqueid)
    {
        this.localTwoUniqueid = localTwoUniqueid;
    }

    public String getLocalOneCalleridNum()
    {
        return localOneCalleridNum;
    }

    public void setLocalOneCalleridNum(String localOneCalleridNum)
    {
        this.localOneCalleridNum = localOneCalleridNum;
    }

    public String getLocalTwoConnectedLineName()
    {
        return localTwoConnectedLineName;
    }

    public void setLocalTwoConnectedLineName(String localTwoConnectedLineName)
    {
        this.localTwoConnectedLineName = localTwoConnectedLineName;
    }

    public String getLocalTwoConnectedLineNum()
    {
        return localTwoConnectedLineNum;
    }

    public void setLocalTwoConnectedLineNum(String localTwoConnectedLineNum)
    {
        this.localTwoConnectedLineNum = localTwoConnectedLineNum;
    }

    public String getLocalTwoLinkedid()
    {
        return localTwoLinkedid;
    }

    public void setLocalTwoLinkedid(String localTwoLinkedid)
    {
        this.localTwoLinkedid = localTwoLinkedid;
    }

    public String getLocalOneLinkedid()
    {
        return localOneLinkedid;
    }

    public void setLocalOneLinkedid(String localOneLinkedid)
    {
        this.localOneLinkedid = localOneLinkedid;
    }

    public String getLocalOneAccountCode()
    {
        return localOneAccountCode;
    }

    public void setLocalOneAccountCode(String localOneAccountCode)
    {
        this.localOneAccountCode = localOneAccountCode;
    }

    public String getLocalTwoAccountCode()
    {
        return localTwoAccountCode;
    }

    public void setLocalTwoAccountCode(String localTwoAccountCode)
    {
        this.localTwoAccountCode = localTwoAccountCode;
    }
}
