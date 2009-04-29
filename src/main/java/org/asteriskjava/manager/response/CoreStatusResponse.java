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

    private Date coreStartupTime;
    private Date coreReloadTime;
    private Integer coreCurrentCalls;

    /**
     * Returns the date the server was started.
     * @return the date the server was started.
     */
    public Date getCoreStartupTime()
    {
        return coreStartupTime;
    }

    public void setCoreStartupTime(String s)
    {
        // format is %H:%M:%S
        final long now = new Date().getTime();
        this.coreStartupTime = new Date(now - str2milliSeconds(s));
    }

    /**
     * Returns the date the server (core module) was last reloaded.
     * @return the date the server (core module) was last reloaded.
     */
    public Date getCoreReloadTime()
    {
        return coreReloadTime;
    }

    public void setCoreReloadTime(String s)
    {
        // format is %H:%M:%S
        final long now = new Date().getTime();
        this.coreReloadTime = new Date(now - str2milliSeconds(s));
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
    
    /**
     * Converts a string formatted as %H:%M:%S to milliseconds.
     *
     * @param s the string formatted as %H:%M:%S to convert
     * @return the milliseconds the string represents
     */
    protected long str2milliSeconds(String s)
    {
        if (s == null || "".equals(s))
        {
            return 0;
        }

        String[] parts = s.split(":");
        if (s.length() != 3)
        {
            throw new IllegalArgumentException("Time must be formatted %H:%M:%S but is '" + s + "'");
        }

        final long hours = Integer.valueOf(parts[0]);
        final long minutes = Integer.valueOf(parts[1]);
        final long seconds = Integer.valueOf(parts[2]);

        return (hours * 60l * 60l + minutes * 60l + seconds) * 1000l;
    }
}