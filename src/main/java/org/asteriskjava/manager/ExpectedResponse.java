package org.asteriskjava.manager;

import org.asteriskjava.ami.action.ManagerAction;
import org.asteriskjava.manager.response.ManagerResponse;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Indicates that an annotated {@link ManagerAction} expects
 * a specific subclass of {@link org.asteriskjava.manager.response.ManagerResponse} when executed
 * successfully.
 *
 * @since 1.0.0
 */
@Target(TYPE)
@Retention(RUNTIME)
public @interface ExpectedResponse {
    Class<? extends ManagerResponse> value();
}
