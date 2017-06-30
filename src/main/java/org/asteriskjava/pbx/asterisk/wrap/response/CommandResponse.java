package org.asteriskjava.pbx.asterisk.wrap.response;

import java.util.List;

public class CommandResponse extends ManagerResponse
{

    private List<String> result;

    public CommandResponse(org.asteriskjava.manager.response.CommandResponse response)
    {
        super(response);
        this.result = response.getResult();

    }

    public List<String> getResult()
    {
        return this.result;
    }
}
