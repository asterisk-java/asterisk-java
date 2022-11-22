/*
 * Copyright 2004-2022 Asterisk-Java contributors
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
package org.asteriskjava.manager.event;

public class ChanSpyStopEvent extends ManagerEvent {
    private static final long serialVersionUID = 3256725065466000696L;

    private String spyerChannel;
    private Integer spyerChannelState;
    private String spyerChannelStateDesc;
    private String spyerCallerIdNum;
    private String spyerCallerIdName;
    private String spyerConnectedLineNum;
    private String spyerConnectedLineName;
    private String spyerLanguage;
    private String spyerAccountCode;
    private String spyerContext;
    private String spyerExten;
    private Integer spyerPriority;
    private String spyerUniqueId;
    private String spyerLinkedId;

    private String spyeeChannel;
    private Integer spyeeChannelState;
    private String spyeeChannelStateDesc;
    private String spyeeCallerIdNum;
    private String spyeeCallerIdName;
    private String spyeeConnectedLineNum;
    private String spyeeConnectedLineName;
    private String spyeeLanguage;
    private String spyeeAccountCode;
    private String spyeeContext;
    private String spyeeExten;
    private Integer spyeePriority;
    private String spyeeUniqueId;
    private String spyeeLinkedId;

    public ChanSpyStopEvent(Object source) {
        super(source);
    }

    public String getSpyerChannel() {
        return spyerChannel;
    }

    public void setSpyerChannel(String spyerChannel) {
        this.spyerChannel = spyerChannel;
    }

    public Integer getSpyerChannelState() {
        return spyerChannelState;
    }

    public void setSpyerChannelState(Integer spyerChannelState) {
        this.spyerChannelState = spyerChannelState;
    }

    public String getSpyerChannelStateDesc() {
        return spyerChannelStateDesc;
    }

    public void setSpyerChannelStateDesc(String spyerChannelStateDesc) {
        this.spyerChannelStateDesc = spyerChannelStateDesc;
    }

    public String getSpyerCallerIdNum() {
        return spyerCallerIdNum;
    }

    public void setSpyerCallerIdNum(String spyerCallerIdNum) {
        this.spyerCallerIdNum = spyerCallerIdNum;
    }

    public String getSpyerCallerIdName() {
        return spyerCallerIdName;
    }

    public void setSpyerCallerIdName(String spyerCallerIdName) {
        this.spyerCallerIdName = spyerCallerIdName;
    }

    public String getSpyerConnectedLineNum() {
        return spyerConnectedLineNum;
    }

    public void setSpyerConnectedLineNum(String spyerConnectedLineNum) {
        this.spyerConnectedLineNum = spyerConnectedLineNum;
    }

    public String getSpyerConnectedLineName() {
        return spyerConnectedLineName;
    }

    public void setSpyerConnectedLineName(String spyerConnectedLineName) {
        this.spyerConnectedLineName = spyerConnectedLineName;
    }

    public String getSpyerLanguage() {
        return spyerLanguage;
    }

    public void setSpyerLanguage(String spyerLanguage) {
        this.spyerLanguage = spyerLanguage;
    }

    public String getSpyerAccountCode() {
        return spyerAccountCode;
    }

    public void setSpyerAccountCode(String spyerAccountCode) {
        this.spyerAccountCode = spyerAccountCode;
    }

    public String getSpyerContext() {
        return spyerContext;
    }

    public void setSpyerContext(String spyerContext) {
        this.spyerContext = spyerContext;
    }

    public String getSpyerExten() {
        return spyerExten;
    }

    public void setSpyerExten(String spyerExten) {
        this.spyerExten = spyerExten;
    }

    public Integer getSpyerPriority() {
        return spyerPriority;
    }

    public void setSpyerPriority(Integer spyerPriority) {
        this.spyerPriority = spyerPriority;
    }

    public String getSpyerUniqueId() {
        return spyerUniqueId;
    }

    public void setSpyerUniqueId(String spyerUniqueId) {
        this.spyerUniqueId = spyerUniqueId;
    }

    public String getSpyerLinkedId() {
        return spyerLinkedId;
    }

    public void setSpyerLinkedId(String spyerLinkedId) {
        this.spyerLinkedId = spyerLinkedId;
    }

    public String getSpyeeChannel() {
        return spyeeChannel;
    }

    public void setSpyeeChannel(String spyeeChannel) {
        this.spyeeChannel = spyeeChannel;
    }

    public Integer getSpyeeChannelState() {
        return spyeeChannelState;
    }

    public void setSpyeeChannelState(Integer spyeeChannelState) {
        this.spyeeChannelState = spyeeChannelState;
    }

    public String getSpyeeChannelStateDesc() {
        return spyeeChannelStateDesc;
    }

    public void setSpyeeChannelStateDesc(String spyeeChannelStateDesc) {
        this.spyeeChannelStateDesc = spyeeChannelStateDesc;
    }

    public String getSpyeeCallerIdNum() {
        return spyeeCallerIdNum;
    }

    public void setSpyeeCallerIdNum(String spyeeCallerIdNum) {
        this.spyeeCallerIdNum = spyeeCallerIdNum;
    }

    public String getSpyeeCallerIdName() {
        return spyeeCallerIdName;
    }

    public void setSpyeeCallerIdName(String spyeeCallerIdName) {
        this.spyeeCallerIdName = spyeeCallerIdName;
    }

    public String getSpyeeConnectedLineNum() {
        return spyeeConnectedLineNum;
    }

    public void setSpyeeConnectedLineNum(String spyeeConnectedLineNum) {
        this.spyeeConnectedLineNum = spyeeConnectedLineNum;
    }

    public String getSpyeeConnectedLineName() {
        return spyeeConnectedLineName;
    }

    public void setSpyeeConnectedLineName(String spyeeConnectedLineName) {
        this.spyeeConnectedLineName = spyeeConnectedLineName;
    }

    public String getSpyeeLanguage() {
        return spyeeLanguage;
    }

    public void setSpyeeLanguage(String spyeeLanguage) {
        this.spyeeLanguage = spyeeLanguage;
    }

    public String getSpyeeAccountCode() {
        return spyeeAccountCode;
    }

    public void setSpyeeAccountCode(String spyeeAccountCode) {
        this.spyeeAccountCode = spyeeAccountCode;
    }

    public String getSpyeeContext() {
        return spyeeContext;
    }

    public void setSpyeeContext(String spyeeContext) {
        this.spyeeContext = spyeeContext;
    }

    public String getSpyeeExten() {
        return spyeeExten;
    }

    public void setSpyeeExten(String spyeeExten) {
        this.spyeeExten = spyeeExten;
    }

    public Integer getSpyeePriority() {
        return spyeePriority;
    }

    public void setSpyeePriority(Integer spyeePriority) {
        this.spyeePriority = spyeePriority;
    }

    public String getSpyeeUniqueId() {
        return spyeeUniqueId;
    }

    public void setSpyeeUniqueId(String spyeeUniqueId) {
        this.spyeeUniqueId = spyeeUniqueId;
    }

    public String getSpyeeLinkedId() {
        return spyeeLinkedId;
    }

    public void setSpyeeLinkedId(String spyeeLinkedId) {
        this.spyeeLinkedId = spyeeLinkedId;
    }
}
