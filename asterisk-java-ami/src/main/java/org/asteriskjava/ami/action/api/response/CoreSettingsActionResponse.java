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
package org.asteriskjava.ami.action.api.response;

import org.asteriskjava.ami.action.api.CoreSettingsAction;
import org.asteriskjava.core.databind.annotation.AsteriskDeserialize;
import org.asteriskjava.core.databind.deserializer.AsteriskBooleanDeserializer;

import java.io.Serial;

/**
 * Corresponds to a {@link CoreSettingsAction} and contains the current settings summary of the Asterisk server.
 *
 * @author Stefan Reuter
 * @author Piotr Olaszewski
 * @since 1.0.0
 */
public class CoreSettingsActionResponse extends ManagerActionResponse {
    @Serial
    private static final long serialVersionUID = 1L;

    private String amiVersion;
    private String asteriskVersion;
    private String systemName;
    private Integer coreMaxCalls;
    private Double coreMaxLoadAvg;
    private String coreRunUser;
    private String coreRunGroup;
    private Integer coreMaxFilehandles;
    @AsteriskDeserialize(deserializer = AsteriskBooleanDeserializer.class)
    private boolean coreRealtimeEnabled;
    @AsteriskDeserialize(deserializer = AsteriskBooleanDeserializer.class)
    private boolean coreCdrEnabled;
    @AsteriskDeserialize(deserializer = AsteriskBooleanDeserializer.class)
    private boolean coreHttpEnabled;

    public String getAmiVersion() {
        return amiVersion;
    }

    public void setAmiVersion(String amiVersion) {
        this.amiVersion = amiVersion;
    }

    public String getAsteriskVersion() {
        return asteriskVersion;
    }

    public void setAsteriskVersion(String asteriskVersion) {
        this.asteriskVersion = asteriskVersion;
    }

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

    public boolean isCoreRealtimeEnabled() {
        return coreRealtimeEnabled;
    }

    public void setCoreRealtimeEnabled(boolean coreRealtimeEnabled) {
        this.coreRealtimeEnabled = coreRealtimeEnabled;
    }

    public boolean isCoreCdrEnabled() {
        return coreCdrEnabled;
    }

    public void setCoreCdrEnabled(boolean coreCdrEnabled) {
        this.coreCdrEnabled = coreCdrEnabled;
    }

    public boolean isCoreHttpEnabled() {
        return coreHttpEnabled;
    }

    public void setCoreHttpEnabled(boolean coreHttpEnabled) {
        this.coreHttpEnabled = coreHttpEnabled;
    }
}
