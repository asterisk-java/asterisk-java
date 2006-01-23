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
package org.asteriskjava.fastagi.reply.impl;

import java.io.*;
import java.util.*;
import java.util.regex.*;

import org.asteriskjava.fastagi.reply.AGIReply;


/**
 * Default implementation of the AGIReply interface.
 * 
 * @author srt
 * @version $Id: AGIReplyImpl.java,v 1.2 2005/05/03 21:07:52 srt Exp $
 */
public class AGIReplyImpl implements Serializable, AGIReply
{
    private static final Pattern STATUS_PATTERN = Pattern
            .compile("^(\\d{3})[ -]");
    private static final Pattern RESULT_PATTERN = Pattern
            .compile("^200 result= *(\\S+)");
    private static final Pattern PARENTHESIS_PATTERN = Pattern
            .compile("^200 result=\\S* +\\((.*)\\)");
    private static final Pattern ADDITIONAL_ATTRIBUTES_PATTERN = Pattern
            .compile("^200 result=\\S* +(\\(.*\\) )?(.+)$");
    private static final Pattern ADDITIONAL_ATTRIBUTE_PATTERN = Pattern
            .compile("(\\S+)=(\\S+)");
    private static final Pattern SYNOPSIS_PATTERN = Pattern
            .compile("^\\s*Usage:\\s*(.*)\\s*$");
    private static final String END_OF_PROPER_USAGE = "520 End of proper usage.";

    private Matcher matcher;

    /**
     * Serial version identifier.
     */
    private static final long serialVersionUID = 3256727294671337012L;

    private List lines;
    private String firstLine;

    /**
     * The result, that is the part directly following the "result=" string.
     */
    private String result;

    /**
     * The status code.
     */
    private int status;

    /**
     * Additional attributes contained in this reply, for example endpos.
     */
    private Map<String, String> attributes;

    /**
     * The contents of the parenthesis.
     */
    private String extra;

    /**
     * In case of status == 520 (invalid command syntax) this attribute contains
     * the synopsis of the command.
     */
    private String synopsis;

    /**
     * In case of status == 520 (invalid command syntax) this attribute contains
     * the usage of the command.
     */
    private String usage;

    public AGIReplyImpl()
    {

    }

    public AGIReplyImpl(List lines)
    {
        this.lines = lines;
        try
        {
            firstLine = (String) lines.get(0);
        }
        catch (Exception e)
        {
        }
    }

    public String getFirstLine()
    {
        return firstLine;
    }

    public List getLines()
    {
        return lines;
    }

    /**
     * Returns the return code (the result as int).
     * 
     * @return the return code or -1 if the result is not an int.
     */
    public int getResultCode()
    {
        String result;

        result = getResult();
        if (result == null)
        {
            return -1;
        }

        try
        {
            return Integer.parseInt(result);
        }
        catch (NumberFormatException e)
        {
            return -1;
        }
    }

    /**
     * Returns the return code as character.
     * 
     * @return the return code as character.
     */
    public char getResultCodeAsChar()
    {
        int resultCode;

        resultCode = getResultCode();
        if (resultCode < 0)
        {
            return 0x0;
        }

        return (char) resultCode;
    }

    private boolean resultCreated;

    /**
     * Returns the result, that is the part directly following the "result="
     * string.
     * 
     * @return the result.
     */
    public String getResult()
    {
        if (resultCreated)
        {
            return result;
        }

        matcher = RESULT_PATTERN.matcher(firstLine);
        if (matcher.find())
        {
            result = matcher.group(1);
        }
        resultCreated = true;
        return result;
    }

    private boolean statusCreated;

    /**
     * Returns the status code.<br>
     * Supported status codes are:
     * <ul>
     * <li>200 Success
     * <li>510 Invalid or unknown command
     * <li>520 Invalid command syntax
     * </ul>
     * 
     * @return the status code.
     */
    public int getStatus()
    {
        if (statusCreated)
        {
            return status;
        }

        matcher = STATUS_PATTERN.matcher(firstLine);
        if (matcher.find())
        {
            status = Integer.parseInt(matcher.group(1));
        }
        statusCreated = true;
        return status;
    }

    private boolean attributesCreated;

    /**
     * Returns an additional attribute contained in the reply.<br>
     * For example the reply to the StreamFileCommand contains an additional
     * endpos attribute indicating the frame where the playback was stopped.
     * This can be retrieved by calling getAttribute("endpos") on the
     * corresponding reply.
     * 
     * @param name the name of the attribute to retrieve. The name is case
     *            insensitive.
     * @return the value of the attribute or <code>null</code> if it is not
     *         set.
     */
    public String getAttribute(String name)
    {
        if (getStatus() != SC_SUCCESS)
        {
            return null;
        }

        if ("result".equalsIgnoreCase(name))
        {
            return getResult();
        }

        if (!attributesCreated)
        {
            matcher = ADDITIONAL_ATTRIBUTES_PATTERN.matcher(firstLine);
            if (matcher.find())
            {
                String s;
                Matcher attributeMatcher;

                attributes = new HashMap<String, String>();
                s = matcher.group(2);
                attributeMatcher = ADDITIONAL_ATTRIBUTE_PATTERN.matcher(s);
                while (attributeMatcher.find())
                {
                    String key;
                    String value;

                    key = attributeMatcher.group(1);
                    value = attributeMatcher.group(2);
                    attributes.put(key.toLowerCase(), value);
                }
            }
            attributesCreated = true;
        }

        if (attributes == null || attributes.isEmpty())
        {
            return null;
        }

        return (String) attributes.get(name.toLowerCase());
    }

    private boolean extraCreated;

    /**
     * Returns the text in parenthesis contained in this reply.<br>
     * The meaning of this property depends on the command sent. Sometimes it
     * contains a flag like "timeout" or "hangup" or - in case of the
     * GetVariableCommand - the value of the variable.
     * 
     * @return the text in the parenthesis or <code>null</code> if not set.
     */
    public String getExtra()
    {
        if (getStatus() != SC_SUCCESS)
        {
            return null;
        }

        if (extraCreated)
        {
            return extra;
        }

        matcher = PARENTHESIS_PATTERN.matcher(firstLine);
        if (matcher.find())
        {
            extra = matcher.group(1);
        }
        extraCreated = true;
        return extra;
    }

    private boolean synopsisCreated;

    /**
     * Returns the synopsis of the command sent if Asterisk expected a different
     * syntax (getStatus() == SC_INVALID_COMMAND_SYNTAX).
     * 
     * @return the synopsis of the command sent, <code>null</code> if there
     *         were no syntax errors.
     */
    public String getSynopsis()
    {
        if (getStatus() != SC_INVALID_COMMAND_SYNTAX)
        {
            return null;
        }

        if (!synopsisCreated)
        {
            StringBuffer usageSB;

            if (lines.size() > 1)
            {
                String secondLine;
                Matcher synopsisMatcher;

                secondLine = (String) lines.get(1);
                synopsisMatcher = SYNOPSIS_PATTERN.matcher(secondLine);
                if (synopsisMatcher.find())
                {
                    synopsis = synopsisMatcher.group(1);
                }
            }
            synopsisCreated = true;

            usageSB = new StringBuffer();
            for (int i = 2; i < lines.size(); i++)
            {
                String line;

                line = (String) lines.get(i);
                if (END_OF_PROPER_USAGE.equals(line))
                {
                    break;
                }

                usageSB.append(line.trim());
                usageSB.append(" ");
            }
            usage = usageSB.toString().trim();
        }
        return synopsis;
    }

    /**
     * Returns the usage of the command sent if Asterisk expected a different
     * syntax (getStatus() == SC_INVALID_COMMAND_SYNTAX).
     * 
     * @return the usage of the command sent, <code>null</code> if there were
     *         no syntax errors.
     */
    public String getUsage()
    {
        return usage;
    }

    public String toString()
    {
        StringBuffer sb;

        sb = new StringBuffer(getClass().getName() + ": ");
        sb.append("status='" + getStatus() + "'; ");
        if (status == SC_SUCCESS)
        {
            sb.append("result='" + getResult() + "'; ");
            sb.append("extra='" + getExtra() + "'; ");
            sb.append("attributes=" + attributes + "; ");
        }
        if (status == SC_INVALID_COMMAND_SYNTAX)
        {
            sb.append("synopsis='" + getSynopsis() + "'; ");
        }
        sb.append("systemHashcode=" + System.identityHashCode(this));

        return sb.toString();
    }
}
