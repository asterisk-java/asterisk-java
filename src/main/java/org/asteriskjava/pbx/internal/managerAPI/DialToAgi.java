package org.asteriskjava.pbx.internal.managerAPI;

import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.asteriskjava.pbx.AgiChannelActivityAction;
import org.asteriskjava.pbx.CallerID;
import org.asteriskjava.pbx.Channel;
import org.asteriskjava.pbx.EndPoint;
import org.asteriskjava.pbx.ListenerPriority;
import org.asteriskjava.pbx.NewChannelListener;
import org.asteriskjava.pbx.PBX;
import org.asteriskjava.pbx.PBXException;
import org.asteriskjava.pbx.PBXFactory;
import org.asteriskjava.pbx.asterisk.wrap.events.HangupEvent;
import org.asteriskjava.pbx.asterisk.wrap.events.ManagerEvent;
import org.asteriskjava.pbx.internal.core.AsteriskPBX;
import org.asteriskjava.util.Log;
import org.asteriskjava.util.LogFactory;

public class DialToAgi extends EventListenerBaseClass
{
    private static final Log logger = LogFactory.getLog(DialToAgi.class);

    private final OriginateResult result[] = new OriginateResult[2];

    private volatile boolean hangupDetected = false;

    private OriginateToExtension originator;

    public DialToAgi(final String descriptiveName)
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
            final AgiChannelActivityAction action, final CallerID callerID, Integer timeout, final boolean hideCallerId,
            Map<String, String> channelVarsToSet) throws PBXException, InterruptedException
    {
        final PBX pbx = PBXFactory.getActivePBX();

        try (final OriginateToExtension originate = new OriginateToExtension(listener))
        {
            this.startListener();
            originator = originate;

            // First bring the operator's handset up and connect it to the
            // 'njr-dial' extension where they can
            // wait whilst we complete the second leg
            final OriginateResult trcResult = originate.originate(localHandset, pbx.getExtensionAgi(), true,
                    ((AsteriskPBX) pbx).getManagementContext(), callerID, timeout, hideCallerId, channelVarsToSet);

            this.result[0] = trcResult;
            if (trcResult.isSuccess() == true)
            {
                if (trcResult.getChannel() != null)
                {
                    trcResult.getChannel().setCurrentActivityAction(action);
                    if (trcResult.getChannel().waitForChannelToReachAgi(30, TimeUnit.SECONDS))
                    {
                        logger.info("Call reached AGI");
                    }
                    else
                    {
                        logger.error("Call never reached agi");
                    }
                }
                else
                {
                    logger.error("Call never reached agi");
                }
            }
            else
            {
                logger.warn("Originate failed: " + trcResult.getAbortReason());
            }
            logger.info("Hangup status is " + hangupDetected);

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
        required.add(HangupEvent.class);

        return required;
    }

    @Override
    public void onManagerEvent(final ManagerEvent event)
    {
        if (event instanceof HangupEvent)
        {
            final HangupEvent hangup = (HangupEvent) event;
            final Channel hangupChannel = hangup.getChannel();

            if (DialToAgi.logger.isDebugEnabled())
            {
                DialToAgi.logger.debug("hangup :" + hangupChannel); //$NON-NLS-1$
                DialToAgi.logger.debug("channel 0:" + this.result[0]); //$NON-NLS-1$
                DialToAgi.logger.debug("channel 1:" + this.result[1]); //$NON-NLS-1$
            }
            if ((this.result[0] != null) && this.result[0].isSuccess() && hangupChannel.isSame(this.result[0].getChannel()))
            {
                hangupDetected = true;
            }
        }
    }

    @Override
    public ListenerPriority getPriority()
    {
        return ListenerPriority.NORMAL;
    }

    public void abort()
    {
        originator.abort("user abort");
    }

}
