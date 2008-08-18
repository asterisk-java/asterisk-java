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

    private Integer coreCurrentCalls;

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