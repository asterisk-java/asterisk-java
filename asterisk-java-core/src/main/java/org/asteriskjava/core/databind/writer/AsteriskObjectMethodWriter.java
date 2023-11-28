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

import org.asteriskjava.core.databind.AsteriskGenerator;
import org.asteriskjava.core.databind.serializer.AsteriskSerializer;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Writer class to write field names and serialized values using the {@link AsteriskGenerator}.
 *
 * @author Piotr Olaszewski
 * @since 4.0.0
 */
public class AsteriskObjectMethodWriter {
    private final Method method;
    private final String name;
    private final AsteriskSerializer<Object> asteriskSerializer;
    private final AsteriskObjectMethodWriterContext asteriskObjectMethodWriterContext;

    public AsteriskObjectMethodWriter(
            Method method,
            String name,
            AsteriskSerializer<Object> asteriskSerializer,
            AsteriskObjectMethodWriterContext asteriskObjectMethodWriterContext
    ) {
        this.method = method;
        this.name = name;
        this.asteriskSerializer = asteriskSerializer;
        this.asteriskObjectMethodWriterContext = asteriskObjectMethodWriterContext;
    }

    public void writeName(AsteriskGenerator asteriskGenerator) {
        if (!asteriskObjectMethodWriterContext.serializerWriteFieldName()) {
            asteriskGenerator.writeFieldName(name);
        }
    }

    public void writeValue(Object obj, AsteriskGenerator asteriskGenerator) {
        Object currentValue = getCurrentValue(obj);
        asteriskSerializer.serialize(name, currentValue, asteriskGenerator);
    }

    private Object getCurrentValue(Object obj) {
        try {
            return method.invoke(obj);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}
