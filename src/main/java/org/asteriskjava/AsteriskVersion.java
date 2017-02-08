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
 * @author srt
 * @version $Id$
 * @since 0.2
 */
public class AsteriskVersion implements Comparable<AsteriskVersion>, Serializable
{
    private final int version;
    private final String versionString;

    /**
     * Represents the Asterisk 1.0 series.
     */
    public static final AsteriskVersion ASTERISK_1_0 = new AsteriskVersion(100, "Asterisk 1.0");

    /**
     * Represents the Asterisk 1.2 series.
     */
    public static final AsteriskVersion ASTERISK_1_2 = new AsteriskVersion(120, "Asterisk 1.2");

    /**
     * Represents the Asterisk 1.4 series.
     *
     * @since 0.3
     */
    public static final AsteriskVersion ASTERISK_1_4 = new AsteriskVersion(140, "Asterisk 1.4");

    /**
     * Represents the Asterisk 1.6 series.
     *
     * @since 1.0.0
     */
    public static final AsteriskVersion ASTERISK_1_6 = new AsteriskVersion(160, "Asterisk 1.6");

    /**
     * Represents the Asterisk 1.8 series.
     *
     * @since 1.0.0
     */
    public static final AsteriskVersion ASTERISK_1_8 = new AsteriskVersion(180, "Asterisk 1.8");

    /**
     * Represents the Asterisk 10 series.
     *
     * @since 1.0.0
     */
    public static final AsteriskVersion ASTERISK_10 = new AsteriskVersion(1000, "Asterisk 10");

    /**
     * Represents the Asterisk 11 series.
     *
     * @since 1.0.0
     */
    public static final AsteriskVersion ASTERISK_11 = new AsteriskVersion(1100, "Asterisk 11");

    /**
     * Represents the Asterisk 12 series.
     *
     * @since 1.0.0
     */
    public static final AsteriskVersion ASTERISK_12 = new AsteriskVersion(1200, "Asterisk 12");

    /**
     * Represents the Asterisk 13 series.
     *
     * @since 1.0.0
     */
    public static final AsteriskVersion ASTERISK_13 = new AsteriskVersion(1300, "Asterisk 13");

    /**
     * Represents the Asterisk 14 series.
     *
     * @since 1.1.0
     */
    public static final AsteriskVersion ASTERISK_14 = new AsteriskVersion(1400, "Asterisk 14");


    /**
     * Serial version identifier.
     */
    private static final long serialVersionUID = 1L;

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
     *         the given version, <code>false</code> otherwise.
     */
    public boolean isAtLeast(AsteriskVersion o)
    {
        return version >= o.version;
    }

    public int compareTo(AsteriskVersion o)
    {
        return Integer.compare(version, o.version);
    }

    @Override
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

        return version == that.version;
    }

    @Override
    public int hashCode()
    {
        return version;
    }

    @Override
    public String toString()
    {
        return versionString;
    }
}
