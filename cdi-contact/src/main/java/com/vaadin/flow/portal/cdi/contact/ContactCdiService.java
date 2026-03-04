package com.vaadin.flow.portal.cdi.contact;

import java.io.Serializable;

import jakarta.enterprise.context.ApplicationScoped;

import com.vaadin.flow.portal.cdi.backend.ContactService;

/**
 * Application-scoped CDI bean wrapping the {@link ContactService}.
 * <p>
 * Managed by Liferay's Weld CDI container. Discovered via {@code beans.xml}
 * with {@code bean-discovery-mode="all"} and resolved programmatically in
 * {@link ContactCdiView} via {@code CDI.current().select()}.
 */
@ApplicationScoped
public class ContactCdiService implements Serializable {

    private final ContactService contactService = new ContactService();

    public ContactService getContactService() {
        return contactService;
    }

    public int getContactCount() {
        return contactService.getContactsCount();
    }
}
