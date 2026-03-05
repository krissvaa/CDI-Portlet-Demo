package com.vaadin.flow.portal.cdi.events;

import java.io.Serializable;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;

/**
 * Application-scoped observer bean that logs CDI events. Demonstrates
 * multiple {@code @Observes} methods with different qualifier combinations:
 * <ul>
 *   <li>Unqualified observer — receives ALL events regardless of qualifier</li>
 *   <li>{@code @HighPriority} observer — receives only high-priority events</li>
 *   <li>{@code @LowPriority} observer — receives only low-priority events</li>
 * </ul>
 */
@ApplicationScoped
public class EventLogger implements Serializable {

    private static final int MAX_ENTRIES = 100;
    private static final DateTimeFormatter TIME_FMT =
            DateTimeFormatter.ofPattern("HH:mm:ss.SSS");

    private final List<String> log =
            Collections.synchronizedList(new ArrayList<>());

    void onAnyEvent(@Observes MessageEvent event) {
        addEntry("@Observes (any): \"" + event.getMessage()
                + "\" [priority=" + event.getPriority() + "]");
    }

    void onHighPriorityEvent(
            @Observes @HighPriority MessageEvent event) {
        addEntry("@Observes @HighPriority: \"" + event.getMessage() + "\"");
    }

    void onLowPriorityEvent(
            @Observes @LowPriority MessageEvent event) {
        addEntry("@Observes @LowPriority: \"" + event.getMessage() + "\"");
    }

    private void addEntry(String entry) {
        String ts = LocalTime.now().format(TIME_FMT);
        log.add("[" + ts + "] " + entry);
        while (log.size() > MAX_ENTRIES) {
            log.remove(0);
        }
    }

    public List<String> getLog() {
        return new ArrayList<>(log);
    }

    public void clear() {
        log.clear();
    }
}
