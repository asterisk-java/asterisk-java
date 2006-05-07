/*
 *  Copyright 2005-2006 Stefan Reuter
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
package org.asteriskjava.util;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Utility class that provides helper methods for reflection that is used by the
 * fastagi and manager packages to access getter and setter methods.<br>
 * Client code is not supposed to use this class. 
 * 
 * @author srt
 */
public class ReflectionUtil
{
    // hide constructor
    private ReflectionUtil()
    {
        
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
    public static Map<String, Method> getGetters(final Class clazz)
    {
        Map<String, Method> accessors = new HashMap<String, Method>();
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

    public static Map<String, Method> getSetters(Class clazz)
    {
        Map<String, Method> accessors = new HashMap<String, Method>();
        Method[] methods = clazz.getMethods();

        for (int i = 0; i < methods.length; i++)
        {
            String name;
            String methodName;
            Method method = methods[i];

            methodName = method.getName();
            if (!methodName.startsWith("set"))
            {
                continue;
            }

            // skip methods with != 1 parameters
            if (method.getParameterTypes().length != 1)
            {
                continue;
            }

            // ok seems to be an accessor
            name = methodName.substring("set".length()).toLowerCase();
            accessors.put(name, method);
        }

        return accessors;
    }
}
