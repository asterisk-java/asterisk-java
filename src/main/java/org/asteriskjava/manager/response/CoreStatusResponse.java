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
package org.asteriskjava.manager.response;

import java.util.Date;

/**
 * Corresponds to a CoreStatusAction and contains the current status summary of the
 * Asterisk server.
 *
 * @author srt
 * @version $Id$
 * @see org.asteriskjava.manager.action.CoreStatusAction
 * @since 1.0.0
 */
public class CoreStatusResponse extends ManagerResponse
{
    private static final long serialVersionUID = 1L;

    private String coreStartupTime;
    private String coreReloadTime;
    private Integer coreCurrentCalls;

    /**
     * Returns the time the server was started. The format is %H:%M:%S.
     * Unfortunately the day is not inclued.
     *
     * @return the time the server was started.
     */
    public String getCoreStartupTime()
    {
        return coreStartupTime;
    }

    public void setCoreStartupTime(String s)
    {
        // format is %H:%M:%S
        this.coreStartupTime = s;
    }

    /**
     * Returns the time the server (core module) was last reloaded. The format is %H:%M:%S.
     * Unfortunately the day is not inclued.
     *
     * @return the time the server (core module) was last reloaded.
     */
    public String getCoreReloadTime()
    {
        return coreReloadTime;
    }

    public void setCoreReloadTime(String s)
    {
        // format is %H:%M:%S
        this.coreReloadTime = s;
    }

    /**
     * Returns the number of currently active channels on the server.
     *
     * @return the number of currently active channels on the server.
     */
    public Integer getCoreCurrentCalls()
    {
        return coreCurrentCalls;
    }

    public void setCoreCurrentCalls(Integer coreCurrentCalls)
    {
        this.coreCurrentCalls = coreCurrentCalls;
    }
}