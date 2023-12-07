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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.asteriskjava.core.databind.utils.Invoker.MethodInvoker.method;

/**
 * Helper for invoking methods via reflection.
 * <p>
 * Examples:
 * <pre>
 * invokeOn(object).method(method).withoutParameter();
 * invokeOn(object).method(method).withParameter("some param");
 * </pre>
 *
 * @author Piotr Olaszewski
 * @since 4.0.0
 */
@FunctionalInterface
public interface Invoker {
    String WITHOUT_PARAMETER_MARKER = "<invokeWithoutParameterMarker>";

    Object withParameter(Object value);

    default Object withoutParameter() {
        return withParameter(WITHOUT_PARAMETER_MARKER);
    }

    static MethodInvoker invokeOn(Object object) {
        return method -> method(method, object);
    }

    @FunctionalInterface
    interface MethodInvoker {
        Invoker method(Method method);

        static Invoker method(Method method, Object object) {
            return value -> {
                try {
                    if (value == WITHOUT_PARAMETER_MARKER) {
                        return method.invoke(object);
                    }
                    return method.invoke(object, value);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            };
        }
    }
}
