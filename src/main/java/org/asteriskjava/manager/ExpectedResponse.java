package org.asteriskjava.manager;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.asteriskjava.manager.response.ManagerResponse;

/**
 * Indicates that an annotated {@link org.asteriskjava.manager.action.ManagerAction} expects
 * a specific subclass of {@link org.asteriskjava.manager.response.ManagerResponse} when executed
 * successfully.
 *
 * @since 1.0.0
 */
@Target(TYPE)
@Retention(RUNTIME)
public @interface ExpectedResponse
{
    Class<? extends ManagerResponse> value();
}
