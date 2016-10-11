package org.asteriskjava.examples.activities;

import java.io.IOException;

import org.asteriskjava.manager.AuthenticationFailedException;
import org.asteriskjava.manager.TimeoutException;
import org.asteriskjava.pbx.ActivityCallback;
import org.asteriskjava.pbx.ActivityStatusEnum;
import org.asteriskjava.pbx.Call;
import org.asteriskjava.pbx.Call.OperandChannel;
import org.asteriskjava.pbx.CallerID;
import org.asteriskjava.pbx.EndPoint;
import org.asteriskjava.pbx.PBX;
import org.asteriskjava.pbx.PBXFactory;
import org.asteriskjava.pbx.activities.BlindTransferActivity;
import org.asteriskjava.pbx.activities.DialActivity;
import org.asteriskjava.pbx.internal.core.AsteriskPBX;
import org.asteriskjava.pbx.internal.core.TechType;

/**
 * dial somebody and then blind transfer the call to a third party.
 * 
 * @author bsutton
 */
public class BlindTransfer
{

    static public void main(String[] args) throws IOException, AuthenticationFailedException, TimeoutException
    {
        PBXFactory.init(new ExamplesAsteriskSettings());
        AsteriskPBX asteriskPbx = (AsteriskPBX) PBXFactory.getActivePBX();
        asteriskPbx.createAgiEntryPoint();
        blindTransfer();
    }

    static private void blindTransfer()
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
                    System.out.println("Dial all good so lets do a blind transfer");
                    PBX pbx = PBXFactory.getActivePBX();
                    // Call is up
                    Call call = activity.getNewCall();
                    CallerID toCallerID = pbx.buildCallerID("101", "I'm calling you");
                    EndPoint transferTarget = pbx.buildEndPoint(TechType.SIP, "101");
                    pbx.blindTransfer(call, OperandChannel.REMOTE_PARTY, transferTarget, toCallerID, false, 30L,
                            new ActivityCallback<BlindTransferActivity>()
                            {

                                @Override
                                public void progress(BlindTransferActivity activity, ActivityStatusEnum status, String message)
                                {
                                    // if success the blind transfer completed.
                                }
                            });
                }
                if (status == ActivityStatusEnum.FAILURE)
                    System.out.println("Oops something bad happened when we dialed.");
            }
        });

    }

}
