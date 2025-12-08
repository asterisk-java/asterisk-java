package org.asteriskjava.manager.action;

import org.asteriskjava.manager.ExpectedResponse;
import org.asteriskjava.manager.response.FaxLicenseStatusResponse;

@ExpectedResponse(FaxLicenseStatusResponse.class)
public final class FaxLicenseStatusAction extends AbstractManagerAction {
    @Override
    public String getAction() {
        return "FaxLicenseStatus";
    }
}
