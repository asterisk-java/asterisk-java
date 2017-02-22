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
import org.asteriskjava.pbx.TechType;
import org.asteriskjava.pbx.Trunk;
import org.asteriskjava.pbx.activities.BlindTransferActivity;
import org.asteriskjava.pbx.activities.DialActivity;
import org.asteriskjava.pbx.internal.core.AsteriskPBX;

/**
 * dial somebody and then blind transfer the call to a third party.
 * 
 * @author bsutton
 */
public class BlindTransfer
{

    static public void main(String[] args) throws IOException, AuthenticationFailedException, TimeoutException
    {
    	/**
    	 * Initialise the PBX Factory. You need to implement your own AsteriskSettings class.
    	 */
        PBXFactory.init(new ExamplesAsteriskSettings());
        
        /**
         * Activities utilise an agi entry point in your dial plan.
         * You can create your own entry point in dialplan or have
         * asterisk-java add it automatically
         */
        AsteriskPBX asteriskPbx = (AsteriskPBX) PBXFactory.getActivePBX();
        asteriskPbx.createAgiEntryPoint();
        
        // We are all configured lets try and do a blind transfer.
        blindTransfer();
    }

    static private void blindTransfer()
    {
        PBX pbx = PBXFactory.getActivePBX();
        
        // The trunk MUST match the section header (e.g. [default]) that appears
        // in your /etc/asterisk/sip.d file (assuming you are using a SIP trunk).
        // The trunk is used to select which SIP trunk to dial through.
        Trunk trunk = pbx.buildTrunk("default");

        // We are going to dial from extension 100
        EndPoint from = pbx.buildEndPoint(TechType.SIP, "100");
        // The caller ID to show on extension 100.
        CallerID fromCallerID = pbx.buildCallerID("100", "My Phone");

        // The caller ID to display on the called parties phone
        CallerID toCallerID = pbx.buildCallerID("83208100", "Asterisk Java is calling");
        // The party we are going to call.
        EndPoint to = pbx.buildEndPoint(TechType.SIP, trunk, "5551234");

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
