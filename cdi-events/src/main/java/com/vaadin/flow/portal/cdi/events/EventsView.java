package com.vaadin.flow.portal.cdi.events;

import java.util.List;

import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.portal.PortletView;
import com.vaadin.flow.portal.PortletViewContext;

/**
 * Portlet view demonstrating the CDI event system. Users can fire events
 * with different qualifiers and observe which {@code @Observes} methods
 * are triggered.
 */
public class EventsView extends VerticalLayout implements PortletView {

    @Inject
    private Event<MessageEvent> anyEvent;

    @Inject
    @HighPriority
    private Event<MessageEvent> highEvent;

    @Inject
    @LowPriority
    private Event<MessageEvent> lowEvent;

    @Inject
    private EventLogger eventLogger;

    private Div logPanel;

    @Override
    public void onPortletViewContextInit(PortletViewContext context) {
        removeAll();
        add(new H3("CDI Events Demo"));

        // Fire event form
        TextField messageField = new TextField("Message");
        messageField.setValue("Hello CDI!");
        messageField.setWidthFull();

        Button fireAny = new Button("Fire (unqualified)", e -> {
            anyEvent.fire(
                    new MessageEvent(messageField.getValue(), "unqualified"));
            refreshLog();
        });

        Button fireHigh = new Button("Fire @HighPriority", e -> {
            highEvent.fire(
                    new MessageEvent(messageField.getValue(), "high"));
            refreshLog();
        });

        Button fireLow = new Button("Fire @LowPriority", e -> {
            lowEvent.fire(
                    new MessageEvent(messageField.getValue(), "low"));
            refreshLog();
        });

        HorizontalLayout buttons = new HorizontalLayout(fireAny, fireHigh,
                fireLow);

        add(messageField, buttons);

        // Explanation
        Paragraph explanation = new Paragraph(
                "Unqualified fire → only the unqualified @Observes fires. "
                        + "@HighPriority fire → unqualified @Observes + "
                        + "@HighPriority @Observes fire. "
                        + "@LowPriority fire → unqualified @Observes + "
                        + "@LowPriority @Observes fire.");
        explanation.getStyle().set("font-size", "var(--lumo-font-size-s)");
        explanation.getStyle().set("color",
                "var(--lumo-secondary-text-color)");
        add(explanation);

        // Event log
        add(new H4("Event Log"));

        logPanel = new Div();
        logPanel.getStyle().set("max-height", "250px");
        logPanel.getStyle().set("overflow-y", "auto");
        logPanel.getStyle().set("border",
                "1px solid var(--lumo-contrast-20pct)");
        logPanel.getStyle().set("padding", "var(--lumo-space-s)");
        logPanel.getStyle().set("font-family", "monospace");
        logPanel.getStyle().set("font-size", "var(--lumo-font-size-s)");
        logPanel.setWidthFull();
        refreshLog();
        add(logPanel);

        HorizontalLayout logButtons = new HorizontalLayout();
        logButtons.add(new Button("Refresh Log", ev -> refreshLog()));
        logButtons.add(new Button("Clear Log", ev -> {
            eventLogger.clear();
            refreshLog();
        }));
        add(logButtons);
    }

    private void refreshLog() {
        logPanel.removeAll();
        List<String> entries = eventLogger.getLog();
        if (entries.isEmpty()) {
            logPanel.add(new Span("No events fired yet."));
        } else {
            for (String entry : entries) {
                Div line = new Div(new Span(entry));
                logPanel.add(line);
            }
        }
    }
}
