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
package org.asteriskjava.core.databind.serializer;

import org.asteriskjava.core.databind.AsteriskGenerator;

/**
 * Interface representing a serializer for a given type.
 *
 * @param <T> type of the serialized value
 * @author Piotr Olaszewski
 * @since 4.0.0
 */
public interface AsteriskSerializer<T> {
    /**
     * Serializes object into Asterisk string.
     *
     * @param fieldName         field name of the currently serialized object
     * @param value             object to serialize
     * @param asteriskGenerator generator used to write a Java object to an Asterisk string
     */
    void serialize(String fieldName, T value, AsteriskGenerator asteriskGenerator);
}
