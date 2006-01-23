/*
 * Copyright  2004-2005 Stefan Reuter
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
package net.sf.asterisk.manager.action;

import net.sf.asterisk.manager.event.StatusCompleteEvent;

/**
 * The StatusAction requests the state of all active channels.<br>
 * For each active channel a StatusEvent is generated. After the state of all
 * channels has been reported a StatusCompleteEvent is generated.
 * 
 * @see net.sf.asterisk.manager.event.StatusEvent
 * @see net.sf.asterisk.manager.event.StatusCompleteEvent
 * @author srt
 * @version $Id: StatusAction.java,v 1.5 2005/08/07 16:55:18 srt Exp $
 */
public class StatusAction extends AbstractManagerAction
        implements
            EventGeneratingAction
{
    /**
     * Serializable version identifier
     */
    static final long serialVersionUID = -320228893513973367L;

    /**
     * Creates a new StatusAction.
     */
    public StatusAction()
    {

    }

    /**
     * Returns the name of this action, i.e. "Status".
     */
    public String getAction()
    {
        return "Status";
    }

    public Class getActionCompleteEventClass()
    {
        return StatusCompleteEvent.class;
    }
}
