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
package org.asteriskjava.ami;

import java.util.Comparator;

/**
 * Convenient class to sort Action and ActionID fields in action object. Those fields are first and second retrospectively.
 *
 * @author Piotr Olaszewski
 * @since 4.0.0
 */
public class ActionFieldsComparator implements Comparator<String> {
    @Override
    public int compare(String o1, String o2) {
        if (o1.equals(o2)) {
            return 0;
        }
        if (o1.equalsIgnoreCase("Action") && o2.equalsIgnoreCase("ActionID") || (o1.equalsIgnoreCase("ActionID") && o2.equalsIgnoreCase("Action"))) {
            return 1;
        }
        if (o1.equalsIgnoreCase("Action") || o1.equalsIgnoreCase("ActionID")) {
            return -1;
        }
        return 1;
    }
}
