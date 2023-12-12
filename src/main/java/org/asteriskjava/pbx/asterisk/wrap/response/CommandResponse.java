package org.asteriskjava.pbx.asterisk.wrap.response;

import org.asteriskjava.ami.action.response.CommandActionResponse;
import org.asteriskjava.ami.action.response.ManagerActionResponse;

import java.util.List;

public class CommandResponse extends ManagerResponse {

    private List<String> result;
    private boolean error;

    public CommandResponse(ManagerActionResponse response) {
        super(response);

        if (response instanceof CommandActionResponse) {
            result = ((CommandActionResponse) response).getOutput();
        } else if (response instanceof org.asteriskjava.manager.response.ManagerError) {
            error = true;
            //todo
//            result = Collections.singletonList(response.getOutput());
        }
    }

    public List<String> getResult() {
        return this.result;
    }

    public boolean isError() {
        return error;
    }
}
