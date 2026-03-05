package com.vaadin.flow.portal.cdi.scopes;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.inject.Inject;

import com.vaadin.cdi.annotation.VaadinSessionScoped;

/**
 * Session-scoped counter — one instance per Vaadin session (i.e. per user
 * browser session). Survives page refreshes within the same session but is
 * destroyed when the session ends.
 */
@VaadinSessionScoped
public class SessionCounter implements Serializable {

    private final AtomicInteger count = new AtomicInteger(0);

    @Inject
    private LifecycleLogger logger;

    @PostConstruct
    void init() {
        logger.log("SessionCounter @PostConstruct (instance "
                + Integer.toHexString(System.identityHashCode(this)) + ")");
    }

    @PreDestroy
    void destroy() {
        logger.log("SessionCounter @PreDestroy (instance "
                + Integer.toHexString(System.identityHashCode(this))
                + ", final value=" + count.get() + ")");
    }

    public int increment() {
        return count.incrementAndGet();
    }

    public int getValue() {
        return count.get();
    }

    public void reset() {
        count.set(0);
    }
}
