package org.asteriskjava.examples.activities;

import org.asteriskjava.pbx.ActivityCallback;
import org.asteriskjava.pbx.Call;
import org.asteriskjava.pbx.CallerID;
import org.asteriskjava.pbx.EndPoint;
import org.asteriskjava.pbx.PBX;
import org.asteriskjava.pbx.PBXException;
import org.asteriskjava.pbx.PBXFactory;
import org.asteriskjava.pbx.activities.DialActivity;
import org.asteriskjava.pbx.internal.core.TechType;

public class Dial
{
	static public void main(String[] args)
	{
		syncDial();
		asyncDial();

	}

	/**
	 * Simple synchronous dial. The dial method won't return until the dial starts.
	 * Using this method will lockup your UI until the dial starts.
	 * For better control use the async Dial method below.
	 */
	static private void syncDial()
	{
		try
		{
			PBX pbx = PBXFactory.getActivePBX();

			// We are going to dial from extension 100
			EndPoint from = pbx.buildEndPoint("100", TechType.SIP);
			// The caller ID to show on extension 100.
			CallerID fromCallerID = pbx.buildCallerID("100", "My Phone");

			// The caller ID to display on the called parties phone
			CallerID toCallerID = pbx.buildCallerID("83208100", "Asterisk Java is calling");
			// The party we are going to call.
			EndPoint to = pbx.buildEndPoint("SIP/default/5551234");

			// Trunk is currently ignored so set to null
			// The call is dialed and only returns when the call comes up (it
			// doesn't wait for the remote end to answer).
			DialActivity dial = pbx.dial(null, from, fromCallerID, to, toCallerID);

			Call call = dial.getNewCall();

			pbx.hangup(call.getOriginatingParty());
		}
		catch (PBXException e)
		{
			System.out.println(e);
		}
	}

	static private void asyncDial()
	{
		PBX pbx = PBXFactory.getActivePBX();

		// We are going to dial from extension 100
		EndPoint from = pbx.buildEndPoint("100", TechType.SIP);
		// The caller ID to show on extension 100.
		CallerID fromCallerID = pbx.buildCallerID("100", "My Phone");

		// The caller ID to display on the called parties phone
		CallerID toCallerID = pbx.buildCallerID("83208100", "Asterisk Java is calling");
		// The party we are going to call.
		EndPoint to = pbx.buildEndPoint("SIP/default/5551234");

		// Start the dial and return immediately.
		// progress is provided via the ActivityCallback.
		pbx.dial(null, from, fromCallerID, to, toCallerID, new ActivityCallback<DialActivity>()
		{
			@Override
			public void start(DialActivity activity)
			{
				System.out.println("The dial has started");
				// We can stop it progress further by cancelling the dial
				// activity.markCancelled();
			}

			@Override
			public void progess(DialActivity activity, String message)
			{
				System.out.println("Dial status: " + message);
			}

			@Override
			public void completed(DialActivity activity, boolean success)
			{
				if (success)
				{
					System.out.println("Dial all good");
					try
					{
						// Call is up
						Call call = activity.getNewCall();
						// So lets just hangup the call
						PBXFactory.getActivePBX().hangup(call.getOriginatingParty());
					}
					catch (PBXException e)
					{
						System.out.println(e);
					}
				}
				else
					System.out.println("Oops something bad happened when we dialed.");
			}
		});

	}

}
