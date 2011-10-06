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

import org.asteriskjava.manager.event.DahdiShowChannelsCompleteEvent;
import org.asteriskjava.manager.event.ResponseEvent;

/**
 * The DahdiShowChannelsAction requests the state of all Dahdi channels.<p>
 * For each Dahdi channel aDahdiShowChannelsEvent is generated. After all Dahdi
 * channels have been listed a DahdiShowChannelsCompleteEvent is generated.
 * 
 * @see org.asteriskjava.manager.event.DahdiShowChannelsEvent
 * @see org.asteriskjava.manager.event.DahdiShowChannelsCompleteEvent
 * @author srt
 * @version $Id$
 */
public class DahdiShowChannelsAction extends AbstractManagerAction
        implements
            EventGeneratingAction
{
    /**
     * Serializable version identifier
     */
    private static final long serialVersionUID = 8697000330085766825L;

    /**
     * Creates a new DahdiShowChannelsAction.
     */
    public DahdiShowChannelsAction()
    {

    }

    /**
     * Returns the name of this action, i.e. "DahdiShowChannels".
     */
    @Override
   public String getAction()
    {
        return "DahdiShowChannels";
    }

    public Class<? extends ResponseEvent> getActionCompleteEventClass()
    {
        return DahdiShowChannelsCompleteEvent.class;
    }
}
