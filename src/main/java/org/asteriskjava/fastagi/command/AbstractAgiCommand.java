/*
 * Copyright 2004-2022 Asterisk-Java contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.asteriskjava.fastagi.command;

import org.asteriskjava.AsteriskVersion;
import org.asteriskjava.util.Log;

import java.io.Serializable;

import static java.lang.String.format;
import static java.lang.System.identityHashCode;
import static org.asteriskjava.AsteriskVersion.ASTERISK_1_4;
import static org.asteriskjava.util.LogFactory.getLog;

/**
 * Abstract base class that provides some convenience methods for implementing {@link AgiCommand} classes.
 *
 * @author srt
 */
public abstract class AbstractAgiCommand implements Serializable, AgiCommand {
    private static final long serialVersionUID = 3257849874518456633L;

    private static final Log logger = getLog(AbstractAgiCommand.class);

    private AsteriskVersion asteriskVersion;

    AsteriskVersion getAsteriskVersion() {
        if (asteriskVersion == null) {
            logger.warn("Asterisk Version isn't known, returning 1.4");
            return ASTERISK_1_4;
        }
        return asteriskVersion;
    }

    public void setAsteriskVersion(AsteriskVersion asteriskVersion) {
        this.asteriskVersion = asteriskVersion;
    }

    public abstract String buildCommand();

    @Override
    public String toString() {
        return format("%s[command='%s', systemHashcode=%d]", getClass().getName(), buildCommand(), identityHashCode(this));
    }

    /**
     * Escapes and quotes a given string according to the rules set by Asterisk's AGI.
     *
     * @param string the string to escape and quote
     * @return the escaped and quoted string
     */
    protected String escapeAndQuote(String string) {
        if (string == null) {
            return "\"\"";
        }

        string = string.replaceAll("\\\\", "\\\\\\\\");
        string = string.replaceAll("\\\"", "\\\\\"");
        string = string.replaceAll("\\\n", "");
        return format("\"%s\"", string);
    }

    /**
     * Escapes and quotes a given strings according to the rules set by Asterisk's AGI.
     *
     * @param strings the strings to escape and quote
     * @return the escaped and quoted joined string
     */
    protected String escapeAndQuote(String[] strings) {
        if (strings == null) {
            return escapeAndQuote((String) null);
        }

        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < strings.length; i++) {
            if (i > 0) {
                sb.append(",");
            }
            sb.append(strings[i]);
        }

        return escapeAndQuote(sb.toString());
    }
}
