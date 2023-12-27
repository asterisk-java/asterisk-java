/*
 * Copyright 2004-2023 Asterisk Java contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.asteriskjava.ami.action.api;

import org.asteriskjava.ami.action.annotation.ExpectedResponse;
import org.asteriskjava.ami.action.api.response.DefaultActionResponse;
import org.asteriskjava.core.databind.annotation.AsteriskDeserialize;
import org.asteriskjava.core.databind.annotation.AsteriskName;
import org.asteriskjava.core.databind.deserializer.AsteriskBooleanDeserializer;

import java.io.Serial;

/**
 * Add interface to queue.
 * <p>
 * Supported Asterisk versions:
 * <ul>
 *     <li>18 - <a href="https://docs.asterisk.org/Asterisk_18_Documentation/API_Documentation/AMI_Actions/QueueAdd/">QueueAdd</a></li>
 *     <li>20 - <a href="https://docs.asterisk.org/Asterisk_20_Documentation/API_Documentation/AMI_Actions/QueueAdd/">QueueAdd</a></li>
 * </ul>
 *
 * @author Stefan Reuter
 * @author Piotr Olaszewski
 * @since 1.0.0
 */
@ExpectedResponse(DefaultActionResponse.class)
public class QueueAddAction extends AbstractManagerAction {
    @Serial
    private static final long serialVersionUID = 2L;

    private String queue;
    @AsteriskName("Interface")
    private String iface;
    private int penalty;
    @AsteriskDeserialize(deserializer = AsteriskBooleanDeserializer.class)
    private boolean paused;
    private String memberName;
    private String stateInterface;

    @Override
    public String getAction() {
        return "QueueAdd";
    }

    public String getQueue() {
        return queue;
    }

    public void setQueue(String queue) {
        this.queue = queue;
    }

    public String getInterface() {
        return iface;
    }

    public void setInterface(String iface) {
        this.iface = iface;
    }

    public int getPenalty() {
        return penalty;
    }

    public void setPenalty(int penalty) {
        this.penalty = penalty;
    }

    public boolean getPaused() {
        return paused;
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getStateInterface() {
        return stateInterface;
    }

    public void setStateInterface(String stateInterface) {
        this.stateInterface = stateInterface;
    }
}
