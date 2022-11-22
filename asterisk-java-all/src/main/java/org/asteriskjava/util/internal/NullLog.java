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
package org.asteriskjava.util.internal;

import org.asteriskjava.util.Log;

/**
 * A Log implementation that does nothing.
 * <p>
 * This logger is only used if neither log4j nor java.util.logging are available
 * which should not happen anyway as Asterisk-Java depends on at least JDK 1.5.
 *
 * @author srt
 * @version $Id$
 */
public class NullLog implements Log {
    /**
     * Creates a new NullLog.
     */
    public NullLog() {
    }

    public void debug(Object obj) {
    }

    public void info(Object obj) {
    }

    public void warn(Object obj) {
    }

    public void warn(Object obj, Throwable ex) {
    }

    public void error(Object obj) {
    }

    public void error(Object obj, Throwable ex) {
    }

    @Override
    public boolean isDebugEnabled() {
        return false;
    }

    @Override
    public void debug(Object e, Throwable e2) {

    }
}
