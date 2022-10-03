package org.asteriskjava.pbx.asterisk.wrap.events;

public class ReloadEvent extends ManagerEvent {

    private static final long serialVersionUID = 1L;

    public ReloadEvent(final org.asteriskjava.manager.event.ReloadEvent event) {
        super(event);
    }
}
