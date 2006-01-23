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
package org.asteriskjava.fastagi.impl;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.asteriskjava.fastagi.AGIRequest;
import org.asteriskjava.util.Log;
import org.asteriskjava.util.LogFactory;


/**
 * Default implementation of the AGIRequest interface.
 * 
 * @author srt
 * @version $Id: AGIRequestImpl.java,v 1.11 2005/11/27 16:08:17 srt Exp $
 */
public class AGIRequestImpl implements Serializable, AGIRequest
{
    private final Log logger = LogFactory.getLog(getClass());
    private static final Pattern SCRIPT_PATTERN = Pattern
            .compile("^([^\\?]*)\\?(.*)$");
    private static final Pattern PARAMETER_PATTERN = Pattern
            .compile("^(.*)=(.*)$");

    private String rawCallerId;

    /**
     * Serial version identifier.
     */
    private static final long serialVersionUID = 3257001047145789496L;

    private Map request;

    /**
     * A map assigning the values of a parameter (an array of Strings) to the
     * name of the parameter.
     */
    private Map parameterMap;

    private String parameters;
    private String script;
    private boolean callerIdCreated;
    private InetAddress localAddress;
    private int localPort;
    private InetAddress remoteAddress;
    private int remotePort;

    /**
     * Creates a new AGIRequestImpl.
     * 
     * @param environment the first lines as received from Asterisk containing
     *            the environment.
     */
    public AGIRequestImpl(final Collection environment)
    {
        if (environment == null)
        {
            throw new IllegalArgumentException("Environment must not be null.");
        }
        request = buildMap(environment);
    }

    /**
     * Builds a map containing variable names as key (with the "agi_" prefix
     * stripped) and the corresponding values.<br>
     * Syntactically invalid and empty variables are skipped.
     * 
     * @param lines the environment to transform.
     * @return a map with the variables set corresponding to the given
     *         environment.
     */
    private Map buildMap(final Collection lines)
    {
        Map map;
        Iterator lineIterator;

        map = new HashMap();
        lineIterator = lines.iterator();

        while (lineIterator.hasNext())
        {
            String line;
            int colonPosition;
            String key;
            String value;

            line = (String) lineIterator.next();
            colonPosition = line.indexOf(':');

            // no colon on the line?
            if (colonPosition < 0)
            {
                continue;
            }

            // key doesn't start with agi_?
            if (!line.startsWith("agi_"))
            {
                continue;
            }

            // first colon in line is last character -> no value present?
            if (line.length() < colonPosition + 2)
            {
                continue;
            }

            key = line.substring(4, colonPosition).toLowerCase();
            value = line.substring(colonPosition + 2);

            if (value.length() != 0)
            {
                map.put(key, value);
            }
        }

        return map;
    }

    public Map getRequest()
    {
        return request;
    }

    /**
     * Returns the name of the script to execute.
     * 
     * @return the name of the script to execute.
     */
    public synchronized String getScript()
    {
        if (script != null)
        {
            return script;
        }

        script = (String) request.get("network_script");
        if (script != null)
        {
            Matcher scriptMatcher = SCRIPT_PATTERN.matcher(script);
            if (scriptMatcher.matches())
            {
                script = scriptMatcher.group(1);
                parameters = scriptMatcher.group(2);
            }
        }

        return script;
    }

    /**
     * Returns the full URL of the request in the form
     * agi://host[:port][/script].
     * 
     * @return the full URL of the request in the form
     *         agi://host[:port][/script].
     */
    public String getRequestURL()
    {
        return (String) request.get("request");
    }

    /**
     * Returns the name of the channel.
     * 
     * @return the name of the channel.
     */
    public String getChannel()
    {
        return (String) request.get("channel");
    }

    /**
     * Returns the unqiue id of the channel.
     * 
     * @return the unqiue id of the channel.
     */
    public String getUniqueId()
    {
        return (String) request.get("uniqueid");
    }

    public String getType()
    {
        return (String) request.get("type");
    }

    public String getLanguage()
    {
        return (String) request.get("language");
    }

    public String getCallerId()
    {
        String callerIdName;
        String callerId;

        callerIdName = (String) request.get("calleridname");
        callerId = (String) request.get("callerid");
        if (callerIdName != null)
        {
            // Asterisk 1.2
            if (callerId == null || "unknown".equals(callerId))
            {
                return null;
            }

            return callerId;
        }
        else
        {
            // Asterisk 1.0
            return getCallerId10();
        }
    }

    public String getCallerIdName()
    {
        String callerIdName;

        callerIdName = (String) request.get("calleridname");
        if (callerIdName != null)
        {
            // Asterisk 1.2
            if ("unknown".equals(callerIdName))
            {
                return null;
            }

            return callerIdName;
        }
        else
        {
            // Asterisk 1.0
            return getCallerIdName10();
        }
    }

    /**
     * Returns the Caller*ID using Asterisk 1.0 logic.
     * 
     * @return the Caller*ID
     */
    private String getCallerId10()
    {
        int lbPosition;
        int rbPosition;

        if (!callerIdCreated)
        {
            rawCallerId = (String) request.get("callerid");
            callerIdCreated = true;
        }

        if (rawCallerId == null)
        {
            return null;
        }

        lbPosition = rawCallerId.indexOf('<');
        rbPosition = rawCallerId.indexOf('>');

        if (lbPosition < 0 || rbPosition < 0)
        {
            return rawCallerId;
        }

        return rawCallerId.substring(lbPosition + 1, rbPosition);
    }

    /**
     * Returns the Caller*ID using Asterisk 1.0 logic.
     * 
     * @return the Caller*ID Name
     */
    private String getCallerIdName10()
    {
        int lbPosition;
        String callerIdName;

        if (!callerIdCreated)
        {
            rawCallerId = (String) request.get("callerid");
            callerIdCreated = true;
        }

        if (rawCallerId == null)
        {
            return null;
        }

        lbPosition = rawCallerId.indexOf('<');

        if (lbPosition < 0)
        {
            return null;
        }

        callerIdName = rawCallerId.substring(0, lbPosition).trim();
        if (callerIdName.startsWith("\"") && callerIdName.endsWith("\""))
        {
            callerIdName = callerIdName.substring(1, callerIdName.length() - 1);
        }

        if (callerIdName.length() == 0)
        {
            return null;
        }
        else
        {
            return callerIdName;
        }
    }

    public String getDnid()
    {
        String dnid;
        
        dnid = (String) request.get("dnid");
        
        if (dnid == null || "unknown".equals(dnid))
        {
            return null;
        }
        
        return dnid; 
    }

    public String getRdnis()
    {
        String rdnis;
        
        rdnis = (String) request.get("rdnis");
        
        if (rdnis == null || "unknown".equals(rdnis))
        {
            return null;
        }
        
        return rdnis; 
    }

    public String getContext()
    {
        return (String) request.get("context");
    }

    public String getExtension()
    {
        return (String) request.get("extension");
    }

    public Integer getPriority()
    {
        if (request.get("priority") != null)
        {
            return new Integer((String) request.get("priority"));
        }
        return null;
    }

    public Boolean getEnhanced()
    {
        if (request.get("enhanced") != null)
        {
            if ("1.0".equals((String) request.get("enhanced")))
            {
                return Boolean.TRUE;
            }
            else
            {
                return Boolean.FALSE;
            }
        }
        return null;
    }

    public String getAccountCode()
    {
        return (String) request.get("accountCode");
    }

    public Integer getCallingAni2()
    {
        if (request.get("callingani2") == null)
        {
            return null;
        }
        
        try
        {
            return Integer.valueOf((String) request.get("callingani2"));
        }
        catch (NumberFormatException e)
        {
            return null;
        }
    }

    public Integer getCallingPres()
    {
        if (request.get("callingpres") == null)
        {
            return null;
        }
        
        try
        {
            return Integer.valueOf((String) request.get("callingpres"));
        }
        catch (NumberFormatException e)
        {
            return null;
        }
    }

    public Integer getCallingTns()
    {
        if (request.get("callingtns") == null)
        {
            return null;
        }
        
        try
        {
            return Integer.valueOf((String) request.get("callingtns"));
        }
        catch (NumberFormatException e)
        {
            return null;
        }
    }

    public Integer getCallingTon()
    {
        if (request.get("callington") == null)
        {
            return null;
        }
        
        try
        {
            return Integer.valueOf((String) request.get("callington"));
        }
        catch (NumberFormatException e)
        {
            return null;
        }
    }

    public String getParameter(String name)
    {
        String[] values;

        values = getParameterValues(name);

        if (values == null || values.length == 0)
        {
            return null;
        }

        return values[0];
    }

    public String[] getParameterValues(String name)
    {
        if (getParameterMap().isEmpty())
        {
            return null;
        }

        return (String[]) parameterMap.get(name);
    }

    public synchronized Map getParameterMap()
    {
        if (parameterMap == null)
        {
            parameterMap = parseParameters(parameters);
        }
        return parameterMap;
    }

    /**
     * Parses the given parameter string and caches the result.
     * 
     * @param s the parameter string to parse
     * @return a Map made up of parameter names their values
     */
    private synchronized Map parseParameters(String s)
    {
        Map parameterMap;
        Map result;
        Iterator parameterIterator;
        StringTokenizer st;

        parameterMap = new HashMap();
        if (s == null)
        {
            return parameterMap;
        }

        st = new StringTokenizer(s, "&");
        while (st.hasMoreTokens())
        {
            String parameter;
            Matcher parameterMatcher;
            String name;
            String value;
            List values;

            parameter = st.nextToken();
            parameterMatcher = PARAMETER_PATTERN.matcher(parameter);
            if (parameterMatcher.matches())
            {
                try
                {
                    name = URLDecoder
                            .decode(parameterMatcher.group(1), "UTF-8");
                    value = URLDecoder.decode(parameterMatcher.group(2),
                            "UTF-8");
                }
                catch (UnsupportedEncodingException e)
                {
                    logger.error("Unable to decode parameter '" + parameter
                            + "'", e);
                    continue;
                }
            }
            else
            {
                try
                {
                    name = URLDecoder.decode(parameter, "UTF-8");
                    value = "";
                }
                catch (UnsupportedEncodingException e)
                {
                    logger.error("Unable to decode parameter '" + parameter
                            + "'", e);
                    continue;
                }
            }

            if (parameterMap.get(name) == null)
            {
                values = new ArrayList();
                values.add(value);
                parameterMap.put(name, values);
            }
            else
            {
                values = (List) parameterMap.get(name);
                values.add(value);
            }
        }

        result = new HashMap();
        parameterIterator = parameterMap.keySet().iterator();
        while (parameterIterator.hasNext())
        {
            String name;
            List values;
            String[] valueArray;

            name = (String) parameterIterator.next();
            values = (List) parameterMap.get(name);

            valueArray = new String[values.size()];
            result.put(name, values.toArray(valueArray));
        }

        return result;
    }

    public InetAddress getLocalAddress()
    {
        return localAddress;
    }

    public void setLocalAddress(InetAddress localAddress)
    {
        this.localAddress = localAddress;
    }

    public int getLocalPort()
    {
        return localPort;
    }

    public void setLocalPort(int localPort)
    {
        this.localPort = localPort;
    }

    public InetAddress getRemoteAddress()
    {
        return remoteAddress;
    }

    public void setRemoteAddress(InetAddress remoteAddress)
    {
        this.remoteAddress = remoteAddress;
    }

    public int getRemotePort()
    {
        return remotePort;
    }

    public void setRemotePort(int remotePort)
    {
        this.remotePort = remotePort;
    }

    public String toString()
    {
        StringBuffer sb;

        sb = new StringBuffer(getClass().getName() + ": ");
        sb.append("script='" + getScript() + "'; ");
        sb.append("requestURL='" + getRequestURL() + "'; ");
        sb.append("channel='" + getChannel() + "'; ");
        sb.append("uniqueId='" + getUniqueId() + "'; ");
        sb.append("type='" + getType() + "'; ");
        sb.append("language='" + getLanguage() + "'; ");
        sb.append("callerId='" + getCallerId() + "'; ");
        sb.append("callerIdName='" + getCallerIdName() + "'; ");
        sb.append("dnid='" + getDnid() + "'; ");
        sb.append("rdnis='" + getRdnis() + "'; ");
        sb.append("context='" + getContext() + "'; ");
        sb.append("extension='" + getExtension() + "'; ");
        sb.append("priority='" + getPriority() + "'; ");
        sb.append("enhanced='" + getEnhanced() + "'; ");
        sb.append("accountCode='" + getAccountCode() + "'; ");
        sb.append("systemHashcode=" + System.identityHashCode(this));

        return sb.toString();
    }
}
