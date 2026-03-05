package com.vaadin.flow.portal.cdi.interceptor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.interceptor.InterceptorBinding;

/**
 * CDI interceptor binding annotation. Methods or classes annotated with
 * {@code @Logged} will have their invocations intercepted by
 * {@link LoggingInterceptor}, which logs method entry, exit, duration,
 * and exceptions.
 */
@InterceptorBinding
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.METHOD })
public @interface Logged {
}
