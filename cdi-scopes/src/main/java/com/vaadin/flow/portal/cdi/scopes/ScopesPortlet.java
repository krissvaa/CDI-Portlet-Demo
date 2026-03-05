package com.vaadin.flow.portal.cdi.scopes;

import jakarta.portlet.annotations.Dependency;
import jakarta.portlet.annotations.InitParameter;
import jakarta.portlet.annotations.PortletConfiguration;

import com.vaadin.flow.portal.cdi.CdiVaadinLiferayPortlet;

@PortletConfiguration(
        portletName = "ScopesCdi",
        dependencies = @Dependency(name = "PortletHub",
                scope = "jakarta.portlet", version = "3.0.0"),
        initParams = @InitParameter(name = "portlet.static.resources.mapping",
                value = "/o/vaadin-cdi-portlet-static/"))
public class ScopesPortlet
        extends CdiVaadinLiferayPortlet<ScopesView> {

    @Override
    protected String getStaticResourcesPath() {
        return "/o/vaadin-cdi-portlet-static/";
    }
}
