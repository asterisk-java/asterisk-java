package org.asteriskjava.core.utils;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ReflectionUtils {
    /**
     * Returns a Map of getter methods of the given class.
     * <p>
     * The key of the map contains the name of the attribute that can be
     * accessed by the getter, the value the getter itself (an instance of
     * java.lang.reflect.Method). A method is considered a getter if its name
     * starts with "get", it is declared public and takes no arguments.
     *
     * @param clazz the class to return the getters for
     * @return a Map of attributes and their accessor methods (getters)
     */
    public static Map<String, Method> getGetters(final Class<?> clazz) {
        final Map<String, Method> accessors = new HashMap<>();
        final Method[] methods = clazz.getMethods();

        for (Method method : methods) {
            if (method.getParameterCount() > 0 ||
                method.getReturnType() == Void.TYPE ||
                !Modifier.isPublic(method.getModifiers()) ||
                Modifier.isNative(method.getModifiers()) ||
                Modifier.isAbstract(method.getModifiers()) ||
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

            if (name == null || name.length() == 0) {
                continue;
            }

            // skip methods with != 0 parameters
            if (method.getParameterTypes().length != 0) {
                continue;
            }

            accessors.put(name.toLowerCase(Locale.ENGLISH), method);
        }

        return accessors;
    }
}
