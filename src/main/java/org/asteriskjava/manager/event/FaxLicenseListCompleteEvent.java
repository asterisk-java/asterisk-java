package org.asteriskjava.manager.event;

import org.asteriskjava.manager.AsteriskMapping;

@AsteriskMapping("FaxLicenseList complete")
public final class FaxLicenseListCompleteEvent extends ResponseEvent {
    public FaxLicenseListCompleteEvent(Object source) {
        super(source);
    }
}
