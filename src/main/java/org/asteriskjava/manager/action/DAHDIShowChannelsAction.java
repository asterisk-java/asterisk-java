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

import org.asteriskjava.manager.event.DAHDIShowChannelsCompleteEvent;
import org.asteriskjava.manager.event.ResponseEvent;

/**
 * The DAHDIShowChannelsAction requests the state of all DAHDI channels.<p>
 * For each DAHDI channel aDAHDIShowChannelsEvent is generated. After all DAHDI
 * channels have been listed a DAHDIShowChannelsCompleteEvent is generated.
 * 
 * @see org.asteriskjava.manager.event.DAHDIShowChannelsEvent
 * @see org.asteriskjava.manager.event.DAHDIShowChannelsCompleteEvent
 * @author srt
 * @version $Id$
 */
public class DAHDIShowChannelsAction extends AbstractManagerAction
        implements
            EventGeneratingAction
{
    /**
     * Serializable version identifier
     */
    private static final long serialVersionUID = 8697000330085766825L;

    /**
     * Creates a new DAHDIShowChannelsAction.
     */
    public DAHDIShowChannelsAction()
    {

    }

    /**
     * Returns the name of this action, i.e. "DAHDIShowChannels".
     */
    @Override
   public String getAction()
    {
        return "DAHDIShowChannels";
    }

    public Class<? extends ResponseEvent> getActionCompleteEventClass()
    {
        return DAHDIShowChannelsCompleteEvent.class;
    }
}
