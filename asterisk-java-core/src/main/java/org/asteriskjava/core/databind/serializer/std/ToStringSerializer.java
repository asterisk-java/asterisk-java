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
package org.asteriskjava.core.databind.serializer.std;

import org.asteriskjava.core.databind.AsteriskGenerator;
import org.asteriskjava.core.databind.serializer.AsteriskSerializer;

/**
 * Base serializer which calls only the toString method on the passed value.
 *
 * @author Piotr Olaszewski
 * @since 4.0.0
 */
public class ToStringSerializer implements AsteriskSerializer<Object> {
    @Override
    public void serialize(String fieldName, Object value, AsteriskGenerator asteriskGenerator) {
        asteriskGenerator.writeFieldValue(value.toString());
    }
}
