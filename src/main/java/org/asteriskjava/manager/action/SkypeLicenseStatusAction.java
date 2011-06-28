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
package org.asteriskjava.manager.action;

import org.asteriskjava.manager.ExpectedResponse;
import org.asteriskjava.manager.response.SkypeLicenseStatusResponse;

/**
 * The SkypeLicenseStatusAction returns the total number of Skype calls licensed.<p>
 * Available with Skype for Asterisk.
 *
 * @since 1.0.0
 */
@ExpectedResponse(SkypeLicenseStatusResponse.class)
public class SkypeLicenseStatusAction extends AbstractManagerAction
{
    /**
     * Serializable version identifier
     */
    static final long serialVersionUID = 1L;

    /**
     * Creates a new SkypeLicenseStatusAction.
     */
    public SkypeLicenseStatusAction()
    {

    }

    /**
     * Returns the name of this action, i.e. "SkypeLicenseStatus".
     */
    @Override
    public String getAction()
    {
        return "SkypeLicenseStatus";
    }
}
