package com.vaadin.flow.portal.cdi.contact;

import jakarta.inject.Inject;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.portal.PortletView;
import com.vaadin.flow.portal.PortletViewContext;
import com.vaadin.flow.portal.cdi.backend.Contact;
import com.vaadin.flow.portal.cdi.backend.PortletEventConstants;
import com.vaadin.flow.portal.lifecycle.PortletEvent;

/**
 * Portlet view that uses {@code @Inject} to obtain CDI-managed services.
 * <p>
 * This works because the portlet class extends {@code CdiVaadinLiferayPortlet},
 * which bridges Vaadin's view creation with CDI via a CDI-aware
 * {@code Instantiator}. Vaadin creates this view through CDI, enabling standard
 * dependency injection.
 */
public class ContactCdiView extends VerticalLayout implements PortletView {

    public static final String CDI_STATUS_ID = "cdi-status";

    @Inject
    private ContactCdiService contactCdiService;

    private Grid<Contact> grid;
    private Div statusDiv;

    public ContactCdiView() {
        statusDiv = new Div();
        statusDiv.setId(CDI_STATUS_ID);

        grid = new Grid<>(Contact.class, false);
        grid.addColumn(Contact::getFirstName).setHeader("First name");
        grid.addColumn(Contact::getLastName).setHeader("Last name");
        grid.addColumn(Contact::getEmail).setHeader("Email");

        add(statusDiv, grid);
    }

    @Override
    public void onPortletViewContextInit(PortletViewContext context) {
        context.addEventChangeListener(
                PortletEventConstants.EVENT_CONTACT_LIST_CHANGED,
                this::onContactsChanged);

        updateStatus();
        loadContacts();
    }

    private void onContactsChanged(PortletEvent event) {
        loadContacts();
        updateStatus();
    }

    private void loadContacts() {
        grid.setItems(contactCdiService.getContactService().getContacts());
    }

    private void updateStatus() {
        int count = contactCdiService.getContactCount();
        statusDiv.removeAll();
        statusDiv.add(new Span(
                "CDI Portlet - " + count + " contacts loaded via @Inject"));
    }
}
