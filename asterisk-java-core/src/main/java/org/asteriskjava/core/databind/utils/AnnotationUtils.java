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

import org.asteriskjava.core.databind.annotation.AsteriskAttributesBucket;
import org.asteriskjava.core.databind.annotation.AsteriskName;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;

/**
 * Convenient class for handling annotations.
 *
 * @author Piotr Olaszewski
 * @since 4.0.0
 */
public final class AnnotationUtils {
    private AnnotationUtils() {
    }

    public static String getName(Method method, String name) {
        AsteriskName asteriskName = method.getAnnotation(AsteriskName.class);
        return asteriskName == null ? name : asteriskName.value();
    }

    public static Method getBucketMethod(Collection<Method> methods) {
        List<Method> list = methods
                .stream()
                .filter(method -> method.isAnnotationPresent(AsteriskAttributesBucket.class))
                .toList();
        if (list.size() > 1) {
            throw new IllegalArgumentException("Only one @AsteriskAttributesBucket annotation allowed");
        }
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }
}
