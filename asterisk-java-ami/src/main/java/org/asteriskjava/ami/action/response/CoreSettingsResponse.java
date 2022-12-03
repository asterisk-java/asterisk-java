/*
 * Copyright 2004-2022 Asterisk Java contributors
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
package org.asteriskjava.ami.action.response;

import org.asteriskjava.ami.action.CoreSettingsAction;

/**
 * Corresponds to a {@link CoreSettingsAction} and contains the current settings summary of the Asterisk server.
 *
 * @author Stefan Reuter
 * @see CoreSettingsAction
 */
public class CoreSettingsResponse extends ManagerResponse {
    private static final long serialVersionUID = 1L;

    private String amiVersion;
    private String asteriskVersion;
    private String systemName;
    private Integer coreMaxCalls;
    private Double coreMaxLoadAvg;
    private String coreRunUser;
    private String coreRunGroup;
    private Integer coreMaxFilehandles;
    private Boolean coreRealtimeEnabled;
    private Boolean coreCdrEnabled;
    private Boolean coreHttpEnabled;

    /**
     * Returns the version of the Asterisk Manager Interface (AMI).
     */
    public String getAmiVersion() {
        return amiVersion;
    }

    public void setAmiVersion(String amiVersion) {
        this.amiVersion = amiVersion;
    }

    /**
     * Returns the version of the Asterisk server.
     */
    public String getAsteriskVersion() {
        return asteriskVersion;
    }

    public void setAsteriskVersion(String asteriskVersion) {
        this.asteriskVersion = asteriskVersion;
    }

    /**
     * Returns the system name.
     */
    public String getSystemName() {
        return systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

    public Integer getCoreMaxCalls() {
        return coreMaxCalls;
    }

    public void setCoreMaxCalls(Integer coreMaxCalls) {
        this.coreMaxCalls = coreMaxCalls;
    }

    public Double getCoreMaxLoadAvg() {
        return coreMaxLoadAvg;
    }

    public void setCoreMaxLoadAvg(Double coreMaxLoadAvg) {
        this.coreMaxLoadAvg = coreMaxLoadAvg;
    }

    public String getCoreRunUser() {
        return coreRunUser;
    }

    public void setCoreRunUser(String coreRunUser) {
        this.coreRunUser = coreRunUser;
    }

    public String getCoreRunGroup() {
        return coreRunGroup;
    }

    public void setCoreRunGroup(String coreRunGroup) {
        this.coreRunGroup = coreRunGroup;
    }

    public Integer getCoreMaxFilehandles() {
        return coreMaxFilehandles;
    }

    public void setCoreMaxFilehandles(Integer coreMaxFilehandles) {
        this.coreMaxFilehandles = coreMaxFilehandles;
    }

    /**
     * Checks whether the realtime subsystem is enabled.
     */
    public boolean isCoreRealtimeEnabled() {
        return coreRealtimeEnabled != null && coreRealtimeEnabled;
    }

    public void setCoreRealtimeEnabled(Boolean coreRealtimeEnabled) {
        this.coreRealtimeEnabled = coreRealtimeEnabled;
    }

    /**
     * Checks whether the CDR (call detail records) subsystem is enabled.
     */
    public boolean isCoreCdrEnabled() {
        return coreCdrEnabled != null && coreCdrEnabled;
    }

    public void setCoreCdrEnabled(Boolean coreCdrEnabled) {
        this.coreCdrEnabled = coreCdrEnabled;
    }

    /**
     * Checks whether the HTTP subsystem is enabled.
     */
    public boolean isCoreHttpEnabled() {
        return coreHttpEnabled != null && coreHttpEnabled;
    }

    public void setCoreHttpEnabled(Boolean coreHttpEnabled) {
        this.coreHttpEnabled = coreHttpEnabled;
    }
}
