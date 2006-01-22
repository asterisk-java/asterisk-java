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
package net.sf.asterisk.manager;

import java.util.EventListener;

import net.sf.asterisk.manager.event.ManagerEvent;

/**
 * An Interface to handle events received from an asterisk server.
 * 
 * @see net.sf.asterisk.manager.event.ManagerEvent
 * @author srt
 * @version $Id: ManagerEventHandler.java,v 1.3 2005/07/29 03:30:14 srt Exp $
 */
public interface ManagerEventHandler extends EventListener
{
    /**
     * This method is called when an event is received.
     * 
     * @param event the event received
     */
    void handleEvent(ManagerEvent event);
}
