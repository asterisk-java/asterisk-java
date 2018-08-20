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
import java.util.regex.Pattern;

/**
 * Represents the version of an Asterisk server.
 *
 * @author srt
 * @version $Id$
 * @since 0.2
 */
public class AsteriskVersion implements Comparable<AsteriskVersion>, Serializable
{
    private static final String VERSION_PATTERN_15 = "^\\s*Asterisk (GIT-)?15[-. ].*";
    private static final String VERSION_PATTERN_14 = "^\\s*Asterisk (GIT-)?14[-. ].*";
    private static final String VERSION_PATTERN_13 = "^\\s*Asterisk ((SVN-branch|GIT)-)?13[-. ].*";
    private final int version;
    private final String versionString;
    private final Pattern patterns[];

    private static final String VERSION_PATTERN_CERTIFIED_13 = "^\\s*Asterisk certified/((SVN-branch|GIT)-)?13[-. ].*";

    /**
     * Represents the Asterisk 1.0 series.
     */
    public static final AsteriskVersion ASTERISK_1_0 = new AsteriskVersion(100, "Asterisk 1.0", new String[]{});

    /**
     * Represents the Asterisk 1.2 series.
     */
    public static final AsteriskVersion ASTERISK_1_2 = new AsteriskVersion(120, "Asterisk 1.2", new String[]{});

    /**
     * Represents the Asterisk 1.4 series.
     *
     * @since 0.3
     */
    public static final AsteriskVersion ASTERISK_1_4 = new AsteriskVersion(140, "Asterisk 1.4", new String[]{});

    /**
     * Represents the Asterisk 1.6 series.
     *
     * @since 1.0.0
     */
    public static final AsteriskVersion ASTERISK_1_6 = new AsteriskVersion(160, "Asterisk 1.6", new String[]{});

    /**
     * Represents the Asterisk 1.8 series.
     *
     * @since 1.0.0
     */
    public static final AsteriskVersion ASTERISK_1_8 = new AsteriskVersion(180, "Asterisk 1.8", new String[]{});

    /**
     * Represents the Asterisk 10 series.
     *
     * @since 1.0.0
     */
    public static final AsteriskVersion ASTERISK_10 = new AsteriskVersion(1000, "Asterisk 10", new String[]{});

    /**
     * Represents the Asterisk 11 series.
     *
     * @since 1.0.0
     */
    public static final AsteriskVersion ASTERISK_11 = new AsteriskVersion(1100, "Asterisk 11", new String[]{});

    /**
     * Represents the Asterisk 12 series.
     *
     * @since 1.0.0
     */
    public static final AsteriskVersion ASTERISK_12 = new AsteriskVersion(1200, "Asterisk 12", new String[]{});

    /**
     * Represents the Asterisk 13 series.
     *
     * @since 1.0.0
     */
    public static final AsteriskVersion ASTERISK_13 = new AsteriskVersion(1300, "Asterisk 13",
            new String[]{VERSION_PATTERN_13, VERSION_PATTERN_CERTIFIED_13});

    /**
     * Represents the Asterisk 14 series.
     *
     * @since 1.1.0
     */
    public static final AsteriskVersion ASTERISK_14 = new AsteriskVersion(1400, "Asterisk 14", VERSION_PATTERN_14);

    /**
     * Represents the Asterisk 15 series.
     *
     * @since 2.1.0
     */
    public static final AsteriskVersion ASTERISK_15 = new AsteriskVersion(1500, "Asterisk 15", VERSION_PATTERN_15);

    private static final AsteriskVersion knownVersions[] = new AsteriskVersion[]{ASTERISK_15, ASTERISK_14, ASTERISK_13};

    // current debian stable version, as of 03/07/2018
    public static final AsteriskVersion DEFAULT_VERSION = ASTERISK_13;

    /**
     * Serial version identifier.
     */
    private static final long serialVersionUID = 1L;

    private AsteriskVersion(int version, String versionString, String pattern)
    {
        this(version, versionString, new String[]{pattern});
    }

    private AsteriskVersion(int version, String versionString, String patterns[])
    {
        this.version = version;
        this.versionString = versionString;

        this.patterns = new Pattern[patterns.length];
        int i = 0;
        for (String pattern : patterns)
        {
            this.patterns[i++] = Pattern.compile(pattern);
        }
    }

    /**
     * Returns <code>true</code> if this version is equal to or higher than the
     * given version.
     *
     * @param o the version to compare to
     * @return <code>true</code> if this version is equal to or higher than the
     *         given version, <code>false</code> otherwise.
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

    /**
     * Determine the Asterisk version from the string returned by Asterisk.
     * The string should contain "Asterisk " followed by a version number.
     *
     * @param coreLine
     * @return the detected version, or null if unknown
     */
    public static AsteriskVersion getDetermineVersionFromString(String coreLine)
    {
        for (AsteriskVersion version : knownVersions)
        {
            for (Pattern pattern : version.patterns)
            {
                if (pattern.matcher(coreLine).matches())
                {
                    return version;
                }
            }
        }

        return null;
    }
}
