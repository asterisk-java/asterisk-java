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
package org.asteriskjava.core.databind.serializer.custom;

import org.asteriskjava.core.databind.AsteriskGenerator;
import org.asteriskjava.core.databind.serializer.AsteriskSerializer;
import org.asteriskjava.core.databind.serializer.WritableFileName;

import java.util.Map;

import static java.lang.String.format;

/**
 * Serializer for key=value pairs with an additional field name writer.
 * <p>
 * Following code:
 * <pre>
 * &#64;AsteriskSerialize(VariableSerializer.class)
 * public Map&lt;String, String&gt; getVariable() {
 *     ...
 * }
 * </pre>
 * would produce:
 * <pre>
 * Variable: key1=value1
 * Variable: key2=value2
 * Variable: key3=value3
 * </pre>
 *
 * @author Piotr Olaszewski
 * @since 4.0.0
 */
public class VariableSerializer implements AsteriskSerializer<Map<String, String>>, WritableFileName {
    @Override
    public void serialize(String fieldName, Map<String, String> value, AsteriskGenerator asteriskGenerator) {
        value.forEach((key, v) -> {
            asteriskGenerator.writeFieldName(fieldName);
            asteriskGenerator.writeFieldValue(format("%s=%s", key, v));
        });
    }
}
