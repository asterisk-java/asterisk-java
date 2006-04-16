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
package org.asteriskjava.manager.impl;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Map;

import org.asteriskjava.AsteriskVersion;
import org.asteriskjava.manager.ActionBuilder;
import org.asteriskjava.manager.action.ManagerAction;
import org.asteriskjava.util.Log;
import org.asteriskjava.util.LogFactory;
import org.asteriskjava.util.ReflectionUtil;



/**
 * Default implementation of the ActionBuilder interface.
 * 
 * @author srt
 * @version $Id$
 */
public class ActionBuilderImpl implements ActionBuilder
{
    private static final String LINE_SEPARATOR = "\r\n";

    /**
     * Instance logger.
     */
    private final Log logger = LogFactory.getLog(getClass());
    private AsteriskVersion targetVersion;

    /**
     * Creates a new ActionBuilder for Asterisk 1.0.
     */
    public ActionBuilderImpl()
    {
        this.targetVersion = AsteriskVersion.ASTERISK_1_0;
    }

    public void setTargetVersion(AsteriskVersion targetVersion)
    {
        this.targetVersion = targetVersion;
    }

    public String buildAction(final ManagerAction action)
    {
        return buildAction(action, null);
    }

    @SuppressWarnings("unchecked")
    public String buildAction(final ManagerAction action, final String internalActionId)
    {
        StringBuffer sb;
        Map<String, Method> getters;

        sb = new StringBuffer("action: ");
        sb.append(action.getAction());
        sb.append(LINE_SEPARATOR);
        if (internalActionId != null)
        {
            sb.append("actionid: ");
            sb.append(Util.addInternalActionId(action.getActionId(), internalActionId));
            sb.append(LINE_SEPARATOR);
        }
        else if (action.getActionId() != null)
        {
            sb.append("actionid: ");
            sb.append(action.getActionId());
            sb.append(LINE_SEPARATOR);
        }
        
        getters = ReflectionUtil.getGetters(action.getClass());
        for (String name : getters.keySet())
        {
            Method getter;
            Object value;

            if ("class".equals(name) || "action".equals(name) || "actionid".equals(name))
            {
                continue;
            }

            getter = getters.get(name);
            try
            {
                value = getter.invoke(action, new Object[]{});
            }
            catch (Exception ex)
            {
                logger.error("Unable to retrieve property '" + name + "' of "
                        + action.getClass(), ex);
                continue;
            }

            if (value == null)
            {
                continue;
            }
            else if (value instanceof Class)
            {
                continue;
            }
            else if (value instanceof Map)
            {
                appendMap(sb, name, (Map) value);
            }
            else if (value instanceof String)
            {
                appendString(sb, name, (String) value);
            }
            else
            {
                appendString(sb, name, value.toString());
            }
        }

        sb.append(LINE_SEPARATOR);
        return sb.toString();
    }

    protected void appendMap(StringBuffer sb, String key, Map<String, String> values)
    {
        String singularKey;

        // strip plural s (i.e. use "variable: " instead of "variables: "
        if (key.endsWith("s"))
        {
            singularKey = key.substring(0, key.length() - 1);
        }
        else
        {
            singularKey = key;
        }

        if (targetVersion.isAtLeast(AsteriskVersion.ASTERISK_1_2))
        {
            appendMap12(sb, singularKey, values);
        }
        else
        {
            appendMap10(sb, singularKey, values);
        }
    }

    protected void appendMap10(StringBuffer sb, String singularKey, Map<String, String> values)
    {
        Iterator<Map.Entry<String, String>> entryIterator;

        sb.append(singularKey);
        sb.append(": ");
        entryIterator = values.entrySet().iterator();
        while (entryIterator.hasNext())
        {
            Map.Entry entry;

            entry = entryIterator.next();
            sb.append(entry.getKey());
            sb.append("=");
            if (entry.getValue() != null)
            {
                sb.append(entry.getValue());
            }

            if (entryIterator.hasNext())
            {
                sb.append("|");
            }
        }
        sb.append(LINE_SEPARATOR);
    }

    protected void appendMap12(StringBuffer sb, String singularKey, Map<String, String> values)
    {
        for (Map.Entry entry : values.entrySet())
        {
            sb.append(singularKey);
            sb.append(": ");
            sb.append(entry.getKey());
            sb.append("=");
            if (entry.getValue() != null)
            {
                sb.append(entry.getValue());
            }

            sb.append(LINE_SEPARATOR);
        }
    }

    protected void appendString(StringBuffer sb, String key, String value)
    {
        sb.append(key);
        sb.append(": ");
        sb.append(value);
        sb.append(LINE_SEPARATOR);
    }
}
