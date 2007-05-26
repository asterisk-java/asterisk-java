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

/**
 * The PingAction will ellicit a 'Pong' response, it is used to keep the manager
 * connection open and performs no operation.
 * 
 * @author srt
 * @version $Id$
 */
public class PingAction extends AbstractManagerAction
{
    /**
     * Serializable version identifier
     */
    private static final long serialVersionUID = -2930397629192323391L;

    /**
     * Creates a new PingAction.
     */
    public PingAction()
    {
        super();
    }

    /**
     * Returns the name of this action, i.e. "Ping".
     */
    @Override
   public String getAction()
    {
        return "Ping";
    }
}
