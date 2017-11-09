package org.asteriskjava.pbx;

/**
 * A convenience class that provides a set of defaults that will work
 * for most people.
 * 
 * You can over-ride any specific methods for settings  that you need to change.
 * 
 * Key thing to watch out for is that we user a meetme range of 3000-3200.
 * If you are already using this range for other meetme rooms then you need
 * to over-ride the getMeetMeBaseAddress.
 * 
 * If you are using handsets other than yealink or snom you should also over-ride 
 * the getAutoAnswer settings.
 * 
 * @author bsutton
 *
 */
public abstract class DefaultAsteriskSettings implements AsteriskSettings
{

	@Override
	public int getManagerPortNo()
	{
		return 5060;
	}

	@Override
	public Integer getMeetmeBaseAddress()
	{
		// We use conference rooms. Just make certain that the base address
		// doesn't overlap any conference rooms you are currently running on your pbx.
		// You should probably allow 200 conference rooms here e.g. (3000-3200)
		// Actions like transfers (and conference calls) use a conference room.
		return new Integer(3000);
	}

	@Override
	public boolean getDisableBridge()
	{
		// Return false if you version of asterisk supports bridging channels.
		// All version after 1.8 support bridging.
		return false;
	}

	@Override
	public String getManagementContext()
	{
		// This is an asterisk dialplan context.
		// Simply pick a context which doesn't already exist
		// and use AsteriskPBX.createAgiEntryPoint()
		// to inject the necessary dialplan used by asterisk-java
		return "asteriskjava";
	}

	@Override
	public String getExtensionPark()
	{
		// Return an asterisk dialplan extension (within the Management Context)
		// that will be used to park calls.
		// Just ensure that its a unique asterisk dialplan extension name.
		return "asterisk-java-park";
	}

	@Override
	public int getDialTimeout()
	{
		// Return the amount of time to wait (in seconds) when dialing before we give up
		// when the call isn't answered
		return 30;
	}

	/*
	 * If you are using handsets other than yealink or snom you should over-ride
	 * these method to return the correct auto-answer string for you handset type.
	 * 
	 * If you neve use the auto answer feature in the dial command you can
	 * ignore this setting.
	 */
	@Override
	public String getAutoAnswer()
	{
		// Return the auto-answer heading used by your organisations handsets
		// to force them to auto-answer when we ring the handset.
		// Bad luck if you have incompatible handsets.
		return "Call-Info:\\; answer-after=0";
	}

	@Override
	public String getAgiExtension()
	{
		// Return an asterisk dialplan extension (within the Management Context)
		// that will be used to inject the agi dialplan entry point used by Activites..
		// Just ensure that its a unique asterisk dialplan extension name.
		return "asterisk-java-agi";
	}

	@Override
	public boolean getCanDetectHangup()
	{
		// Return try if the telephony tech you are dialing from can reliabily
		// detect hangups.
		return true;
	}

}
