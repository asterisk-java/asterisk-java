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

import org.asteriskjava.manager.event.ResponseEvent;
import org.asteriskjava.manager.event.SkypeBuddyListCompleteEvent;

/**
 * The SkypeLicenseListAction lists all Skype for Asterisk licenses currently installed on
 * the system.<p>
 * For each license a SkypeLicenseEvent is generated. After all licenses have been
 * reported a SkypeLicenseListCompleteEvent is generated.<p>
 * Available with Skype for Asterisk.
 *
 * @see org.asteriskjava.manager.event.SkypeLicenseEvent
 * @see org.asteriskjava.manager.event.SkypeLicenseListCompleteEvent
 * @since 1.0.0
 */
public class SkypeLicenseListAction extends AbstractManagerAction implements EventGeneratingAction
{
    /**
     * Serializable version identifier
     */
    static final long serialVersionUID = 1L;

    /**
     * Creates a new SkypeLicenseListAction.
     */
    public SkypeLicenseListAction()
    {

    }

    /**
     * Returns the name of this action, i.e. "SkypeLicenseList".
     */
    @Override
    public String getAction()
    {
        return "SkypeLicenseList";
    }

    public Class<? extends ResponseEvent> getActionCompleteEventClass()
    {
        return SkypeBuddyListCompleteEvent.class;
    }
}
