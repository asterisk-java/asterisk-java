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
 * Corresponds to a SkypeLicenseStatusAction and contains the number of licensed Skype calls.
 *
 * @see org.asteriskjava.manager.action.SkypeLicenseStatusAction
 * @since 1.0.0
 */
public class SkypeLicenseStatusResponse extends ManagerResponse
{
    private static final long serialVersionUID = 0L;

    private Integer callsLicensed;

    /**
     * Returns the total number of concurrent Skype calls currently licenced for this system.
     *
     * @return the total number of concurrent Skype calls currently licenced for this system.
     */
    public Integer getCallsLicensed()
    {
        return callsLicensed;
    }

    /**
     * Sets the total number of concurrent Skype calls currently licenced for this system.
     *
     * @param callsLicensed the total number of concurrent Skype calls currently licenced for this system.
     */
    public void setCallsLicensed(Integer callsLicensed)
    {
        this.callsLicensed = callsLicensed;
    }
}