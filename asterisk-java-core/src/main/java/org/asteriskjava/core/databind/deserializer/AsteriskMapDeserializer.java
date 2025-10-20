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

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdKeyDeserializer;
import com.fasterxml.jackson.databind.node.TextNode;
import org.apache.commons.lang3.tuple.Pair;
import org.asteriskjava.core.databind.annotation.AsteriskDeserialize;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Piotr Olaszewski
 * @since 4.0.0
 */
public class AsteriskMapDeserializer extends JsonDeserializer<Map<Object, Object>> implements ContextualDeserializer {
    private Class<?> keyAs;
    private Class<?> valueAs;

    @Override
    public Map<Object, Object> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        TreeNode treeNode = p.readValueAsTree();
        if (treeNode.isValueNode()) {
            String text = ((TextNode) treeNode).asText();
            Pair<Object, Object> result = getObjectObjectPair(ctxt, text);
            return Map.of(result.getKey(), result.getValue());
        } else {
            Map<Object, Object> map = new LinkedHashMap<>();
            for (JsonNode jsonNode : (JsonNode) treeNode) {
                String text = jsonNode.asText();
                Pair<Object, Object> result = getObjectObjectPair(ctxt, text);
                map.put(result.getKey(), result.getValue());
            }
            return map;
        }
    }

    @Override
    public JsonDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty property) {
        AsteriskDeserialize deserialize = property.getAnnotation(AsteriskDeserialize.class);
        keyAs = deserialize.keyAs();
        valueAs = deserialize.valueAs();
        return this;
    }

    private Pair<Object, Object> getObjectObjectPair(DeserializationContext ctxt, String text) throws IOException {
        String[] split = text.split("=");
        Object key = StdKeyDeserializer.forType(keyAs).deserializeKey(split[0], ctxt);
        Object value = StdKeyDeserializer.forType(valueAs).deserializeKey(split[1], ctxt);
        return Pair.of(key, value);
    }
}
