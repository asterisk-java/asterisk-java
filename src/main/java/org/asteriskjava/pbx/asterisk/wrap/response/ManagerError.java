package org.asteriskjava.pbx.asterisk.wrap.response;

import org.asteriskjava.ami.action.api.response.ManagerActionResponse;

public class ManagerError extends CommandResponse {

    public ManagerError(ManagerActionResponse error) {
        super(error);
    }

}
