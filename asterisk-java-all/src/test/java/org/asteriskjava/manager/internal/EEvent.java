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
package org.asteriskjava.manager.internal;

import org.asteriskjava.manager.event.ManagerEvent;

public class EEvent
        extends ManagerEvent {

    private static final long serialVersionUID = 3545240219457894199L;

    private Boolean line;

    public EEvent(Object source) {
        super(source);
    }

    public Boolean getLineAsBoolean() {
        return line;
    }

    public void setLine(Boolean line) {
        this.line = line;
        if (line == null) {
            super.setLine(null);
        } else if (line) {
            super.setLine(1);
        } else {
            super.setLine(0);
        }
    }
}
