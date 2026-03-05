package com.vaadin.flow.portal.cdi.events;

import jakarta.portlet.annotations.Dependency;
import jakarta.portlet.annotations.InitParameter;
import jakarta.portlet.annotations.PortletConfiguration;

import com.vaadin.flow.portal.cdi.CdiVaadinLiferayPortlet;

@PortletConfiguration(
        portletName = "EventsCdi",
        dependencies = @Dependency(name = "PortletHub",
                scope = "jakarta.portlet", version = "3.0.0"),
        initParams = @InitParameter(name = "portlet.static.resources.mapping",
                value = "/o/vaadin-cdi-portlet-static/"))
public class EventsPortlet
        extends CdiVaadinLiferayPortlet<EventsView> {

    @Override
    protected String getStaticResourcesPath() {
        return "/o/vaadin-cdi-portlet-static/";
    }
}
