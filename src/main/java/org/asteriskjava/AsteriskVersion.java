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
public class AsteriskVersion implements Comparable<AsteriskVersion>, Serializable {
    private static final String VERSION_PATTERN_22 = "^\\s*Asterisk (GIT-)?22[-. ].*";
    private static final String VERSION_PATTERN_21 = "^\\s*Asterisk (GIT-)?21[-. ].*";
    private static final String VERSION_PATTERN_20 = "^\\s*Asterisk (GIT-)?20[-. ].*";
    private static final String VERSION_PATTERN_19 = "^\\s*Asterisk (GIT-)?19[-. ].*";
    private static final String VERSION_PATTERN_18 = "^\\s*Asterisk (GIT-)?18[-. ].*";
    private static final String VERSION_PATTERN_17 = "^\\s*Asterisk (GIT-)?17[-. ].*";
    private static final String VERSION_PATTERN_16 = "^\\s*Asterisk (GIT-)?16[-. ].*";
    private static final String VERSION_PATTERN_15 = "^\\s*Asterisk (GIT-)?15[-. ].*";
    private static final String VERSION_PATTERN_14 = "^\\s*Asterisk (GIT-)?14[-. ].*";
    private static final String VERSION_PATTERN_13 = "^\\s*Asterisk ((SVN-branch|GIT)-)?13[-. ].*";
    private final int version;
    private final String versionString;
    private final Pattern patterns[];

    private static final String VERSION_PATTERN_CERTIFIED_20 = "^\\s*Asterisk certified/(GIT-)?20[-. ].*";
    private static final String VERSION_PATTERN_CERTIFIED_18 = "^\\s*Asterisk certified/(GIT-)?18[-. ].*";
    private static final String VERSION_PATTERN_CERTIFIED_16 = "^\\s*Asterisk certified/(GIT-)?16[-. ].*";
    private static final String VERSION_PATTERN_CERTIFIED_13 = "^\\s*Asterisk certified/((SVN-branch|GIT)-)?13[-. ].*";


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
    public static final AsteriskVersion ASTERISK_13 = new AsteriskVersion(1300, "Asterisk 13", VERSION_PATTERN_13,
        VERSION_PATTERN_CERTIFIED_13);

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

    /**
     * Represents the Asterisk 16 series.
     *
     * @since 2.1.0
     */
    public static final AsteriskVersion ASTERISK_16 = new AsteriskVersion(1600, "Asterisk 16", VERSION_PATTERN_16, VERSION_PATTERN_CERTIFIED_16);

    /**
     * Represents the Asterisk 17 series.
     *
     * @since 3.7.0
     */
    public static final AsteriskVersion ASTERISK_17 = new AsteriskVersion(1700, "Asterisk 17", VERSION_PATTERN_17);

    /**
     * Represents the Asterisk 18 series.
     *
     * @since 3.13.0
     */
    public static final AsteriskVersion ASTERISK_18 = new AsteriskVersion(1800, "Asterisk 18", VERSION_PATTERN_18, VERSION_PATTERN_CERTIFIED_18);

    /**
     * Represents the Asterisk 19 series.
     *
     * @since 3.36.2
     */
    public static final AsteriskVersion ASTERISK_19 = new AsteriskVersion(1900, "Asterisk 19", VERSION_PATTERN_19);

    /**
     * Represents the Asterisk 20 series.
     *
     * @since 3.36.2
     */
    public static final AsteriskVersion ASTERISK_20 = new AsteriskVersion(2000, "Asterisk 20", VERSION_PATTERN_20, VERSION_PATTERN_CERTIFIED_20);

    /**
     * Represents the Asterisk 21 series.
     *
     * @since 3.40.0
     */
    public static final AsteriskVersion ASTERISK_21 = new AsteriskVersion(2100, "Asterisk 21", VERSION_PATTERN_21);

    /**
     * Represents the Asterisk 22 series.
     *
     * @since 3.40.0
     */
    public static final AsteriskVersion ASTERISK_22 = new AsteriskVersion(2200, "Asterisk 22", VERSION_PATTERN_22);

    private static final AsteriskVersion knownVersions[] = new AsteriskVersion[]{
        ASTERISK_22, ASTERISK_21, ASTERISK_20, ASTERISK_19, ASTERISK_18, ASTERISK_17, ASTERISK_16, ASTERISK_15,
        ASTERISK_14, ASTERISK_13, ASTERISK_12, ASTERISK_11, ASTERISK_10, ASTERISK_1_8, ASTERISK_1_6
    };

    // current debian stable version, as of 09/10/2018
    public static final AsteriskVersion DEFAULT_VERSION = ASTERISK_16;

    /**
     * Serial version identifier.
     */
    private static final long serialVersionUID = 1L;

    private AsteriskVersion(int version, String versionString, String... patterns) {
        this.version = version;
        this.versionString = versionString;

        this.patterns = new Pattern[patterns.length];
        int i = 0;
        for (String pattern : patterns) {
            this.patterns[i++] = Pattern.compile(pattern);
        }
    }

    /**
     * Returns <code>true</code> if this version is equal to or higher than the
     * given version.
     *
     * @param o the version to compare to
     * @return <code>true</code> if this version is equal to or higher than the
     * given version, <code>false</code> otherwise.
     */
    public boolean isAtLeast(AsteriskVersion o) {
        return version >= o.version;
    }

    public int compareTo(AsteriskVersion o) {
        return Integer.compare(version, o.version);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        AsteriskVersion that = (AsteriskVersion) o;

        return version == that.version;
    }

    @Override
    public int hashCode() {
        return version;
    }

    @Override
    public String toString() {
        return versionString;
    }

    /**
     * Determine the Asterisk version from the string returned by Asterisk. The
     * string should contain "Asterisk " followed by a version number.
     *
     * @param coreLine
     * @return the detected version, or null if unknown
     */
    public static AsteriskVersion getDetermineVersionFromString(String coreLine) {
        for (AsteriskVersion version : knownVersions) {
            for (Pattern pattern : version.patterns) {
                if (pattern.matcher(coreLine).matches()) {
                    return version;
                }
            }
        }

        return null;
    }
}
