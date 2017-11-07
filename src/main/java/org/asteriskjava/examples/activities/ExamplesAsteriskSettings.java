package org.asteriskjava.examples.activities;

import org.asteriskjava.pbx.DefaultAsteriskSettings;

public class ExamplesAsteriskSettings extends DefaultAsteriskSettings
{

	@Override
	public String getManagerPassword()
	{
		// this password MUST match the password (secret=) in manager.conf
		return "a good password";
	}

	@Override
	public String getManagerUsername()
	{
		// this MUST match the section header '[myconnection]' in manager.conf
        return "myconnection";
	}

	@Override
	public String getAsteriskIP()
	{
		// The IP address or FQDN of your Asterisk server.
        return "2.2.2.2";
	}

	@Override
	public String getAgiHost()
	{
		// The IP Address of FQDN of you asterisk-java application.
		return "1.1.1.1";
	}

}
