package com.vaadin.flow.portal.cdi.scopes;

import java.util.List;

import jakarta.inject.Inject;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.portal.PortletView;
import com.vaadin.flow.portal.PortletViewContext;

/**
 * Portlet view demonstrating CDI scope lifecycles. Three counters with
 * different scopes are displayed side-by-side so the user can observe how
 * each scope manages bean instances.
 */
public class ScopesView extends VerticalLayout implements PortletView {

    @Inject
    private ApplicationCounter appCounter;

    @Inject
    private SessionCounter sessionCounter;

    @Inject
    private DependentCounter dependentCounter;

    @Inject
    private LifecycleLogger lifecycleLogger;

    private Span appValue;
    private Span sessionValue;
    private Span dependentValue;
    private Div logPanel;

    @Override
    public void onPortletViewContextInit(PortletViewContext context) {
        removeAll();
        add(new H3("CDI Scopes Demo"));

        HorizontalLayout counters = new HorizontalLayout();
        counters.setWidthFull();
        counters.add(
                createCounterPanel("@ApplicationScoped",
                        "Shared across ALL users and sessions. "
                                + "Open in another browser — same value.",
                        appCounter.getValue(),
                        v -> appValue = v,
                        () -> {
                            appCounter.increment();
                            appValue.setText(
                                    String.valueOf(appCounter.getValue()));
                        },
                        () -> {
                            appCounter.reset();
                            appValue.setText(
                                    String.valueOf(appCounter.getValue()));
                        }),
                createCounterPanel("@VaadinSessionScoped",
                        "Per Vaadin session (browser tab group). "
                                + "Survives page refresh, lost on session end.",
                        sessionCounter.getValue(),
                        v -> sessionValue = v,
                        () -> {
                            sessionCounter.increment();
                            sessionValue.setText(
                                    String.valueOf(sessionCounter.getValue()));
                        },
                        () -> {
                            sessionCounter.reset();
                            sessionValue.setText(String
                                    .valueOf(sessionCounter.getValue()));
                        }),
                createCounterPanel("@Dependent",
                        "New instance per injection. Always starts at 0. "
                                + "Instance: " + dependentCounter.getInstanceId(),
                        dependentCounter.getValue(),
                        v -> dependentValue = v,
                        () -> {
                            dependentCounter.increment();
                            dependentValue.setText(String
                                    .valueOf(dependentCounter.getValue()));
                        },
                        null));
        add(counters);

        // Lifecycle log section
        add(new H4("Lifecycle Event Log"));
        add(new Paragraph("Shows @PostConstruct / @PreDestroy calls "
                + "with bean identity and timestamps."));

        logPanel = new Div();
        logPanel.getStyle().set("max-height", "200px");
        logPanel.getStyle().set("overflow-y", "auto");
        logPanel.getStyle().set("border", "1px solid var(--lumo-contrast-20pct)");
        logPanel.getStyle().set("padding", "var(--lumo-space-s)");
        logPanel.getStyle().set("font-family", "monospace");
        logPanel.getStyle().set("font-size", "var(--lumo-font-size-s)");
        logPanel.setWidthFull();
        refreshLog();
        add(logPanel);

        HorizontalLayout logButtons = new HorizontalLayout();
        logButtons.add(new Button("Refresh Log", e -> refreshLog()));
        logButtons.add(new Button("Clear Log", e -> {
            lifecycleLogger.clear();
            refreshLog();
        }));
        add(logButtons);
    }

    private VerticalLayout createCounterPanel(String title,
            String description, int initialValue,
            java.util.function.Consumer<Span> valueRef, Runnable onIncrement,
            Runnable onReset) {
        VerticalLayout panel = new VerticalLayout();
        panel.getStyle().set("border",
                "1px solid var(--lumo-contrast-20pct)");
        panel.getStyle().set("border-radius", "var(--lumo-border-radius-m)");
        panel.getStyle().set("padding", "var(--lumo-space-m)");
        panel.setWidth("33%");

        H4 heading = new H4(title);
        heading.getStyle().set("margin", "0");

        Span value = new Span(String.valueOf(initialValue));
        value.getStyle().set("font-size", "var(--lumo-font-size-xxxl)");
        value.getStyle().set("font-weight", "bold");
        valueRef.accept(value);

        Button incrementBtn = new Button("Increment",
                e -> onIncrement.run());

        Paragraph desc = new Paragraph(description);
        desc.getStyle().set("font-size", "var(--lumo-font-size-s)");
        desc.getStyle().set("color", "var(--lumo-secondary-text-color)");

        panel.add(heading, value, incrementBtn);
        if (onReset != null) {
            panel.add(new Button("Reset", e -> onReset.run()));
        }
        panel.add(desc);
        return panel;
    }

    private void refreshLog() {
        logPanel.removeAll();
        List<String> entries = lifecycleLogger.getEntries();
        if (entries.isEmpty()) {
            logPanel.add(new Span("No lifecycle events yet."));
        } else {
            for (String entry : entries) {
                Div line = new Div(new Span(entry));
                logPanel.add(line);
            }
        }
    }
}
