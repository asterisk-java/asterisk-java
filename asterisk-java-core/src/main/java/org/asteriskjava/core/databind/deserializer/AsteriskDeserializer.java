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
package org.asteriskjava.core.databind.deserializer;

/**
 * Interface representing a deserializer for a given type.
 *
 * @param <T> type of the deserialized value
 * @author Piotr Olaszewski
 * @since 4.0.0
 */
public interface AsteriskDeserializer<T> {
    /**
     * Deserializes object into Asterisk string.
     *
     * @return deserialized value
     */
    T deserialize(String value);
}
