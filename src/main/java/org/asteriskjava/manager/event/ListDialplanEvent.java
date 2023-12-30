package org.asteriskjava.manager.event;

import org.asteriskjava.ami.action.api.response.event.ResponseEvent;

/**
 * A ShowDialplanCompleteEvent is triggered for each priority in the dialplan
 * in response to a ShowDialplanAction.<p>
 * Available since Asterisk 1.6<p>
 * It is implemented in <code>main/pbx.c</code>
 *
 * @author srt
 * @version $Id$
 * @see org.asteriskjava.manager.action.ShowDialplanAction
 * @see ShowDialplanCompleteEvent
 * @since 1.0.0
 */
public class ListDialplanEvent extends ResponseEvent {
    private static final long serialVersionUID = 1L;

    private static final String PRIORITY_HINT = "hint";

    private String extension;
    private String extensionLabel;
    private boolean hint = false;
    private String application;
    private String appData;
    private String registrar;
    private String includeContext;
    private String _switch;
    private String ignorePattern;

    public ListDialplanEvent(Object source) {
        super(source);
    }

    /**
     * Returns the extension or extension pattern.
     *
     * @return the extension or extension pattern.
     */
    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    /**
     * Returns the extension label.
     *
     * @return the extension label or <code>null</code> if none.
     */
    public String getExtensionLabel() {
        return extensionLabel;
    }

    public void setExtensionLabel(String extensionLabel) {
        this.extensionLabel = extensionLabel;
    }

    /**
     * Checks whether this is a hint.
     *
     * @return <code>true</code> if this is a hint, <code>false</code> otherwise.
     */
    public boolean isHint() {
        return hint;
    }

    public void setPriority(String priorityString) {
        if (priorityString == null) {
            this.priority = null;
            return;
        }

        if (PRIORITY_HINT.equals(priorityString)) {
            hint = true;
            this.priority = null;
        } else {
            this.priority = Integer.parseInt(priorityString);
        }
    }

    /**
     * Returns the application configured to handle this priority.
     *
     * @return the application configured to handle this priority.
     */
    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application;
    }

    /**
     * Returns the parameters of the application configured to handle this priority.
     *
     * @return the parameters of the application configured to handle this priority
     * or <code>null</code> if none.
     */
    public String getAppData() {
        return appData;
    }

    public void setAppData(String appData) {
        this.appData = appData;
    }

    /**
     * Returns the registrar that registered this priority.<p>
     * Typical values are "features" for the parkedcalls context, "pbx_config" for priorities
     * defined in <code>extensions.conf</code> or "app_dial" for the
     * app_dial_gosub_virtual_context context.
     *
     * @return the registrar that registered this priority.
     */
    public String getRegistrar() {
        return registrar;
    }

    public void setRegistrar(String registrar) {
        this.registrar = registrar;
    }

    public String getIncludeContext() {
        return includeContext;
    }

    public void setIncludeContext(String includeContext) {
        this.includeContext = includeContext;
    }

    public String getSwitch() {
        return _switch;
    }

    public ListDialplanEvent setSwitch(String _switch) {
        this._switch = _switch;
        return this;
    }

    public String getIgnorePattern() {
        return ignorePattern;
    }

    public ListDialplanEvent setIgnorePattern(String ignorePattern) {
        this.ignorePattern = ignorePattern;
        return this;
    }
}
