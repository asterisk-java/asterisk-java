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
package net.sf.asterisk.manager.impl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.sf.asterisk.AsteriskVersion;
import net.sf.asterisk.manager.ActionBuilder;
import net.sf.asterisk.manager.action.ManagerAction;

import net.sf.asterisk.util.Log;
import net.sf.asterisk.util.LogFactory;

/**
 * Default implementation of the ActionBuilder interface.
 * 
 * @author srt
 * @version $Id: ActionBuilderImpl.java,v 1.5 2005/11/08 15:25:18 srt Exp $
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

    public String buildAction(final ManagerAction action, final String internalActionId)
    {
        StringBuffer sb;
        Map getters;

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
        
        getters = getGetters(action.getClass());

        Iterator i = getters.keySet().iterator();
        while (i.hasNext())
        {
            String name;
            Method getter;
            Object value;

            name = (String) i.next();

            if ("class".equals(name) || "action".equals(name) || "actionid".equals(name))
            {
                continue;
            }

            getter = (Method) getters.get(name);
            try
            {
                value = getter.invoke(action, new Object[]{});
            }
            catch (IllegalAccessException ex)
            {
                logger.error("Unable to retrieve property '" + name + "' of "
                        + action.getClass(), ex);
                continue;
            }
            catch (InvocationTargetException ex)
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

    /**
     * Returns a Map of getter methods of the given class.<br>
     * The key of the map contains the name of the attribute that can be
     * accessed by the getter, the value the getter itself (an instance of
     * java.lang.reflect.Method). A method is considered a getter if its name
     * starts with "get", it is declared public and takes no arguments.
     * 
     * @param clazz the class to return the getters for
     * @return a Map of attributes and their accessor methods (getters)
     */
    private Map getGetters(final Class clazz)
    {
        Map accessors = new HashMap();
        Method[] methods = clazz.getMethods();

        for (int i = 0; i < methods.length; i++)
        {
            String name;
            String methodName;
            Method method = methods[i];

            methodName = method.getName();
            if (!methodName.startsWith("get"))
            {
                continue;
            }

            // skip methods with != 0 parameters
            if (method.getParameterTypes().length != 0)
            {
                continue;
            }

            // ok seems to be an accessor
            name = methodName.substring("get".length()).toLowerCase();

            if (name.length() == 0)
            {
                continue;
            }

            accessors.put(name, method);
        }

        return accessors;
    }

    protected void appendMap(StringBuffer sb, String key, Map values)
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

    protected void appendMap10(StringBuffer sb, String singularKey, Map values)
    {
        Iterator entryIterator;

        sb.append(singularKey);
        sb.append(": ");
        entryIterator = values.entrySet().iterator();
        while (entryIterator.hasNext())
        {
            Map.Entry entry;

            entry = (Map.Entry) entryIterator.next();
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

    protected void appendMap12(StringBuffer sb, String singularKey, Map values)
    {
        Iterator entryIterator;

        entryIterator = values.entrySet().iterator();
        while (entryIterator.hasNext())
        {
            Map.Entry entry;

            entry = (Map.Entry) entryIterator.next();

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
