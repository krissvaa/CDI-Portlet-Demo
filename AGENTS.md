# CDI Portlet Demo

Standalone CDI portlet demo showcasing `@Inject` support in Vaadin portlets on Liferay.

| Module | Artifact | Description |
|--------|----------|-------------|
| cdi-backend | JAR | Contact entity, ContactService (SQLite at `/tmp/vaadin-cdi-portal.db`), IPC event constants |
| cdi-contact | `cdi-contact.war` | CDI contact portlet — uses `@Inject` for service injection |
| cdi-bundle | `vaadin-cdi-portlet-static.war` | Shared Vaadin frontend resources |

Stack: Vaadin 24.9.10, vaadin-portlet 2.1-SNAPSHOT, vaadin-cdi 16.0-SNAPSHOT, Jakarta EE 10, Java 17.

## Build

```bash
mvn clean package -Pproduction
```

Output WARs are collected in `build/`:
- `cdi-contact.war`
- `vaadin-cdi-portlet-static.war`

## Deploy to Liferay

Copy both WARs to the Liferay hot-deploy directory:

```bash
cp build/*.war $LIFERAY_HOME/deploy/
```

`$LIFERAY_HOME` is the root of your Liferay installation (contains `deploy/`, `tomcat-*/`, etc.). Liferay auto-detects and deploys WARs from the `deploy/` directory.

### Verify Deployment

Wait ~30 seconds for Liferay to process the WARs, then check logs:

```bash
tail -100 $LIFERAY_HOME/tomcat-*/logs/catalina.$(date +%Y-%m-%d).log
```

**Success indicators:**
- `STARTED cdi-contact_*` or `1 portlet for cdi-contact`
- `STARTED vaadin-cdi-portlet-static_*`

**Failure indicators:**
- `ERROR` or `Exception` stacktraces
- `FAILED` deployment messages
- `ClassNotFoundException` or `NoClassDefFoundError`

### Add Portlet to a Page

1. Log in to Liferay as an admin
2. Edit a page or create a new page
3. Add widget → look under **"CDI Sample"** category → **"Contact CDI"**
4. The portlet renders a contact grid with CDI status

## Playwright MCP (optional)

If a Playwright MCP server is available, use it to interact with the Liferay UI for testing and verification.

### Login

1. Navigate to `http://localhost:8080/en/c/portal/login`
2. Fill login form:
   - Username field: `#_com_liferay_login_web_portlet_LoginPortlet_login`
   - Password field: `#_com_liferay_login_web_portlet_LoginPortlet_password`
3. Click `button[type='submit']` ("Sign In")
4. Wait for `div.personal-menu-dropdown` to confirm successful login

### Portlet Load

Vaadin portlets go through: HTML shell → web component init → Vaadin app init. Wait for actual Vaadin components to appear inside the portlet root before interacting.

### DOM Structure & Selectors

**ContactCdi portlet** (`ContactCdiView.java`):
- `div#cdi-status` — CDI status indicator, shows: "CDI Portlet - N contacts loaded via @Inject"
- `vaadin-grid` — contact grid with columns: firstName, lastName, email
  - `vaadin-grid-cell-content` — individual cell values

### Test Scenarios

1. **Smoke test**: Navigate to page, verify `vaadin-grid` renders with contact data
2. **CDI verification**: Check `#cdi-status` shows "CDI Portlet - N contacts loaded via @Inject" (confirms `@Inject` worked)

### Tips

- Always take a screenshot after navigation to see the current state
- If a portlet shows a loading spinner, wait a few seconds and screenshot again

## Key Source Files

- `cdi-contact/src/main/java/com/vaadin/flow/portal/cdi/contact/ContactCdiView.java`
- `cdi-contact/src/main/java/com/vaadin/flow/portal/cdi/contact/ContactCdiPortlet.java`
- `cdi-contact/src/main/java/com/vaadin/flow/portal/cdi/contact/ContactCdiService.java`
- `cdi-backend/src/main/java/com/vaadin/flow/portal/cdi/backend/ContactService.java`
- `cdi-backend/src/main/java/com/vaadin/flow/portal/cdi/backend/PortletEventConstants.java`