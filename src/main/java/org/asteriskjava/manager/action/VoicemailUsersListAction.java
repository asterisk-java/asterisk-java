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

import org.asteriskjava.manager.event.VoicemailUserEntryCompleteEvent;

/**
 * Retrieves a list of all defined voicemail users.<p>
 * For each user that is found a VoicemailUserEntryEvent event is sent by Asterisk containing
 * the details. When all peers have been reported a VoicemailUserEntryCompleteEvent is
 * sent.<p>
 * If no voicemail users are defined you only receive a success response with message
 * "There are no voicemail users currently defined." and no VoicemailUserEntryCompleteEvent.
 * As Asterisk-Java waits for the VoicemailUserEntryCompleteEvent you will run into a timeout
 * in this case. This issue has been reported to Digium at http://bugs.digium.com/view.php?id=11874.<p>
 * It is implemented in <code>apps/app_voicemail.c</code>
 * <p/>
 * Available since Asterisk 1.6
 *
 * @author srt
 * @version $Id$
 * @todo Asterisk bug: update if http://bugs.digium.com/view.php?id=11874 is resolved
 * @see org.asteriskjava.manager.event.VoicemailUserEntryEvent
 * @see org.asteriskjava.manager.event.VoicemailUserEntryCompleteEvent
 * @since 1.0.0
 */
public class VoicemailUsersListAction extends AbstractManagerAction implements EventGeneratingAction
{
    /**
     * Serial version identifier.
     */
    private static final long serialVersionUID = 0L;

    /**
     * Creates a new VoicemailUsersListAction.
     */
    public VoicemailUsersListAction()
    {

    }

    @Override
    public String getAction()
    {
        return "VoicemailUsersList";
    }

    public Class getActionCompleteEventClass()
    {
        return VoicemailUserEntryCompleteEvent.class;
    }
}