/*
 * Copyright 2004-2022 Asterisk Java contributors
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
package org.asteriskjava.ami.databind.serializer;

import org.asteriskjava.ami.databind.serializer.custom.ComaJoiningSerializer;
import org.asteriskjava.ami.databind.serializer.std.ToStringSerializer;

import java.util.HashMap;
import java.util.Map;

public class AsteriskSerializers {
    private static final Map<Class<? extends AsteriskSerializer<?>>, ComaJoiningSerializer> customSerializers = new HashMap<>();

    private final Map<Class<? extends AsteriskSerializer<?>>, AsteriskSerializer<?>> serializerMap = new HashMap<>();

    static {
        customSerializers.put(ComaJoiningSerializer.class, new ComaJoiningSerializer());
    }

    public AsteriskSerializers add(Class<? extends AsteriskSerializer<?>> clazz, AsteriskSerializer<?> serializer) {
        serializerMap.put(clazz, serializer);
        return this;
    }

    public AsteriskSerializer<?> get(Class<? extends AsteriskSerializer<?>> key) {
        return serializerMap.get(key);
    }

    public AsteriskSerializer<?> findSerializer(Class<? extends AsteriskSerializer<?>> using) {
        return customSerializers.get(using);
    }

    public AsteriskSerializer<Object> findTypeSerializer(Class<?> clazz) {
        return new ToStringSerializer();
    }
}
