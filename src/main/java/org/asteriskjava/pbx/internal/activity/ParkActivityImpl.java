package org.asteriskjava.pbx.internal.activity;

import java.io.IOException;
import java.util.HashSet;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.asteriskjava.manager.TimeoutException;
import org.asteriskjava.pbx.ActivityCallback;
import org.asteriskjava.pbx.AsteriskSettings;
import org.asteriskjava.pbx.Call;
import org.asteriskjava.pbx.CallDirection;
import org.asteriskjava.pbx.Channel;
import org.asteriskjava.pbx.EndPoint;
import org.asteriskjava.pbx.ListenerPriority;
import org.asteriskjava.pbx.PBXException;
import org.asteriskjava.pbx.PBXFactory;
import org.asteriskjava.pbx.TechType;
import org.asteriskjava.pbx.activities.ParkActivity;
import org.asteriskjava.pbx.asterisk.wrap.actions.RedirectAction;
import org.asteriskjava.pbx.asterisk.wrap.events.ManagerEvent;
import org.asteriskjava.pbx.asterisk.wrap.events.ParkedCallEvent;
import org.asteriskjava.pbx.asterisk.wrap.response.ManagerResponse;
import org.asteriskjava.pbx.internal.core.AsteriskPBX;
import org.asteriskjava.util.Log;
import org.asteriskjava.util.LogFactory;

/**
 * The ParkActivity is used by the AsteriksPBX implementation to park a channel.
 * The park activity expects two channels which are two legs legs of the same
 * call. The parkChannel will be redirect to the njr-park extension. The
 * hangupChannel (the second leg of the call) will be hung up. The (obvious?)
 * limitation is that we can't park something like a conference call as it has
 * more than two channels.
 * 
 * @author bsutton
 */
public class ParkActivityImpl extends ActivityHelper<ParkActivity> implements ParkActivity
{
    private static final Log logger = LogFactory.getLog(ParkActivityImpl.class);

    /**
     * call - the call which is being packed.
     */
    private final Call _call;

    /**
     * parkChannel - the channel from the above call which is going to be
     * parked. extension.
     */
    private final Channel _parkChannel;

    private volatile EndPoint _parkingLot;

    private final CountDownLatch _latch = new CountDownLatch(1);

    /*
     * @param call the that is being parked.
     * @param parkChannel the channel from the call which is the one which will
     * be parked. All other channels in the call will be hungup.
     */
    public ParkActivityImpl(final Call call, final Channel parkChannel, final ActivityCallback<ParkActivity> listener)
    {
        super("ParkActivity", listener);

        if (call == null)
        {
            throw new IllegalArgumentException("call may not be null");
        }
        if (parkChannel == null)
        {
            throw new IllegalArgumentException("parkChannel may not be null");
        }
        this._parkChannel = parkChannel;
        this._call = call;

        if (!this._call.contains(parkChannel))
            throw new IllegalArgumentException("The parkChannel must be from the call.");

        this.startActivity(true);
    }

    @Override
    public boolean doActivity() throws PBXException
    {
        boolean success = false;
        final AsteriskPBX pbx = (AsteriskPBX) PBXFactory.getActivePBX();

        ParkActivityImpl.logger.debug("*******************************************************************************");
        ParkActivityImpl.logger.info("***********                    begin park               ****************");
        ParkActivityImpl.logger.info("***********            " + this._parkChannel + "                 ****************");
        ParkActivityImpl.logger.debug("*******************************************************************************");

        try
        {
            final AsteriskSettings profile = PBXFactory.getActiveProfile();

            if (!pbx.waitForChannelToQuiescent(this._parkChannel, 3000))
                throw new PBXException("Channel: " + this._parkChannel + " cannot be parked as it is still in transition.");

            // The extension must be a non-qualified name as
            // otherwise it would be of the form DIALPLAN/njr-park
            final RedirectAction redirect = new RedirectAction(this._parkChannel, profile.getManagementContext(),
                    pbx.getExtensionPark(), 1);

            final ManagerResponse response = pbx.sendAction(redirect, 1000);
            if ((response != null) && (response.getResponse().compareToIgnoreCase("success") == 0))
            {
                // Hangup the call as we have parked the other side of
                // the call.
                if (this._call.getDirection() == CallDirection.INBOUND)
                {
                    logger.warn("Hanging up");
                    pbx.hangup(this._call.getAcceptingParty());
                }
                else
                {
                    logger.warn("Hanging up");
                    pbx.hangup(this._call.getOriginatingParty());
                }
                success = true;
            }

        }
        catch (IllegalArgumentException | IllegalStateException | IOException | TimeoutException e)
        {
            ParkActivityImpl.logger.error(e, e);
            throw new PBXException(e);

        }
        finally
        {
            // we wait at most 2 seconds for the parking extension to
            // arrive.
            try
            {
                if (success)
                {
                    this._latch.await(2, TimeUnit.SECONDS);
                }

                if (this._parkingLot == null)
                {
                    success = false;
                    ParkActivityImpl.logger.warn("ParkCallEvent not recieved within 2 seconds of parking call.");
                    this.setLastException(new PBXException("ParkCallEvent  not recieved within 2 seconds of parking call."));
                }
            }
            catch (final InterruptedException e)
            {
                ParkActivityImpl.logger.error(e, e);

            }

        }
        return success;
    }

    @Override
    public EndPoint getParkingLot()
    {
        return this._parkingLot;
    }

    @Override
    public HashSet<Class< ? extends ManagerEvent>> requiredEvents()
    {
        HashSet<Class< ? extends ManagerEvent>> required = new HashSet<>();

        required.add(ParkedCallEvent.class);

        return required;
    }

    @Override
    synchronized public void onManagerEvent(final ManagerEvent event)
    {
        assert event instanceof ParkedCallEvent : "Unexpected event";

        final ParkedCallEvent parkedEvent = (ParkedCallEvent) event;
        final AsteriskPBX pbx = (AsteriskPBX) PBXFactory.getActivePBX();

        this._parkingLot = pbx.buildEndPoint(TechType.LOCAL, parkedEvent.getExten());
        this._latch.countDown();
    }

    @Override
    public ListenerPriority getPriority()
    {
        return ListenerPriority.NORMAL;
    }

}
