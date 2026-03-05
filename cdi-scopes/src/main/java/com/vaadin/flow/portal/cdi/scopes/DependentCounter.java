package com.vaadin.flow.portal.cdi.scopes;

import java.io.Serializable;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;

/**
 * Dependent-scoped counter — a new instance is created for every injection
 * point. The counter always starts at 0 and its lifecycle is tied to the
 * owning bean. Each page load gets a fresh instance.
 */
@Dependent
public class DependentCounter implements Serializable {

    private int count = 0;
    private String instanceId;

    @Inject
    private LifecycleLogger logger;

    @PostConstruct
    void init() {
        instanceId = Integer.toHexString(System.identityHashCode(this));
        logger.log("DependentCounter @PostConstruct (instance " + instanceId
                + ")");
    }

    @PreDestroy
    void destroy() {
        logger.log("DependentCounter @PreDestroy (instance " + instanceId
                + ", final value=" + count + ")");
    }

    public int increment() {
        return ++count;
    }

    public int getValue() {
        return count;
    }

    public String getInstanceId() {
        return instanceId;
    }
}
