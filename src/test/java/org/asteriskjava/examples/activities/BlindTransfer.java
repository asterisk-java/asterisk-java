package org.asteriskjava.examples.activities;

import org.asteriskjava.pbx.ActivityCallback;
import org.asteriskjava.pbx.Call;
import org.asteriskjava.pbx.Call.OperandChannel;
import org.asteriskjava.pbx.CallerID;
import org.asteriskjava.pbx.EndPoint;
import org.asteriskjava.pbx.PBX;
import org.asteriskjava.pbx.PBXException;
import org.asteriskjava.pbx.PBXFactory;
import org.asteriskjava.pbx.activities.BlindTransferActivity;
import org.asteriskjava.pbx.activities.DialActivity;
import org.asteriskjava.pbx.internal.core.TechType;

/**
 * dial somebody and then blind transfer the call to a third party.
 * 
 * @author bsutton
 *
 */
public class BlindTransfer
{

	static public void main(String[] args)
	{
		blindTransfer();
	}

	static private void blindTransfer()
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
					System.out.println("Dial all good so lets do a blind transfer");
					PBX pbx = PBXFactory.getActivePBX();
					// Call is up
					Call call = activity.getNewCall();
					CallerID toCallerID = pbx.buildCallerID("101", "I'm calling you");
					EndPoint transferTarget = pbx.buildEndPoint("101", TechType.SIP);
					pbx.blindTransfer(call, OperandChannel.REMOTE_PARTY, transferTarget, toCallerID, false, 30L,
							new ActivityCallback<BlindTransferActivity>()
							{

								@Override
								public void start(BlindTransferActivity activity)
								{
									// TODO Auto-generated method stub

								}

								@Override
								public void progess(BlindTransferActivity activity, String message)
								{
									// TODO Auto-generated method stub

								}

								@Override
								public void completed(BlindTransferActivity activity, boolean success)
								{
									// if success the blind transfer completed.
								}
							});
				}
				else
					System.out.println("Oops something bad happened when we dialed.");
			}
		});

	}

}
