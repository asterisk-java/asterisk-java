package org.asteriskjava.examples.activities;

import org.asteriskjava.pbx.ActivityCallback;
import org.asteriskjava.pbx.ActivityStatusEnum;
import org.asteriskjava.pbx.Call;
import org.asteriskjava.pbx.CallerID;
import org.asteriskjava.pbx.EndPoint;
import org.asteriskjava.pbx.PBX;
import org.asteriskjava.pbx.PBXException;
import org.asteriskjava.pbx.PBXFactory;
import org.asteriskjava.pbx.TechType;
import org.asteriskjava.pbx.Trunk;
import org.asteriskjava.pbx.activities.DialActivity;
import org.asteriskjava.util.Log;
import org.asteriskjava.util.LogFactory;

public class Dial
{
    static private Log logger = LogFactory.getLog(Dial.class);

    static public void main(String[] args)
    {
        syncDial();
        asyncDial();

    }

    /**
     * Simple synchronous dial. The dial method won't return until the dial
     * starts. Using this method will lockup your UI until the dial starts. For
     * better control use the async Dial method below.
     */
    static private void syncDial()
    {
        try
        {
            PBX pbx = PBXFactory.getActivePBX();

            // The trunk MUST match the section header (e.g. [default]) that
            // appears
            // in your /etc/asterisk/sip.d file (assuming you are using a SIP
            // trunk).
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

            // Trunk is currently ignored so set to null
            // The call is dialed and only returns when the call comes up (it
            // doesn't wait for the remote end to answer).
            DialActivity dial = pbx.dial(from, fromCallerID, to, toCallerID);

            Call call = dial.getNewCall();

            Thread.sleep(20000);

            logger.warn("Hanging up");
            pbx.hangup(call);
        }
        catch (PBXException | InterruptedException e)
        {
            System.out.println(e);
        }
    }

    static private void asyncDial()
    {
        PBX pbx = PBXFactory.getActivePBX();

        // We are going to dial from extension 100
        EndPoint from = pbx.buildEndPoint(TechType.SIP, "100");
        // The caller ID to show on extension 100.
        CallerID fromCallerID = pbx.buildCallerID("100", "My Phone");

        // The caller ID to display on the called parties phone
        CallerID toCallerID = pbx.buildCallerID("83208100", "Asterisk Java is calling");
        // The party we are going to call.
        EndPoint to = pbx.buildEndPoint(TechType.SIP, pbx.buildTrunk("default"), "5551234");

        // Start the dial and return immediately.
        // progress is provided via the ActivityCallback.
        pbx.dial(from, fromCallerID, to, toCallerID, new ActivityCallback<DialActivity>()
        {

            @Override
            public void progress(DialActivity activity, ActivityStatusEnum status, String message)
            {
                if (status == ActivityStatusEnum.SUCCESS)
                {
                    System.out.println("Dial all good");
                    try
                    {
                        // Call is up
                        Call call = activity.getNewCall();
                        // So lets just hangup the call
                        logger.warn("Hanging up");
                        PBXFactory.getActivePBX().hangup(call.getOriginatingParty());
                    }
                    catch (PBXException e)
                    {
                        System.out.println(e);
                    }
                }
                if (status == ActivityStatusEnum.FAILURE)
                    System.out.println("Oops something bad happened when we dialed.");
            }
        });

    }

}
