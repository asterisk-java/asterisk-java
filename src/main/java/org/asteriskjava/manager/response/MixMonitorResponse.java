/*
 *  Copyright 2018 CallShaper, LLC
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
 * A MixMonitorResponse is sent in response to a MixMonitorAction and provides
 * the ID of the MixMonitor that was started on the referenced channel.
 *
 * Available since Asterisk 11
 *
 * @see org.asteriskjava.manager.action.MixMonitorAction;
 */
public class MixMonitorResponse extends ManagerResponse
{
    private static final long serialVersionUID = 1L;
    private String mixMonitorId;

    /**
     * Returns the ID of the MixMonitor
     *
     * @return the MixMonitor ID
     */
    public String getMixMonitorId()
    {
        return mixMonitorId;
    }

    /**
     * Sets the ID of the MixMonitor
     *
     * @param mixMonitorId the MixMonitor ID
     */
    public void setMixMonitorId(String mixMonitorId)
    {
        this.mixMonitorId = mixMonitorId;
    }
}
