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
 * An OriginateSuccessEvent is triggered when the execution of an
 * OriginateAction succeeded.<p>
 * Deprecated since Asterisk 1.4.
 *
 * @author srt
 * @version $Id$
 * @see org.asteriskjava.manager.action.OriginateAction
 * @see OriginateResponseEvent
 * @deprecated
 */
@Deprecated
public class OriginateSuccessEvent extends OriginateResponseEvent {
    /**
     * Serializable version identifier
     */
    private static final long serialVersionUID = -5086668438614692086L;

    /**
     * @param source
     */
    public OriginateSuccessEvent(Object source) {
        super(source);
        setResponse("Success");
    }
}
