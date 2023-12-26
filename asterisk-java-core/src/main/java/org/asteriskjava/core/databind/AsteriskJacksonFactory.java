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
package org.asteriskjava.core.databind;

import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.asteriskjava.core.databind.annotation.*;

import static com.fasterxml.jackson.databind.DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY;
import static com.fasterxml.jackson.databind.MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS;
import static com.fasterxml.jackson.databind.MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES;
import static com.fasterxml.jackson.databind.PropertyNamingStrategies.UPPER_CAMEL_CASE;

/**
 * @author Piotr Olaszewski
 * @since 4.0.0
 */
public class AsteriskJacksonFactory {
    public static JsonMapper create() {
        return JsonMapper
                .builder()
                .addModule(new JavaTimeModule())
                .configure(ACCEPT_CASE_INSENSITIVE_ENUMS, true)
                .configure(ACCEPT_CASE_INSENSITIVE_PROPERTIES, true)
                .configure(ACCEPT_SINGLE_VALUE_AS_ARRAY, true)
                .propertyNamingStrategy(UPPER_CAMEL_CASE)
                .annotationIntrospector(new AsteriskJacksonAnnotationIntrospector())
                .build();
    }

    static class AsteriskJacksonAnnotationIntrospector extends JacksonAnnotationIntrospector {
        @Override
        public PropertyName findNameForSerialization(Annotated a) {
            AsteriskName asteriskName = a.getAnnotation(AsteriskName.class);
            if (asteriskName != null) {
                return PropertyName.construct(asteriskName.value());
            }
            return super.findNameForSerialization(a);
        }

        @Override
        public String[] findSerializationPropertyOrder(AnnotatedClass ac) {
            AsteriskFieldOrder asteriskFieldOrder = ac.getAnnotation(AsteriskFieldOrder.class);
            if (asteriskFieldOrder != null) {
                return asteriskFieldOrder.value();
            }
            return super.findSerializationPropertyOrder(ac);
        }

        @Override
        public PropertyName findNameForDeserialization(Annotated a) {
            AsteriskName asteriskName = a.getAnnotation(AsteriskName.class);
            if (asteriskName != null) {
                return PropertyName.construct(asteriskName.value());
            }
            return super.findNameForDeserialization(a);
        }

        @Override
        public Object findDeserializer(Annotated a) {
            AsteriskDeserialize asteriskDeserialize = a.getAnnotation(AsteriskDeserialize.class);
            if (asteriskDeserialize != null) {
                @SuppressWarnings("rawtypes")
                Class<? extends JsonDeserializer> deserClass = asteriskDeserialize.deserializer();
                if (deserClass != JsonDeserializer.None.class) {
                    return deserClass;
                }
            }
            return super.findDeserializer(a);
        }

        @Override
        public Object findSerializationConverter(Annotated a) {
            AsteriskConverter asteriskConverter = a.getAnnotation(AsteriskConverter.class);
            if (asteriskConverter != null) {
                return asteriskConverter.value();
            }
            return super.findSerializationConverter(a);
        }

        @Override
        public Boolean hasAnySetter(Annotated a) {
            AsteriskAttributesBucket asteriskAttributesBucket = a.getAnnotation(AsteriskAttributesBucket.class);
            if (asteriskAttributesBucket != null) {
                return true;
            }
            return super.hasAnySetter(a);
        }
    }
}
