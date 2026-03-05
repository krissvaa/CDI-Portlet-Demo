package com.vaadin.flow.portal.cdi.scopes;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

/**
 * Application-scoped counter — single instance shared across ALL users and
 * sessions. Created once when first injected, destroyed only when the
 * application shuts down.
 */
@ApplicationScoped
public class ApplicationCounter implements Serializable {

    private final AtomicInteger count = new AtomicInteger(0);

    @Inject
    private LifecycleLogger logger;

    @PostConstruct
    void init() {
        logger.log("ApplicationCounter @PostConstruct (instance "
                + Integer.toHexString(System.identityHashCode(this)) + ")");
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
