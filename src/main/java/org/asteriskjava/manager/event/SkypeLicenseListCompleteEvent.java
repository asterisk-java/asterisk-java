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
package org.asteriskjava.manager.event;

/**
 * A SkypeLicenseListCompleteEvent is triggered in response to a SkypeLicenseListAction when
 * all licenses have been reported.<p>
 * It is implemented in <code>chan_skype.c</code>.<p>
 * Available with Skype for Asterisk.
 *
 * @see org.asteriskjava.manager.action.SkypeLicenseListAction
 * @since 1.0.0
 */
public class SkypeLicenseListCompleteEvent extends ResponseEvent
{
    /**
     * Serial version identifier.
     */
    private static final long serialVersionUID = 1L;

    public SkypeLicenseListCompleteEvent(Object source)
    {
        super(source);
    }
}
