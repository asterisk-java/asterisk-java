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

import java.util.HashSet;
import java.util.Set;

import static java.util.Locale.ENGLISH;

/**
 * @author Stefan Reuter
 * @author Piotr Olaszewski
 * @since 4.0.0
 */
public class ToBoolean {
    private static final Set<String> TRUE_LITERALS = new HashSet<>(20);

    static {
        TRUE_LITERALS.add("yes");
        TRUE_LITERALS.add("true");
        TRUE_LITERALS.add("y");
        TRUE_LITERALS.add("t");
        TRUE_LITERALS.add("1");
        TRUE_LITERALS.add("on");
        TRUE_LITERALS.add("enabled");
    }

    private ToBoolean() {
    }

    /**
     * Checks if a String represents {@code true} or {@code false} according to Asterisk's logic.
     * <p>
     * The original implementation is <code>util.c</code> file in asterisk/asterisk repository.<br>
     * Method: {@code int ast_true(const char *s)} and {@code int ast_false(const char *s)}.
     */
    public static boolean toBoolean(Object o) {
        if (o == null) {
            return false;
        }

        if (o instanceof Boolean) {
            return (Boolean) o;
        }

        return TRUE_LITERALS.contains(o.toString().toLowerCase(ENGLISH));
    }
}
