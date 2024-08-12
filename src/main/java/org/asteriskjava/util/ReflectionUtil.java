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

import org.reflections.Reflections;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

/**
 * Utility class that provides helper methods for reflection that is used by the
 * fastagi and manager packages to access getter and setter methods.
 * <p>
 * Client code is not supposed to use this class.
 *
 * @author srt
 */
public class ReflectionUtil {
    private ReflectionUtil() {
        // hide constructor
    }

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

    private final static ConcurrentHashMap<Class<?>, Map<String, Method>> setterMap = new ConcurrentHashMap<>();

    /**
     * The main benefit here is that there will not be repeated errors when
     * inspecting classes for setters on every single Event being processed.
     * <br>
     * <br>
     * While this method adds caching which is 100 times faster, the time
     * Benefit is largely insignificant as the execution time was already very
     * fast.
     *
     * @param clazz
     * @return
     */
    public static Map<String, Method> getSetters(Class<?> clazz) {
        return setterMap.computeIfAbsent(clazz, (c) -> {
            return getSettersInternal(c);
        });
    }

    /**
     * Returns a Map of setter methods of the given class.
     * <p>
     * The key of the map contains the name of the attribute that can be
     * accessed by the setter, the value the setter itself (an instance of
     * java.lang.reflect.Method). A method is considered a setter if its name
     * starts with "set", it is declared public and takes exactly one argument.
     *
     * @param clazz the class to return the setters for
     * @return a Map of attributes and their accessor methods (setters)
     */
    private static Map<String, Method> getSettersInternal(Class<?> clazz) {
        final Map<String, Method> accessors = new HashMap<>();
        final Method[] methods = clazz.getMethods();

        for (Method method : methods) {
            String name;
            String methodName = method.getName();
            if (!methodName.startsWith("set")) {
                continue;
            }

            // skip methods with != 1 parameters
            if (method.getParameterTypes().length != 1) {
                continue;
            }

            // OK seems to be an setter
            name = methodName.substring("set".length()).toLowerCase(Locale.US);

            // if an event class implements a setter that exists in
            // ManagerEvent, then prefer the setter from the extending class
            Method existing = accessors.get(name);
            if (existing != null) {
                logger.warn("multiple setters (case insensitive) exist for " + name + " on class(es) "
                        + existing.getDeclaringClass() + " and " + method.getDeclaringClass());
            }
            if (existing == null) {
                // we don't have a mapping for this setter so add it.
                accessors.put(name, method);
            } else if (!method.getDeclaringClass().isAssignableFrom(existing.getDeclaringClass())) {
                // we already have a mapping for this setter, but this one is
                // from an extending class so replace it.
                logger.warn("Preferring setter from extending class " + existing + " -> " + method);
                accessors.put(name, method);
            } else {
                // there is already a mapping for this setter from class
                logger.warn("Preferring setter from extending class " + method + " -> " + existing);
            }
        }
        return accessors;
    }

    /**
     * Strips all illegal charaters from the given lower case string. Illegal
     * characters are all characters that are neither characters ('a' to 'z')
     * nor digits ('0' to '9').
     *
     * @param s the original string
     * @return the string with all illegal characters stripped
     */
    public static String stripIllegalCharacters(String s) {
        char c;
        boolean needsStrip = false;
        StringBuilder sb;

        if (s == null) {
            return null;
        }

        for (int i = 0; i < s.length(); i++) {
            c = s.charAt(i);
            if (c >= '0' && c <= '9') {
                // continue
            } // NOPMD
            else if (c >= 'a' && c <= 'z') {
                // continue
            } // NOPMD
            else {
                needsStrip = true;
                break;
            }
        }

        if (!needsStrip) {
            return s;
        }

        sb = new StringBuilder(s.length());
        for (int i = 0; i < s.length(); i++) {
            c = s.charAt(i);
            if (c >= '0' && c <= '9') {
                sb.append(c);
            } else if (c >= 'a' && c <= 'z') {
                sb.append(c);
            }
        }

        return sb.toString();
    }

    /**
     * Checks if the class is available on the current thread's context class
     * loader.
     *
     * @param s fully qualified name of the class to check.
     * @return <code>true</code> if the class is available, <code>false</code>
     * otherwise.
     */
    public static boolean isClassAvailable(String s) {
        final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        try {
            classLoader.loadClass(s);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    /**
     * Creates a new instance of the given class. The class is loaded using the
     * current thread's context class loader and instantiated using its default
     * constructor.
     *
     * @param s fully qualified name of the class to instantiate.
     * @return the new instance or <code>null</code> on failure.
     */
    public static Object newInstance(String s) {
        final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        try {
            Class<?> clazz = classLoader.loadClass(s);
            Constructor<?> constructor = clazz.getConstructor();
            return constructor.newInstance();
        } catch (ClassNotFoundException e) {
            return null;
        } catch (IllegalAccessException e) {
            return null;
        } catch (InstantiationException e) {
            return null;
        } catch (NoSuchMethodException e) {
            // no default constructor
            return null;
        } catch (InvocationTargetException e) {
            // constructor threw an exception
            return null;
        }
    }

    private static final Log logger = LogFactory.getLog(ReflectionUtil.class);

    /**
     * Find all non-abstract classes in the given package that
     * implement/extend the provided type.
     *
     * @param packageName the package to search
     * @param baseClassOrInterface the supertype or interface to filter by
     * @return a Set of types that implement or extend the provided type
     */
    public static <T> Set<Class<? extends T>> loadClasses(String packageName, Class<T> baseClassOrInterface) {
        Set<Class<? extends T>> result = new Reflections(packageName)
            .getSubTypesOf(baseClassOrInterface)
            .stream()
            .filter(c -> !Modifier.isAbstract(c.getModifiers()))
            .collect(Collectors.toSet());

        logger.info("Loaded " + result.size());

        return result;
    }

    /**
     * retrieve all the classes that can be found on the classpath for the
     * specified packageName
     *
     * @param packageName
     * @return
     * @throws IOException
     * @throws URISyntaxException
     */
    private static Set<String> getClassNamesFromPackage(String packageName) throws IOException, URISyntaxException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        Enumeration<URL> packageURLs;
        Set<String> names = new HashSet<String>();

        packageName = packageName.replace(".", "/");
        packageURLs = classLoader.getResources(packageName);

        while (packageURLs.hasMoreElements()) {
            URL packageURL = packageURLs.nextElement();
            if (packageURL.getProtocol().equals("jar")) {
                String jarFileName;

                Enumeration<JarEntry> jarEntries;
                String entryName;

                // build jar file name, then loop through entries
                jarFileName = URLDecoder.decode(packageURL.getFile(), "UTF-8");
                jarFileName = jarFileName.substring(5, jarFileName.indexOf("!"));
                logger.info(">" + jarFileName);
                try (JarFile jf = new JarFile(jarFileName);) {
                    jarEntries = jf.entries();
                    while (jarEntries.hasMoreElements()) {
                        entryName = jarEntries.nextElement().getName();
                        if (entryName.startsWith(packageName) && entryName.endsWith(".class")) {
                            entryName = entryName.substring(packageName.length() + 1, entryName.lastIndexOf('.'));
                            names.add(entryName);
                        }
                    }
                }

                // loop through files in classpath
            } else {
                URI uri = new URI(packageURL.toString());
                File folder = new File(uri.getPath());
                // won't work with path which contains blank (%20)
                // File folder = new File(packageURL.getFile());
                File[] files = folder.listFiles();
                if (files != null) {
                    for (File actual : files) {
                        String entryName = actual.getName();
                        entryName = entryName.substring(0, entryName.lastIndexOf('.'));
                        names.add(entryName);
                    }
                }
            }
        }

        // clean up
        Iterator<String> itr = names.iterator();
        while (itr.hasNext()) {
            String name = itr.next();
            if (name.equals("package") || name.endsWith(".") || name.length() == 0) {
                itr.remove();
            }
        }

        return names;
    }
}
