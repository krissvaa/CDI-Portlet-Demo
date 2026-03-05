package com.vaadin.flow.portal.cdi.scopes;

import java.io.Serializable;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;

/**
 * Application-scoped bean that collects lifecycle events from other CDI beans.
 * Used to visualize when {@code @PostConstruct} and {@code @PreDestroy}
 * callbacks fire for beans with different scopes.
 */
@ApplicationScoped
public class LifecycleLogger implements Serializable {

    private static final int MAX_ENTRIES = 50;
    private static final DateTimeFormatter TIME_FMT =
            DateTimeFormatter.ofPattern("HH:mm:ss.SSS");

    private final List<String> entries =
            Collections.synchronizedList(new ArrayList<>());

    public void log(String message) {
        String timestamp = LocalTime.now().format(TIME_FMT);
        entries.add("[" + timestamp + "] " + message);
        while (entries.size() > MAX_ENTRIES) {
            entries.remove(0);
        }
    }

    public List<String> getEntries() {
        return new ArrayList<>(entries);
    }

    public void clear() {
        entries.clear();
    }
}
