package com.vaadin.flow.portal.cdi.interceptor;

import java.io.Serializable;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;

/**
 * Application-scoped bean that collects interceptor log entries for display
 * in the portlet UI.
 */
@ApplicationScoped
public class InterceptorLog implements Serializable {

    private static final int MAX_ENTRIES = 100;
    private static final DateTimeFormatter TIME_FMT =
            DateTimeFormatter.ofPattern("HH:mm:ss.SSS");

    private final List<String> entries =
            Collections.synchronizedList(new ArrayList<>());

    public void addEntry(String entry) {
        String ts = LocalTime.now().format(TIME_FMT);
        entries.add("[" + ts + "] " + entry);
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
