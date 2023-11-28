package org.asteriskjava.pbx.asterisk.wrap.response;

import org.asteriskjava.ami.action.response.ManagerResponse;

public class ManagerError extends CommandResponse {

    public ManagerError(ManagerResponse error) {
        super(error);
    }

}
