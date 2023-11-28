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
package org.asteriskjava.core.databind.writer;

import org.asteriskjava.core.databind.annotation.AsteriskName;
import org.asteriskjava.core.databind.annotation.AsteriskSerialize;
import org.asteriskjava.core.databind.serializer.AsteriskSerializer;
import org.asteriskjava.core.databind.serializer.WritableFileName;
import org.asteriskjava.core.databind.serializer.std.ToStringSerializer;

import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.List;
import java.util.Map.Entry;

import static org.asteriskjava.core.databind.utils.ReflectionUtils.getGetters;

/**
 * @author Piotr Olaszewski
 * @since 4.0.0
 */
public class AsteriskObjectWriter {
    private final Class<?> clazz;
    private final Comparator<String> fieldNamesComparator;

    public AsteriskObjectWriter(Class<?> clazz, Comparator<String> fieldNamesComparator) {
        this.clazz = clazz;
        this.fieldNamesComparator = fieldNamesComparator;
    }

    public List<AsteriskObjectMethodWriter> getAsteriskObjectMethodWriters() {
        return getGetters(clazz, fieldNamesComparator)
                .entrySet()
                .stream()
                .map(this::getAsteriskObjectMethodWriter)
                .toList();
    }

    private AsteriskObjectMethodWriter getAsteriskObjectMethodWriter(Entry<String, Method> entry) {
        Method method = entry.getValue();

        String name = getName(method, entry.getKey());

        AsteriskSerializer<Object> asteriskSerializer = getAsteriskSerializer(method);

        boolean serializerWriteFieldName = asteriskSerializer instanceof WritableFileName;
        AsteriskObjectMethodWriterContext context = new AsteriskObjectMethodWriterContext(serializerWriteFieldName);

        return new AsteriskObjectMethodWriter(method, name, asteriskSerializer, context);
    }

    private static String getName(Method method, String name) {
        AsteriskName asteriskName = method.getAnnotation(AsteriskName.class);
        return asteriskName == null ? name : asteriskName.value();
    }

    private AsteriskSerializer<Object> getAsteriskSerializer(Method method) {
        AsteriskSerialize asteriskSerialize = method.getAnnotation(AsteriskSerialize.class);
        if (asteriskSerialize == null) {
            return new ToStringSerializer();
        }

        Class<? extends AsteriskSerializer<?>> asteriskSerializerClass = asteriskSerialize.value();
        try {
            //noinspection unchecked
            return (AsteriskSerializer<Object>) asteriskSerializerClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Cannot create new instance of serializer %s".formatted(asteriskSerialize), e);
        }
    }
}
