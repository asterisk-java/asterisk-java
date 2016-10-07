package org.asteriskjava.pbx.internal.asterisk;

import java.util.concurrent.atomic.AtomicReference;

public class PBXSettingsManager
{

	final static AtomicReference<AsteriskSettings> profile = new AtomicReference<>();

	public static void setAsteriskSettings(AsteriskSettings profile)
	{
		PBXSettingsManager.profile.set(profile);

	}

	public static AsteriskSettings getActiveProfile()
	{
		AsteriskSettings activeProfile = profile.get();
		if (activeProfile == null)
		{
			throw new RuntimeException(
					"you must call setAsteriskSettings() before getActiveProfile() is called the first time");
		}

		return activeProfile;
	}

}
