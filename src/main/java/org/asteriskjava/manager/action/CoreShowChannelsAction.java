/*
 *  Copyright 2009 Sebastian.
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */

package org.asteriskjava.manager.action;

import org.asteriskjava.manager.event.CoreShowChannelsCompleteEvent;
import org.asteriskjava.manager.event.ResponseEvent;

/**
 * The CoreShowChannelsAction requests the state of all active channels.<p>
 * It is simmilar to StatusAction but with more information about that channel
 * For each active channel a CoreShowChannelEvent is generated. After the state of all
 * channels has been reported a CoreShowChannelsCompleteEvent is generated.<p>
 * This action only works properly in 1.6.2 later than beta3.
 *
 * @author sebastian gutierrez
 * @see org.asteriskjava.manager.event.CoreShowChannelEvent
 * @see org.asteriskjava.manager.event.CoreShowChannelsCompleteEvent
 * @since 1.0.0
 */
public class CoreShowChannelsAction extends AbstractManagerAction implements EventGeneratingAction
{

    /**
     * Serializable version identifier.
     */
    static final long serialVersionUID = 0L;

    public CoreShowChannelsAction()
    {

    }

    @Override
    public String getAction()
    {
        return "CoreShowChannels";
    }

    public Class<? extends ResponseEvent> getActionCompleteEventClass()
    {
        return CoreShowChannelsCompleteEvent.class;
    }


}
