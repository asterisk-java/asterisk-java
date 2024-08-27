package org.asteriskjava.manager;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Customize the mapping to Asterisk. In general the mapping is done implicitly
 * based on reflection but there are certain actions that use headers with
 * characters that are not valid in Java identifiers. In those cases you can
 * annotate the property (getter, setter or field) and provide the header name
 * that Asterisk sends or expects.
 * <p>
 * Similarly, if an AMI event name contains characters that are not valid Java
 * identifiers, the class itself can be decorated with this annotation to
 * affect the mapping.
 *
 * @since 1.0.0
 */
@Target({TYPE, METHOD, FIELD})
@Retention(RUNTIME)
public @interface AsteriskMapping {
    String value();
}
