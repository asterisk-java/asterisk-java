package org.asteriskjava.pbx.internal.core;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.asteriskjava.manager.TimeoutException;
import org.asteriskjava.pbx.Activity;
import org.asteriskjava.pbx.ActivityCallback;
import org.asteriskjava.pbx.ActivityStatusEnum;
import org.asteriskjava.pbx.AsteriskSettings;
import org.asteriskjava.pbx.CallerID;
import org.asteriskjava.pbx.Channel;
import org.asteriskjava.pbx.EndPoint;
import org.asteriskjava.pbx.ListenerPriority;
import org.asteriskjava.pbx.PBX;
import org.asteriskjava.pbx.PBXException;
import org.asteriskjava.pbx.PBXFactory;
import org.asteriskjava.pbx.asterisk.wrap.actions.GetVarAction;
import org.asteriskjava.pbx.asterisk.wrap.actions.OriginateAction;
import org.asteriskjava.pbx.asterisk.wrap.events.HangupEvent;
import org.asteriskjava.pbx.asterisk.wrap.events.ManagerEvent;
import org.asteriskjava.pbx.asterisk.wrap.events.NewChannelEvent;
import org.asteriskjava.pbx.asterisk.wrap.events.OriginateResponseEvent;
import org.asteriskjava.pbx.asterisk.wrap.response.ManagerResponse;
import org.asteriskjava.pbx.internal.managerAPI.EventListenerBaseClass;
import org.asteriskjava.pbx.internal.managerAPI.OriginateBaseClass;
import org.asteriskjava.util.Log;
import org.asteriskjava.util.LogFactory;

public class DialLocalToAgiActivity extends EventListenerBaseClass implements Runnable, Activity
{

    private EndPoint from;
    private CallerID fromCallerID;
    private Thread thread;
    private final Log logger = LogFactory.getLog(this.getClass());
    private String originateId;

    CountDownLatch latch = new CountDownLatch(1);
    private List<Channel> channels = new LinkedList<>();
    private ActivityCallback<DialLocalToAgiActivity> callback;
    private Map<String, String> channelVarsToSet;

    public DialLocalToAgiActivity(EndPoint from, CallerID fromCallerID, ActivityCallback<DialLocalToAgiActivity> callback,
            Map<String, String> channelVarsToSet)
    {
        super("Dial " + from + " to AGI", PBXFactory.getActivePBX());
        this.from = from;
        this.fromCallerID = fromCallerID;
        this.callback = callback;
        this.channelVarsToSet = channelVarsToSet;

        thread = new Thread(this, "Dial " + from + " to AGI");
        thread.start();
    }
    // Logger logger = LogManager.getLogger();

    @Override
    public void run()
    {
        logger.debug("*******************************************************************************");
        logger.info("***********                    begin dial local to AGI                  ****************");
        logger.debug("***********                                                      ****************");
        logger.debug("*******************************************************************************");
        final AsteriskSettings settings = PBXFactory.getActiveProfile();

        AsteriskPBX pbx = (AsteriskPBX) PBXFactory.getActivePBX();

        final OriginateAction originate = new OriginateAction();
        originate.setEndPoint(from);
        originate.setContext(settings.getManagementContext());
        originate.setExten(pbx.getExtensionAgi());
        originate.setPriority(1);
        originate.setCallerId(fromCallerID);
        originate.setTimeout(30000);
        originateId = originate.getActionId();

        Map<String, String> myVars = new HashMap<>();

        myVars.put("__" + OriginateBaseClass.NJR_ORIGINATE_ID, this.originateId);
        if (channelVarsToSet != null)
        {
            myVars.putAll(channelVarsToSet);
        }

        originate.setVariables(myVars);

        try
        {
            this.startListener();

            pbx.sendAction(originate, 30000);
            latch.await(30, TimeUnit.SECONDS);
            callback.progress(this, ActivityStatusEnum.SUCCESS, ActivityStatusEnum.SUCCESS.getDefaultMessage());

        }
        catch (Exception e)
        {
            logger.error(e, e);
        }
        finally
        {
            this.close();
        }

    }

    public void abort(final String reason)
    {
        logger.warn("Aborting originate ");

        for (Channel channel : channels)
        {
            PBX pbx = PBXFactory.getActivePBX();
            try
            {
                pbx.hangup(channel);
            }
            catch (IllegalArgumentException | IllegalStateException | PBXException e)
            {
                logger.error(e, e);

            }
        }
        latch.countDown();

    }

    @Override
    synchronized public void onManagerEvent(final ManagerEvent event)
    {
        if (event instanceof NewChannelEvent)
        {
            final NewChannelEvent newState = (NewChannelEvent) event;
            final Channel channel = newState.getChannel();
            AsteriskPBX pbx = (AsteriskPBX) PBXFactory.getActivePBX();
            final GetVarAction var = new GetVarAction(channel, OriginateBaseClass.NJR_ORIGINATE_ID);

            ManagerResponse response;
            try
            {
                if (channel.isLocal())
                {
                    int wait = 5;
                    while (wait > 0)
                    {
                        wait--;
                        response = pbx.sendAction(var, 500);
                        String channelsOriginateId = response.getAttribute("value");
                        if (originateId.equalsIgnoreCase(channelsOriginateId))
                        {
                            logger.info("Found the local channel " + channel);
                            channels.add(channel);
                            wait = 0;
                            if (channels.size() == 2)
                            {
                                latch.countDown();
                            }
                        }
                        else if (channelsOriginateId == null || channelsOriginateId.length() == 0)

                        {
                            Thread.sleep(100);
                            logger.error("Waiting for variables on " + channel);
                        }
                        else
                        {
                            logger.error("Didn't match " + channel + " " + channelsOriginateId + "!=" + originateId);

                            wait = 0;
                        }
                    }
                }
            }
            catch (IllegalArgumentException | IllegalStateException | IOException | TimeoutException e)
            {
                logger.error(e, e);
            }
            catch (InterruptedException e)
            {
                logger.error(e, e);
            }

        }
    }

    @Override
    public HashSet<Class< ? extends ManagerEvent>> requiredEvents()
    {
        HashSet<Class< ? extends ManagerEvent>> required = new HashSet<>();

        required.add(OriginateResponseEvent.class);
        required.add(HangupEvent.class);
        required.add(NewChannelEvent.class);

        return required;
    }

    @Override
    public ListenerPriority getPriority()
    {
        return ListenerPriority.NORMAL;
    }

    @Override
    public Throwable getLastException()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean isSuccess()
    {
        // TODO Auto-generated method stub
        return channels.size() == 2;
    }

    public Channel getChannel1()
    {
        return channels.get(0);

    }

    public Channel getChannel2()
    {
        return channels.get(1);

    }
}
