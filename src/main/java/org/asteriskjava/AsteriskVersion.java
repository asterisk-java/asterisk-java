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
package org.asteriskjava;

import java.io.Serializable;

/**
 * Represents the version of an Asterisk server.
 * 
 * @since 0.2
 * @author srt
 * @version $Id$
 */
public class AsteriskVersion implements Comparable<AsteriskVersion>, Serializable
{
    private final int version;
    private final String versionString;

    /**
     * Represents the Asterisk 1.0 series.
     */
    public static final AsteriskVersion ASTERISK_1_0 = new AsteriskVersion(100,
            "Asterisk 1.0");

    /**
     * Represents the Asterisk 1.2 series.
     */
    public static final AsteriskVersion ASTERISK_1_2 = new AsteriskVersion(120,
            "Asterisk 1.2");

    /**
     * Represents the Asterisk 1.4 series.
     *
     * @since 0.3
     */
    public static final AsteriskVersion ASTERISK_1_4 = new AsteriskVersion(140,
            "Asterisk 1.4");

    /**
     * Serial version identifier.
     */
    private static final long serialVersionUID = -5696160640576385797L;

    private AsteriskVersion(int version, String versionString)
    {
        this.version = version;
        this.versionString = versionString;
    }

    /**
     * Returns <code>true</code> if this version is equal to or higher than
     * the given version.
     * 
     * @param o the version to compare to
     * @return <code>true</code> if this version is equal to or higher than
     *         the given version.
     */
    public boolean isAtLeast(AsteriskVersion o)
    {
        if (version >= o.version)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public int compareTo(AsteriskVersion o)
    {
        int otherVersion;

        otherVersion = o.version;
        if (version < otherVersion)
        {
            return -1;
        }
        else if (version > otherVersion)
        {
            return 1;
        }
        else
        {
            return 0;
        }
    }


    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (o == null || getClass() != o.getClass())
        {
            return false;
        }

        AsteriskVersion that = (AsteriskVersion) o;

        if (version != that.version)
        {
            return false;
        }

        return true;
    }

    public int hashCode()
    {
        return version;
    }

    public String toString()
    {
        return versionString;
    }
}
