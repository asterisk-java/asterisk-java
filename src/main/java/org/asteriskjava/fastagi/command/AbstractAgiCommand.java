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
package org.asteriskjava.fastagi.command;

import java.io.Serializable;

import org.asteriskjava.AsteriskVersion;
import org.asteriskjava.util.Log;
import org.asteriskjava.util.LogFactory;

/**
 * Abstract base class that provides some convenience methods for implementing
 * AgiCommand classes.
 * 
 * @author srt
 * @version $Id$
 */
public abstract class AbstractAgiCommand implements Serializable, AgiCommand
{
    /**
     * Serial version identifier.
     */
    private static final long serialVersionUID = 3257849874518456633L;
    private AsteriskVersion asteriskVersion;

    private static final Log logger = LogFactory.getLog(AbstractAgiCommand.class);

    public abstract String buildCommand();

    /**
     * Escapes and quotes a given String according to the rules set by
     * Asterisk's AGI.
     * 
     * @param s the String to escape and quote
     * @return the transformed String
     */
    protected String escapeAndQuote(String s)
    {
        String tmp;

        if (s == null)
        {
            return "\"\"";
        }

        tmp = s;
        tmp = tmp.replaceAll("\\\\", "\\\\\\\\");
        tmp = tmp.replaceAll("\\\"", "\\\\\"");
        tmp = tmp.replaceAll("\\\n", ""); // filter newline
        return "\"" + tmp + "\""; // add quotes
    }

    protected String escapeAndQuote(String[] options)
    {
        if (options == null)
        {
            return escapeAndQuote((String) null);
        }

        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < options.length; i++)
        {
            if (i > 0)
            {
                sb.append(",");
            }
            sb.append(options[i]);
        }

        return escapeAndQuote(sb.toString());
    }

    @Override
    public String toString()
    {
        StringBuilder sb;

        sb = new StringBuilder(getClass().getName()).append("[");
        sb.append("command='").append(buildCommand()).append("', ");
        sb.append("systemHashcode=").append(System.identityHashCode(this)).append("]");

        return sb.toString();
    }

    public void setAsteriskVersion(AsteriskVersion asteriskVersion)
    {
        this.asteriskVersion = asteriskVersion;
    }

    AsteriskVersion getAsteriskVersion()
    {
        if (asteriskVersion == null)
        {
            logger.warn("Asterisk Version isn't known, returning 1.4");
            return AsteriskVersion.ASTERISK_1_4;
        }
        return asteriskVersion;
    }
}
