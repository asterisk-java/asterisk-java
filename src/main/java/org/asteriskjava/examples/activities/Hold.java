package org.asteriskjava.examples.activities;

import org.asteriskjava.pbx.ActivityCallback;
import org.asteriskjava.pbx.ActivityStatusEnum;
import org.asteriskjava.pbx.Call;
import org.asteriskjava.pbx.CallerID;
import org.asteriskjava.pbx.Channel;
import org.asteriskjava.pbx.EndPoint;
import org.asteriskjava.pbx.PBX;
import org.asteriskjava.pbx.PBXFactory;
import org.asteriskjava.pbx.TechType;
import org.asteriskjava.pbx.activities.DialActivity;
import org.asteriskjava.pbx.activities.HoldActivity;

/**
 * dial somebody and then put them on hold.
 * 
 * @author bsutton
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
        EndPoint from = pbx.buildEndPoint(TechType.SIP, "100");
        // The caller ID to show on extension 100.
        CallerID fromCallerID = pbx.buildCallerID("100", "My Phone");

        // The caller ID to display on the called parties phone
        CallerID toCallerID = pbx.buildCallerID("83208100", "Asterisk Java is calling");
        // The party we are going to call.
        EndPoint to = pbx.buildEndPoint("SIP/default/5551234");

        // Start the dial and return immediately.
        // progress is provided via the ActivityCallback.
        pbx.dial(from, fromCallerID, to, toCallerID, new ActivityCallback<DialActivity>()
        {

            @Override
            public void progress(DialActivity activity, ActivityStatusEnum status, String message)
            {
                if (status == ActivityStatusEnum.SUCCESS)
                {
                    System.out.println("Dial all good so lets place them on hold");
                    PBX pbx = PBXFactory.getActivePBX();
                    // Call is up
                    Call call = activity.getNewCall();

                    // Place the remote party on hold.
                    HoldActivity hold = pbx.hold(call.getRemoteParty());

                    Channel heldChannel = hold.getChannel();

                    System.out.println("Held channel is " + heldChannel);
                }
                if (status == ActivityStatusEnum.FAILURE)
                    System.out.println("Oops something bad happened when we dialed.");
            }
        });

    }

}
