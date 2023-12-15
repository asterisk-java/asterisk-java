package org.asteriskjava.core.databind.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Marker annotation that enables writing all unmatched fields along with their values to the 'bucket' map.
 *
 * @author Piotr Olaszewski
 * @since 4.0.0
 */
@Target({METHOD})
@Retention(RUNTIME)
public @interface AsteriskAttributesBucket {
}
