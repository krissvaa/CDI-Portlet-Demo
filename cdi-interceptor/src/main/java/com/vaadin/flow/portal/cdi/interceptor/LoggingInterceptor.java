package com.vaadin.flow.portal.cdi.interceptor;

import java.io.Serializable;

import jakarta.annotation.Priority;
import jakarta.inject.Inject;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;

/**
 * CDI interceptor that logs method entry, exit, duration, and exceptions.
 * Activated by the {@link Logged} interceptor binding. Uses
 * {@code @Priority} for global enablement (no {@code <interceptors>}
 * declaration needed in beans.xml with CDI 4.0).
 */
@Logged
@Interceptor
@Priority(Interceptor.Priority.APPLICATION)
public class LoggingInterceptor implements Serializable {

    @Inject
    private InterceptorLog log;

    @AroundInvoke
    public Object logMethodCall(InvocationContext ctx) throws Exception {
        String method = ctx.getMethod().getDeclaringClass().getSimpleName()
                + "." + ctx.getMethod().getName() + "()";

        log.addEntry("ENTER " + method);
        long start = System.nanoTime();
        try {
            Object result = ctx.proceed();
            long elapsedMs = (System.nanoTime() - start) / 1_000_000;
            log.addEntry("EXIT  " + method + " [" + elapsedMs + " ms]");
            return result;
        } catch (Exception e) {
            long elapsedMs = (System.nanoTime() - start) / 1_000_000;
            log.addEntry("ERROR " + method + " threw "
                    + e.getClass().getSimpleName() + ": " + e.getMessage()
                    + " [" + elapsedMs + " ms]");
            throw e;
        }
    }
}
