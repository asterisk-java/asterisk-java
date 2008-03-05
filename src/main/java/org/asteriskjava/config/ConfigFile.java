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
package org.asteriskjava.config;

import java.util.Map;
import java.util.List;

/**
 * An Asterisk configuration file.
 *
 * @author srt
 * @version $Id$
 * @since 1.0.0
 */
public class ConfigFile
{
    private final String filename;
    private final Map<String, List<String>> categories;

    public ConfigFile(String filename, Map<String, List<String>> categories)
    {
        this.filename = filename;
        this.categories = categories;
    }

    /**
     * Returns the filename.
     *
     * @return the filename, for example "voicemail.conf".
     */
    public String getFilename()
    {
        return filename;
    }

    /**
     * Returns the lines per category.
     *
     * @return the lines per category.
     */
    public Map<String, List<String>> getCategories()
    {
        return categories;
    }
}
