package org.asteriskjava.examples.activities;

import org.asteriskjava.pbx.ActivityCallback;
import org.asteriskjava.pbx.Call;
import org.asteriskjava.pbx.CallerID;
import org.asteriskjava.pbx.Channel;
import org.asteriskjava.pbx.EndPoint;
import org.asteriskjava.pbx.PBX;
import org.asteriskjava.pbx.PBXFactory;
import org.asteriskjava.pbx.activities.DialActivity;
import org.asteriskjava.pbx.activities.HoldActivity;
import org.asteriskjava.pbx.internal.core.TechType;

/**
 * dial somebody and then put them on hold.
 * 
 * @author bsutton
 *
 */
public class Hold
{

	static public void main(String[] args)
	{
		hold();
	}

	static private void hold()
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
					System.out.println("Dial all good so lets place them on hold");
					PBX pbx = PBXFactory.getActivePBX();
					// Call is up
					Call call = activity.getNewCall();
					
					// Place the remote party on hold.
					HoldActivity hold = pbx.hold(call.getRemoteParty());
					
					Channel heldChannel = hold.getChannel();
				}
				else
					System.out.println("Oops something bad happened when we dialed.");
			}
		});

	}

}
