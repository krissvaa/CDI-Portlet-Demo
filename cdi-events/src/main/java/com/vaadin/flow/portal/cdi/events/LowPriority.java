package com.vaadin.flow.portal.cdi.events;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.inject.Qualifier;

/**
 * CDI qualifier annotation for low-priority events. When used with
 * {@code Event<MessageEvent>}, only observers annotated with
 * {@code @LowPriority} (plus unqualified observers) will be notified.
 */
@Qualifier
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD,
        ElementType.TYPE })
public @interface LowPriority {
}
