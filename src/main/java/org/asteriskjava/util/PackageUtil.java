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

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class PackageUtil {
    private static final Log logger = LogFactory.getLog(ReflectionUtil.class);

    private PackageUtil() {
        // hide constructor
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
    public static Set<String> getClassNamesFromPackage(String packageName) throws IOException, URISyntaxException {
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
