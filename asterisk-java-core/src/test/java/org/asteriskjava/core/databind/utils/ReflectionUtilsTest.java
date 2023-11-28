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

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.Map;
import java.util.StringJoiner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.asteriskjava.core.databind.utils.ReflectionUtils.getGetters;

class ReflectionUtilsTest {
    @Test
    void shouldDoesNotReturnGetterWhenHasAnyParameters() {
        //when
        Map<String, Method> getters = getGetters(InvalidClass.class);

        //then
        assertThat(getters).doesNotContainKey("WithParameters");
    }

    @Test
    void shouldDoesNotReturnGetterWhenHasVoidType() {
        //when
        Map<String, Method> getters = getGetters(InvalidClass.class);

        //then
        assertThat(getters).doesNotContainKey("WithVoidType");
    }

    @Test
    void shouldDoesNotReturnGetterWhenHasNotPublicScope() {
        //when
        Map<String, Method> getters = getGetters(InvalidClass.class);

        //then
        assertThat(getters).doesNotContainKey("WhichIsHasNotPublicScope");
    }

    @Test
    void shouldDoesNotReturnGetterWhenIsNative() {
        //when
        Map<String, Method> getters = getGetters(InvalidClass.class);

        //then
        assertThat(getters).doesNotContainKey("WithNative");
    }

    @Test
    void shouldDoesNotReturnGetterWhenIsAbstract() {
        //when
        Map<String, Method> getters = getGetters(InvalidClass.class);

        //then
        assertThat(getters).doesNotContainKey("WithAbstract");
    }

    @Test
    void shouldDoesNotReturnGetterWhenIsStatic() {
        //when
        Map<String, Method> getters = getGetters(InvalidClass.class);

        //then
        assertThat(getters).doesNotContainKey("WithStatic");
    }

    @Test
    void shouldDoesNotReturnGetterWhenIsToString() {
        //when
        Map<String, Method> getters = getGetters(InvalidClass.class);

        //then
        assertThat(getters).doesNotContainKey("toString");
    }

    @Test
    void shouldDoesNotReturnGetterWhenNameHasOnlyGet() {
        //when
        Map<String, Method> getters = getGetters(InvalidClass.class);

        //then
        assertThat(getters).isEmpty();
    }

    @Test
    void shouldDoesNotReturnGetterWhenNameHasOnlyIs() {
        //when
        Map<String, Method> getters = getGetters(InvalidClass.class);

        //then
        assertThat(getters).isEmpty();
    }

    static abstract class InvalidClass {
        public String getWithParameters(String parameter) {
            return parameter;
        }

        public void getWithVoidType() {
        }

        String getWhichIsHasNotPublicScope() {
            return "";
        }

        public native String getWithNative();

        public abstract String getWithAbstract();

        public static String getWithStatic() {
            return "";
        }

        public String get() {
            return "";
        }

        public String is() {
            return "";
        }

        @Override
        public String toString() {
            return new StringJoiner(", ", InvalidClass.class.getSimpleName() + "[", "]")
                    .toString();
        }
    }

    @Test
    void shouldReturnValidGetters() {
        //when
        Map<String, Method> getters = getGetters(ValidClass.class);

        //then
        assertThat(getters).containsOnlyKeys("Value", "Valid", "Action");
    }

    @Test
    void shouldReturnValidGettersSorted() {
        //when
        Map<String, Method> getters = getGetters(ValidClass.class, new ActionFieldsComparator());

        //then
        assertThat(getters.keySet()).containsExactly("Action", "Value", "Valid");
    }

    static class ValidClass {

        public String getValue() {
            return "value";
        }

        public boolean isValid() {
            return true;
        }

        public String getAction() {
            return "NewAction";
        }
    }

    static class ActionFieldsComparator implements Comparator<String> {
        @Override
        public int compare(String o1, String o2) {
            if (o1.equals(o2)) {
                return 0;
            }
            return o1.equalsIgnoreCase("Action") ? -1 : 1;
        }
    }
}
