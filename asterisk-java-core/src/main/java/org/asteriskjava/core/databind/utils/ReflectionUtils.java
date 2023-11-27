/*
 * Copyright 2004-2023 Asterisk Java contributors
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
package org.asteriskjava.core.databind.utils;

import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

import static java.lang.reflect.Modifier.*;

/**
 * Convenient class to deal with getters from mapped classes.
 *
 * @author Piotr Olaszewski
 * @since 4.0.0
 */
public final class ReflectionUtils {
    /**
     * Returns a {@link Map} of getter methods of the given class.
     * <p>
     * The key of the map contains the name of the attribute that can be accessed by the getter, the value being the
     * getter itself (an instance of Method). A method is considered a getter if its name starts with 'get' or 'is'.
     * It is declared public and takes no arguments.
     *
     * @param clazz      the class to return the getters for
     * @param comparator the comparator for sorting properties
     * @return a Map of attributes and their accessor methods (getters)
     * @see #getGetters(Class)
     */
    public static Map<String, Method> getGetters(Class<?> clazz, Comparator<String> comparator) {
        Map<String, Method> accessors = comparator != null ? new TreeMap<>(comparator) : new LinkedHashMap<>();

        Method[] methods = clazz.getMethods();
        for (Method method : methods) {
            if (method.getParameterCount() > 0 ||
                method.getReturnType() == Void.TYPE ||
                !isPublic(method.getModifiers()) ||
                isNative(method.getModifiers()) ||
                isAbstract(method.getModifiers()) ||
                isStatic(method.getModifiers()) ||
                method.getName().equals("toString")
            ) {
                continue;
            }

            String name = null;
            String methodName = method.getName();

            if (methodName.startsWith("get")) {
                name = methodName.substring(3);
            } else if (methodName.startsWith("is")) {
                name = methodName.substring(2);
            }

            if (name == null || name.isEmpty()) {
                continue;
            }

            accessors.put(name, method);
        }

        return accessors;
    }

    /**
     * Returns a Map of getter methods of the given class.
     *
     * @param clazz the class to return the getters for
     * @return a Map of attributes and their accessor methods (getters)
     * @see #getGetters(Class, Comparator)
     */
    public static Map<String, Method> getGetters(Class<?> clazz) {
        return getGetters(clazz, null);
    }
}