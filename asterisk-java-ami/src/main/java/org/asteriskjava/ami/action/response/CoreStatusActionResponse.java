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
package org.asteriskjava.ami.action.response;

import org.asteriskjava.ami.action.CoreStatusAction;

import java.io.Serial;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Corresponds to a {@link CoreStatusAction} and contains the current status summary of the Asterisk server.
 *
 * @author Stefan Reuter
 * @since 1.0.0
 */
public class CoreStatusActionResponse extends ManagerActionResponse {
    @Serial
    private static final long serialVersionUID = 1L;

    private LocalDate coreStartupDate;
    private LocalTime coreStartupTime;
    private LocalDate coreReloadDate;
    private LocalTime coreReloadTime;
    private int coreCurrentCalls;

    public LocalDate getCoreStartupDate() {
        return coreStartupDate;
    }

    public void setCoreStartupDate(LocalDate coreStartupDate) {
        this.coreStartupDate = coreStartupDate;
    }

    public LocalTime getCoreStartupTime() {
        return coreStartupTime;
    }

    public void setCoreStartupTime(LocalTime coreStartupTime) {
        this.coreStartupTime = coreStartupTime;
    }

    public LocalDate getCoreReloadDate() {
        return coreReloadDate;
    }

    public void setCoreReloadDate(LocalDate coreReloadDate) {
        this.coreReloadDate = coreReloadDate;
    }

    public LocalTime getCoreReloadTime() {
        return coreReloadTime;
    }

    public void setCoreReloadTime(LocalTime coreReloadTime) {
        this.coreReloadTime = coreReloadTime;
    }

    public int getCoreCurrentCalls() {
        return coreCurrentCalls;
    }

    public void setCoreCurrentCalls(int coreCurrentCalls) {
        this.coreCurrentCalls = coreCurrentCalls;
    }
}
