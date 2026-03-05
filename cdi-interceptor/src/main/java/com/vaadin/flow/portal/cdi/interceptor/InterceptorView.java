package com.vaadin.flow.portal.cdi.interceptor;

import java.util.List;

import jakarta.inject.Inject;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.portal.PortletView;
import com.vaadin.flow.portal.PortletViewContext;

/**
 * Portlet view demonstrating CDI interceptors. The injected
 * {@link DataService} has methods annotated with {@link Logged}, so
 * every call is transparently intercepted by {@link LoggingInterceptor}.
 * The interceptor log is displayed below the service invocation buttons.
 */
public class InterceptorView extends VerticalLayout implements PortletView {

    @Inject
    private DataService dataService;

    @Inject
    private InterceptorLog interceptorLog;

    private Span resultDisplay;
    private Div logPanel;

    @Override
    public void onPortletViewContextInit(PortletViewContext context) {
        removeAll();
        add(new H3("CDI Interceptors Demo"));

        // Explanation
        Paragraph explanation = new Paragraph(
                "Each button calls a @Logged service method. "
                        + "The LoggingInterceptor transparently logs "
                        + "entry/exit/duration/errors via @AroundInvoke — "
                        + "no logging code in the service itself.");
        explanation.getStyle().set("font-size", "var(--lumo-font-size-s)");
        explanation.getStyle().set("color",
                "var(--lumo-secondary-text-color)");
        add(explanation);

        // Service operation buttons
        Button fetchBtn = new Button("Fetch Data", e -> {
            String result = dataService.fetchData();
            resultDisplay.setText("Result: " + result);
            refreshLog();
        });

        Button processBtn = new Button("Process 5 Items", e -> {
            List<String> items = dataService.processItems(5);
            resultDisplay
                    .setText("Result: " + items.size() + " items processed");
            refreshLog();
        });

        Button riskyBtn = new Button("Risky Operation", e -> {
            try {
                String result = dataService.riskyOperation();
                resultDisplay.setText("Result: " + result);
            } catch (RuntimeException ex) {
                resultDisplay.setText("Error: " + ex.getMessage());
                Notification.show("Operation failed: " + ex.getMessage(),
                        3000, Notification.Position.MIDDLE);
            }
            refreshLog();
        });

        HorizontalLayout buttons = new HorizontalLayout(fetchBtn, processBtn,
                riskyBtn);
        add(buttons);

        // Result display
        resultDisplay = new Span("Click a button to invoke a service method.");
        resultDisplay.getStyle().set("font-style", "italic");
        add(resultDisplay);

        // Interceptor log
        add(new H4("Interceptor Activity Log"));

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
            interceptorLog.clear();
            refreshLog();
        }));
        add(logButtons);
    }

    private void refreshLog() {
        logPanel.removeAll();
        List<String> entries = interceptorLog.getEntries();
        if (entries.isEmpty()) {
            logPanel.add(new Span("No interceptor activity yet."));
        } else {
            for (String entry : entries) {
                Div line = new Div(new Span(entry));
                logPanel.add(line);
            }
        }
    }
}
