package com.vaadin.flow.portal.cdi.contact;

import jakarta.portlet.annotations.Dependency;
import jakarta.portlet.annotations.InitParameter;
import jakarta.portlet.annotations.PortletConfiguration;

import com.vaadin.flow.portal.cdi.CdiVaadinLiferayPortlet;

/**
 * CDI portlet extending {@link CdiVaadinLiferayPortlet} for true {@code @Inject}
 * support in Vaadin views on Liferay.
 *
 * @see ContactCdiView
 */
@PortletConfiguration(
    portletName = "ContactCdi",
    dependencies = @Dependency(name = "PortletHub", scope = "jakarta.portlet", version = "3.0.0"),
    initParams = @InitParameter(name = "portlet.static.resources.mapping", value = "/o/vaadin-cdi-portlet-static/")
)
public class ContactCdiPortlet extends CdiVaadinLiferayPortlet<ContactCdiView> {

    @Override
    protected String getStaticResourcesPath() {
        return "/o/vaadin-cdi-portlet-static/";
    }
}
