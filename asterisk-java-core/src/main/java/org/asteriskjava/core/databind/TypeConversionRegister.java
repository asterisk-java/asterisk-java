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

import org.asteriskjava.core.databind.utils.AsteriskBoolean;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

/**
 * Manages different converters for data types.
 *
 * @author Piotr Olaszewski
 * @since 4.0.0
 */
public class TypeConversionRegister {
    public static final Map<Class<?>, Map<Class<?>, Converter<?, ?>>> TYPE_CONVERTERS = new ConcurrentHashMap<>();

    static {
        convertFrom(String.class).to(boolean.class).register(AsteriskBoolean::toBoolean);
        convertFrom(String.class).to(Boolean.class).register(AsteriskBoolean::toBoolean);
        convertFrom(String.class).to(Double.class).register(Double::valueOf);
        convertFrom(String.class).to(Instant.class).register(Instant::parse);
        convertFrom(String.class).to(int.class).register(Integer::valueOf);
        convertFrom(String.class).to(Integer.class).register(Integer::valueOf);
        convertFrom(String.class).to(LocalDate.class).register(LocalDate::parse);
        convertFrom(String.class).to(LocalTime.class).register(LocalTime::parse);
        convertFrom(String.class).to(String.class).register(Object::toString);
    }

    private TypeConversionRegister() {
    }

    public static <S> TypeConverter<S> convertFrom(Class<S> sourceType) {
        return new TypeConverter<>(sourceType);
    }

    public static class TypeConverter<S> {
        private final Class<S> sourceType;

        TypeConverter(Class<S> sourceType) {
            this.sourceType = sourceType;
        }

        public <T> Registrar<S, T> to(Class<T> targetType) {
            return new Registrar<>(sourceType, targetType);
        }

        public static class Registrar<S, T> {
            private final Class<S> sourceType;
            private final Class<T> targetType;

            Registrar(Class<S> sourceType, Class<T> targetType) {
                this.sourceType = sourceType;
                this.targetType = targetType;
            }

            public void register(Converter<S, T> converter) {
                TYPE_CONVERTERS.computeIfAbsent(targetType, k -> new HashMap<>()).put(sourceType, converter);
            }
        }
    }

    @FunctionalInterface
    public interface Converter<S, T> extends Function<S, T> {
        T applyWithException(S source) throws Exception;

        default T apply(S source) {
            try {
                return applyWithException(source);
            } catch (Exception ignored) {
                return null;
            }
        }
    }
}
