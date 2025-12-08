package org.asteriskjava.manager.action;

import org.asteriskjava.manager.event.FaxLicenseListCompleteEvent;
import org.asteriskjava.manager.event.ResponseEvent;

public final class FaxLicenseListAction extends AbstractManagerAction implements EventGeneratingAction {
    @Override
    public String getAction() {
        return "FaxLicenseList";
    }

    @Override
    public Class<? extends ResponseEvent> getActionCompleteEventClass() {
        return FaxLicenseListCompleteEvent.class;
    }
}
