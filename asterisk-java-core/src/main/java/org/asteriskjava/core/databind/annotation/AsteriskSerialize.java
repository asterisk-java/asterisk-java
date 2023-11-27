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
package org.asteriskjava.core.databind.annotation;

import org.asteriskjava.core.databind.serializer.AsteriskSerializer;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Annotation used for configuring serialization aspects, by attaching to "getter" methods.
 *
 * @author Piotr Olaszewski
 * @since 4.0.0
 */
@Target({METHOD})
@Retention(RUNTIME)
public @interface AsteriskSerialize {
    /**
     * Serializer class to use for serializing associated value.
     */
    Class<? extends AsteriskSerializer<?>> value();
}