package org.asteriskjava.pbx.internal.managerAPI;

import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.asteriskjava.pbx.CallerID;
import org.asteriskjava.pbx.Channel;
import org.asteriskjava.pbx.EndPoint;
import org.asteriskjava.pbx.ListenerPriority;
import org.asteriskjava.pbx.NewChannelListener;
import org.asteriskjava.pbx.PBX;
import org.asteriskjava.pbx.PBXException;
import org.asteriskjava.pbx.PBXFactory;
import org.asteriskjava.pbx.agi.AgiChannelActivityDial;
import org.asteriskjava.pbx.asterisk.wrap.events.BridgeEvent;
import org.asteriskjava.pbx.asterisk.wrap.events.HangupEvent;
import org.asteriskjava.pbx.asterisk.wrap.events.LinkEvent;
import org.asteriskjava.pbx.asterisk.wrap.events.ManagerEvent;
import org.asteriskjava.pbx.asterisk.wrap.events.UnlinkEvent;
import org.asteriskjava.pbx.internal.core.AsteriskPBX;
import org.asteriskjava.util.Log;
import org.asteriskjava.util.LogFactory;

public class Dial extends EventListenerBaseClass
{
    private static final Log logger = LogFactory.getLog(Dial.class);

    private final OriginateResult result[] = new OriginateResult[2];

    private CountDownLatch _latch;

    public Dial(final String descriptiveName)
    {
        super(descriptiveName, PBXFactory.getActivePBX());
    }

    /**
     * Dials the targetEndPoint connecting the call to the localHandset. The
     * dial is done in two parts as we need to set the auto answer header
     * differently for each phone. For the localHandset we set auto answer on,
     * whilst for the remote targetEndPoint we MUST not auto answer the phone.
     * 
     * @param localHandset the users handset we are going to dial from (e.g.
     *            receptionist phone).
     * @param targetEndPoint - the remote handset we are going to connect the
     *            localHandset to.
     * @param dialContext - context we are going to dial from.
     * @param hideCallerId
     * @param channelVarsToSet
     * @param callerId - the callerID to display on the targetEndPoint.
     * @return
     * @throws PBXException
     */
    public OriginateResult[] dial(final NewChannelListener listener, final EndPoint localHandset,
            final EndPoint targetEndPoint, final String dialContext, final CallerID callerID, final boolean hideCallerId,
            Map<String, String> channelVarsToSet) throws PBXException
    {
        final PBX pbx = PBXFactory.getActivePBX();

        try (final OriginateToExtension originate = new OriginateToExtension(listener))
        {

            this.startListener();

            // First bring the operator's handset up and connect it to the
            // 'njr-dial' extension where they can
            // wait whilst we complete the second leg
            final OriginateResult trcResult = originate.originate(localHandset, pbx.getExtensionAgi(), true,
                    ((AsteriskPBX) pbx).getManagementContext(), callerID, null, hideCallerId, channelVarsToSet);

            this.result[0] = trcResult;
            if (trcResult.isSuccess() == true)
            {

                try
                {
                    if (targetEndPoint instanceof HoldAtAgi)
                    {
                        if (trcResult.getChannel().waitForChannelToReachAgi(30, TimeUnit.SECONDS))
                        {
                            // call is now in agi
                            System.out.println("Call is in agi");
                        }
                        else
                        {
                            // call never reached agi
                            System.out.println("Call never reached agi");
                        }
                    }
                    else
                    {

                        // The call is now up, so connect it to
                        // the
                        // destination.
                        this._latch = new CountDownLatch(1);

                        trcResult.getChannel().setCurrentActivityAction(
                                new AgiChannelActivityDial(targetEndPoint.getFullyQualifiedName()));

                        this._latch.await(30, TimeUnit.SECONDS);

                    }
                }
                catch (final InterruptedException e)
                {
                    // noop
                }
            }

            return this.result;
        }
        finally
        {
            this.close();
        }
    }

    @Override
    public HashSet<Class< ? extends ManagerEvent>> requiredEvents()
    {
        HashSet<Class< ? extends ManagerEvent>> required = new HashSet<>();

        required.add(BridgeEvent.class);
        // bridge event is a subclass of linkevent, so we need link & unlink in
        // our list of events to support Asterisk 1.4
        required.add(LinkEvent.class);
        required.add(UnlinkEvent.class);
        required.add(HangupEvent.class);

        return required;
    }

    @Override
    public void onManagerEvent(final ManagerEvent event)
    {
        if (event instanceof BridgeEvent)
        {
            final BridgeEvent link = (BridgeEvent) event;

            if ((link.getChannel1() != null) && (this.result[0] != null)
                    && link.getChannel1().isSame(this.result[0].getChannel()))
            {

                Dial.logger.debug("Dial out bridged on " + link.getChannel1() + " to " + link.getChannel2()); //$NON-NLS-1$ //$NON-NLS-2$
                this.result[1] = new OriginateResult();

                this.result[1].setChannelData(link.getChannel2());
                this._latch.countDown();
            }
        }
        else if (event instanceof HangupEvent)
        {
            final HangupEvent hangup = (HangupEvent) event;
            final Channel hangupChannel = hangup.getChannel();

            if (Dial.logger.isDebugEnabled())
            {
                Dial.logger.debug("hangup :" + hangupChannel); //$NON-NLS-1$
                Dial.logger.debug("channel 0:" + this.result[0]); //$NON-NLS-1$
                Dial.logger.debug("channel 1:" + this.result[1]); //$NON-NLS-1$
            }
            if ((this.result[0] != null) && this.result[0].isSuccess() && hangupChannel.isSame(this.result[0].getChannel()))
            {
                if (this._latch != null)
                {
                    this._latch.countDown();
                }
            }
        }
    }

    @Override
    public ListenerPriority getPriority()
    {
        return ListenerPriority.NORMAL;
    }

}
