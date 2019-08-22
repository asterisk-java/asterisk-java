package org.asteriskjava.pbx.asterisk.wrap.response;

import java.util.Collections;
import java.util.List;

public class CommandResponse extends ManagerResponse
{

    private List<String> result;
	private boolean error;

	public CommandResponse(org.asteriskjava.manager.response.ManagerResponse response)
    {
        super(response);

        if (response instanceof org.asteriskjava.manager.response.CommandResponse) {
	        result = ((org.asteriskjava.manager.response.CommandResponse) response).getResult();
        } else if (response instanceof org.asteriskjava.manager.response.ManagerError) {
        	error = true;
        	result = Collections.singletonList(response.getOutput());
        }
    }

    public List<String> getResult()
    {
        return this.result;
    }

	public boolean isError()
	{
		return error;
	}
}
