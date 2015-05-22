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
}
