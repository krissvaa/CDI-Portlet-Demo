# CDI Portlet Demo for Liferay

A demo project showcasing CDI (`@Inject`) support in Vaadin Flow portlets running on
Liferay 2025.Q1+ (Jakarta EE 10). The portlet displays a contact grid with data loaded
through a CDI-injected service, proving that standard dependency injection works inside
Vaadin portlet views.

The key integration point is `CdiVaadinLiferayPortlet` — a portlet base class from
[vaadin-cdi](https://github.com/vaadin/cdi) that bridges Vaadin's view creation with
CDI via a CDI-aware `Instantiator`, enabling `@Inject` in view classes.

You need Java 17 or later and Maven installed.

The documentation for Vaadin Portlet support is available [here](https://vaadin.com/docs/latest/flow/integrations/portlet).

## Project Structure

| Module | Artifact | Description |
|--------|----------|-------------|
| cdi-backend | JAR | `Contact` entity, `ContactService` (SQLite), IPC event constants |
| cdi-contact | `cdi-contact.war` | The CDI portlet — `ContactCdiView` uses `@Inject` for service access |
| cdi-bundle | `vaadin-cdi-portlet-static.war` | Shared Vaadin frontend resources (JS/CSS bundles) |

## Building

```bash
mvn clean package -Pproduction
```

Both WARs are collected in the `build/` directory:
- `cdi-contact.war`
- `vaadin-cdi-portlet-static.war`

> **Note:** Vaadin portlets only work in production mode. The `-Pproduction` profile is required.

## Running under Liferay

Before the portlet can be used, it must be deployed to a Liferay portal.
This project requires **Liferay DXP 2025.Q3 or later** — the first releases to ship
with Jakarta EE 10 (`jakarta.*` namespaces). The community Portal CE edition does not
yet support Jakarta EE 10. DXP images include a 30-day trial license.

We assume Liferay is running at http://localhost:8080/. An easy way to run a local
copy of Liferay is to use their official [docker images](https://hub.docker.com/r/liferay/dxp).
A `docker-compose.yaml` is included in this repository — start it with:

```bash
docker-compose up
```

> **Note:** Check available DXP tags at https://hub.docker.com/r/liferay/dxp/tags.
> Only tags from **2025.Q3** onward support Jakarta EE 10.

Add the following to the end of the last line in Tomcat's `setenv.sh`
(`/var/liferay/tomcat-<version>/bin`) before starting Liferay. When using the above
docker-compose file, place an edited copy of `setenv.sh` in `./files/tomcat/bin`:

````
 -Dvaadin.portlet.static.resources.mapping=/o/vaadin-cdi-portlet-static/
````

### Deploying

Copy both WARs from `build/` to the Liferay hot-deploy directory:

```bash
cp build/*.war $LIFERAY_HOME/deploy/
```

When using docker-compose, copy to `./deploy/` instead. The files disappear once Liferay processes them.

Wait ~30 seconds, then check deployment status in the Tomcat log:

```bash
tail -100 $LIFERAY_HOME/tomcat-*/logs/catalina.$(date +%Y-%m-%d).log
```

**Success indicators:**
- `STARTED cdi-contact_*` or `1 portlet for cdi-contact`
- `STARTED vaadin-cdi-portlet-static_*`

**Failure indicators:**
- `ERROR` or `Exception` stacktraces
- `FAILED` deployment messages

### Adding the Portlet to a Page

1. Log in to Liferay as an administrator
2. Navigate to a page and click the Pen (edit) icon at the top right
3. Under **Widgets**, find the **"CDI Sample"** category
4. Drag **"Contact CDI"** onto the page
5. Click **Publish** to make the change visible

The portlet should render a contact grid showing first name, last name, and email columns,
along with a status line: *"CDI Portlet - N contacts loaded via @Inject"*.

## Disabling Liferay SPA (Single Page Application)

Liferay's SPA navigation (powered by senna.js / `frontend-js-spa-web`) must be disabled
on pages that host Vaadin portlets. Vaadin's frontend is built on ES modules which only
execute once per page lifecycle — after an SPA navigation, portlets appear blank.

The recommended approach is to **exclude portlet pages from SPA** rather than disabling
it globally:

1. Navigate to **Control Panel → Instance Settings → Infrastructure → Frontend SPA Infrastructure**
2. Add portlet page paths to **Custom Excluded Paths** (regex)
3. Save

Alternatively, disable SPA globally via Instance Settings (uncheck **Enable SPA**) or
set the portal property (requires server restart):

````properties
javascript.single.page.application.enabled=false
````

## Remote Debugging

Add the following to Tomcat's `setenv.sh` before starting Liferay:

````
 -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=8000
````

Remote debugging (JDWP) is then available on port 8000
(in IntelliJ: `Run → Attach to Process…`).

## How It Works

The CDI integration follows this flow:

1. **`ContactCdiPortlet`** extends `CdiVaadinLiferayPortlet<ContactCdiView>`, which
   registers a CDI-aware `Instantiator` with Vaadin
2. When Vaadin creates **`ContactCdiView`**, CDI manages the instance and injects
   `@Inject`-annotated fields (e.g., `ContactCdiService`)
3. **`ContactCdiService`** wraps `ContactService` (the backend data layer) and is
   produced by CDI as a dependent-scoped bean
4. The view displays contacts in a `Grid` and listens for IPC events to refresh data

## Known Issues

See Vaadin Portlet [release notes](https://github.com/vaadin/portlet/releases) for
limitations and known issues.